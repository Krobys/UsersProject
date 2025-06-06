package exomind.online.usersproject.data

import exomind.online.usersproject.data.mappers.UsersMapper
import exomind.online.usersproject.data.network.UserDto
import exomind.online.usersproject.data.network.UsersApi
import exomind.online.usersproject.domain.models.AddUser
import exomind.online.usersproject.domain.models.User
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class UsersRepositoryImplTest {

    private val api = mockk<UsersApi>()
    private val mapper = mockk<UsersMapper>()
    private val repository = UsersRepositoryImpl(api, mapper)

    @Test
    fun `getUsers returns mapped domain users`() = runTest {
        // GIVEN
        val dto1 = UserDto(id = 1, name = "Alice", email = "alice@test", gender = "female")
        val dto2 = UserDto(id = 2, name = "Bob", email = "bob@test", gender = "male")
        coEvery { api.getUsers() } returns listOf(dto1, dto2)
        val domain1 = User(id = 1, name = "Alice", email = "alice@test", gender = "female")
        val domain2 = User(id = 2, name = "Bob", email = "bob@test", gender = "male")
        coEvery { mapper.dtoToDomain(dto1) } returns domain1
        coEvery { mapper.dtoToDomain(dto2) } returns domain2

        // WHEN
        val result = repository.getUsers()

        // THEN
        assertEquals(listOf(domain1, domain2), result)
        coVerify(exactly = 1) { api.getUsers() }
        coVerify(exactly = 1) { mapper.dtoToDomain(dto1) }
        coVerify(exactly = 1) { mapper.dtoToDomain(dto2) }
    }

    @Test
    fun `addUser calls api_addUser with correct argument`() = runTest {
        // GIVEN
        val addUser = AddUser(name = "Charlie", email = "charlie@test", gender = "male")
        coJustRun { api.addUser(addUser) }

        // WHEN
        repository.addUser(addUser)

        // THEN
        coVerify(exactly = 1) { api.addUser(addUser) }
    }

    @Test
    fun `removeUser calls api_deleteUser with correct id`() = runTest {
        // GIVEN
        val userId = 5
        coJustRun { api.deleteUser(userId) }

        // WHEN
        repository.removeUser(userId)

        // THEN
        coVerify(exactly = 1) { api.deleteUser(userId) }
    }
}
