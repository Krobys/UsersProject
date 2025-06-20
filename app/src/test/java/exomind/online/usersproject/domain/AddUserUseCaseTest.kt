package exomind.online.usersproject.domain

import exomind.online.usersproject.domain.models.AddUser
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Test

class AddUserUseCaseTest {

    private val repository = mockk<UsersRepository>()
    private val useCase = AddUserUseCase(repository)

    @Test
    fun `invoke calls repository addUser when repository succeeds`() = runTest {
        // GIVEN
        val newUser = AddUser(name = "Mark", email = "mark@test", gender = "male")
        coEvery { repository.addUser(newUser) } returns Unit

        // WHEN
        useCase(newUser)

        // THEN
        coVerify { repository.addUser(newUser) }
    }

    @Test
    fun `invoke returns result fail when repository fails`() = runTest {
        // GIVEN
        val newUser = AddUser(name = "Mark", email = "mark@test", gender = "male")
        coEvery { repository.addUser(newUser) } throws Exception("Repository error")

        // WHEN
        val result = useCase.invoke(newUser)

        // THEN
        assertEquals(true, result.isFailure)
    }
}
