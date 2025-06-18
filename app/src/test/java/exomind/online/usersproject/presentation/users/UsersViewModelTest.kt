package exomind.online.usersproject.presentation.users

import exomind.online.usersproject.R
import exomind.online.usersproject.domain.AddUserUseCase
import exomind.online.usersproject.domain.DeleteUserUseCase
import exomind.online.usersproject.domain.GetUsersUseCase
import exomind.online.usersproject.domain.models.AddUser
import exomind.online.usersproject.domain.models.User
import exomind.online.usersproject.presentation.users.models.UIEffect
import exomind.online.usersproject.presentation.users.models.UIEvent
import exomind.online.usersproject.presentation.users.models.UIState
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.lang.RuntimeException

@OptIn(ExperimentalCoroutinesApi::class)
class UsersViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private val getUsersUseCase: GetUsersUseCase = mockk()
    private val addUserUseCase: AddUserUseCase = mockk()
    private val deleteUserUseCase: DeleteUserUseCase = mockk()
    private lateinit var viewModel: UsersViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is Success when getUsersUseCase succeeds`() = runTest {
        // GIVEN
        val users = listOf(
            User(id = 1, name = "Alice", email = "alice@test", gender = "female")
        )
        coEvery { getUsersUseCase() } returns flowOf(users)

        // WHEN
        viewModel = UsersViewModel(getUsersUseCase, addUserUseCase, deleteUserUseCase)
        advanceUntilIdle()

        // THEN
        val state = viewModel.state.first() as UIState.Success
        assertEquals(users, state.items)
        coVerify(exactly = 1) { getUsersUseCase() }
    }

    @Test
    fun `initial state is Error when getUsersUseCase fails`() = runTest {
        // GIVEN
        coEvery { getUsersUseCase() } returns flow { throw RuntimeException("fail") }

        // WHEN
        viewModel = UsersViewModel(getUsersUseCase, addUserUseCase, deleteUserUseCase)
        advanceUntilIdle()

        // THEN
        val state = viewModel.state.first() as UIState.Error
        assertEquals("fail", state.message)
        coVerify(exactly = 1) { getUsersUseCase() }
    }

    @Test
    fun `onEvent AddUser calls addUserUseCase, refreshes list, and emits ShowToast effect`() =
        runTest {
            // GIVEN
            val usersFirst = listOf(User(id = 1, name = "A", email = "a@test", gender = "female"))
            val usersAfter = listOf(
                User(id = 1, name = "A", email = "a@test", gender = "female"),
                User(id = 2, name = "B", email = "b@test", gender = "male")
            )
            coEvery { getUsersUseCase() } returnsMany listOf(flowOf(usersFirst), flowOf(usersAfter))
            coEvery {
                addUserUseCase(
                    AddUser(
                        name = "B",
                        email = "b@test",
                        gender = "male"
                    )
                )
            } returns Result.success(Unit)

            // WHEN
            viewModel = UsersViewModel(getUsersUseCase, addUserUseCase, deleteUserUseCase)
            advanceUntilIdle()

            var receivedEffect: UIEffect? = null
            val job = launch(testDispatcher) { viewModel.effects.collect { receivedEffect = it } }

            viewModel.onEvent(UIEvent.AddUser(name = "B", email = "b@test", gender = "male"))
            advanceUntilIdle()

            // THEN
            coVerify(exactly = 1) {
                addUserUseCase(
                    AddUser(
                        name = "B",
                        email = "b@test",
                        gender = "male"
                    )
                )
            }
            coVerify(exactly = 2) { getUsersUseCase() } // one for init, one after add
            assertTrue(receivedEffect is UIEffect.ShowToast)
            assertEquals(
                R.string.user_added_message,
                (receivedEffect as UIEffect.ShowToast).stringResource
            )

            job.cancel()
        }

    @Test
    fun `onEvent DeleteUser calls deleteUserUseCase, refreshes list, and emits ShowToast effect`() =
        runTest {
            // GIVEN
            val usersFirst = listOf(User(id = 1, name = "A", email = "a@test", gender = "female"))
            val usersAfter = emptyList<User>()
            coEvery { getUsersUseCase() } returnsMany listOf(flowOf(usersFirst), flowOf(usersAfter))
            coEvery { deleteUserUseCase(1) } returns Result.success(Unit)

            // WHEN
            viewModel = UsersViewModel(getUsersUseCase, addUserUseCase, deleteUserUseCase)
            advanceUntilIdle()

            var receivedEffect: UIEffect? = null
            val job = launch(testDispatcher) { viewModel.effects.collect { receivedEffect = it } }

            viewModel.onEvent(UIEvent.DeleteUser(userId = 1))
            advanceUntilIdle()

            // THEN
            coVerify(exactly = 1) { deleteUserUseCase(1) }
            coVerify(exactly = 2) { getUsersUseCase() } // one for init, one after delete
            assertTrue(receivedEffect is UIEffect.ShowToast)
            assertEquals(
                R.string.user_deleted_message,
                (receivedEffect as UIEffect.ShowToast).stringResource
            )

            job.cancel()
        }

    @Test
    fun `onEvent Retry calls getUsersUseCase again`() = runTest {
        // GIVEN
        val users = listOf(User(id = 1, name = "A", email = "a@test", gender = "female"))
        coEvery { getUsersUseCase() } returns flowOf(users)

        // WHEN
        viewModel = UsersViewModel(getUsersUseCase, addUserUseCase, deleteUserUseCase)
        advanceUntilIdle()
        viewModel.onEvent(UIEvent.Retry)
        advanceUntilIdle()

        // THEN
        coVerify(atLeast = 2) { getUsersUseCase() } // init and retry
        val state = viewModel.state.first() as UIState.Success
        assertEquals(users, state.items)
    }
}
