package exomind.online.usersproject.data

import exomind.online.usersproject.data.mappers.UsersMapper
import exomind.online.usersproject.data.network.UsersApi
import exomind.online.usersproject.domain.UsersRepository
import exomind.online.usersproject.domain.models.AddUser
import exomind.online.usersproject.domain.models.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UsersRepositoryImpl @Inject constructor(
    private val api: UsersApi,
    private val mapper: UsersMapper
): UsersRepository {
    override suspend fun getUsers(): Flow<List<User>> {
        return api.getUsers()
            .map {
                it.map(mapper::dtoToDomain)
            }
    }

    override suspend fun addUser(addUser: AddUser) {
        api.addUser(addUser)
    }

    override suspend fun removeUser(id: Int) {
        api.deleteUser(id)
    }
}
