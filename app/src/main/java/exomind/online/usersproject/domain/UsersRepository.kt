package exomind.online.usersproject.domain

import exomind.online.usersproject.domain.models.AddUser
import exomind.online.usersproject.domain.models.User
import kotlinx.coroutines.flow.Flow

interface UsersRepository {
    suspend fun getUsers(): Flow<List<User>>
    suspend fun addUser(addUser: AddUser)
    suspend fun removeUser(id: Int)
}