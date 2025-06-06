package exomind.online.usersproject.domain

import exomind.online.usersproject.domain.models.AddUser
import exomind.online.usersproject.domain.models.User

interface UsersRepository {
    suspend fun getUsers(): List<User>
    suspend fun addUser(addUser: AddUser)
    suspend fun removeUser(id: Int)
}