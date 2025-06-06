package exomind.online.usersproject.domain

import exomind.online.usersproject.domain.models.User
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class GetUsersUseCaseTest {

    private val repository = mockk<UsersRepository>()
    private val useCase = GetUsersUseCase(repository)

    @Test
    fun `invoke returns list of users when repository succeeds`() = runTest {
        // GIVEN
        val user1 = User(id = 1, name = "Alice", email = "alice@test", gender = "female")
        val user2 = User(id = 2, name = "Bob", email = "bob@test", gender = "male")
        coEvery { repository.getUsers() } returns listOf(user1, user2)

        // WHEN
        val result = useCase()

        // THEN
        assertEquals(listOf(user1, user2), result)
    }

    @Test(expected = Exception::class)
    fun `invoke throws exception when repository fails`() = runTest {
        // GIVEN
        coEvery { repository.getUsers() } throws Exception("Repository error")

        // WHEN
        useCase()
    }
}
