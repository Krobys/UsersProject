package exomind.online.usersproject.domain

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.Test

class DeleteUserUseCaseTest {

    private val repository = mockk<UsersRepository>()
    private val useCase = DeleteUserUseCase(repository)

    @Test
    fun `invoke calls repository removeUser when repository succeeds`() = runTest {
        // GIVEN
        val userId = 7
        coEvery { repository.removeUser(userId) } returns Unit

        // WHEN
        useCase(userId)

        // THEN
        coVerify { repository.removeUser(userId) }
    }

    @Test
    fun `invoke returns result failure when repository fails`() = runTest {
        // GIVEN
        val userId = 7
        coEvery { repository.removeUser(userId) } throws Exception("Repository error")

        // WHEN
        val result = useCase.invoke(userId)

        // THEN
        assertTrue(result.isFailure)
    }
}
