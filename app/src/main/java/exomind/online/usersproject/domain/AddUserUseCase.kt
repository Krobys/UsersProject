package exomind.online.usersproject.domain

import exomind.online.usersproject.domain.models.AddUser
import javax.inject.Inject

class AddUserUseCase @Inject constructor(
    private val repository: UsersRepository,
) {
    suspend operator fun invoke(addUser: AddUser) {
        repository.addUser(addUser)
    }
}