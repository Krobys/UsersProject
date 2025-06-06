package exomind.online.usersproject.data

import exomind.online.usersproject.data.mappers.UsersMapper
import exomind.online.usersproject.data.network.UsersApi
import exomind.online.usersproject.domain.UsersRepository
import exomind.online.usersproject.domain.models.AddUser
import exomind.online.usersproject.domain.models.User
import javax.inject.Inject

class UsersRepositoryImpl @Inject constructor(
    private val api: UsersApi,
    private val mapper: UsersMapper
): UsersRepository {
    override suspend fun getUsers(): List<User> {
        return api.getUsers()
            .map(mapper::dtoToDomain)
    }

    override suspend fun addUser(addUser: AddUser) {
        api.addUser(addUser)
    }

    override suspend fun removeUser(id: Long) {
        TODO("Not yet implemented")
    }
}
