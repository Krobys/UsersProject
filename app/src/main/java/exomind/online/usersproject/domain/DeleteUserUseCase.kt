package exomind.online.usersproject.domain

import javax.inject.Inject

class DeleteUserUseCase @Inject constructor(
    private val repository: UsersRepository
) {
    suspend operator fun invoke(userId: Int) {
        repository.removeUser(userId)
    }
}