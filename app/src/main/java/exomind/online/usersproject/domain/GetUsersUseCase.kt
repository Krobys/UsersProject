package exomind.online.usersproject.domain

import exomind.online.usersproject.domain.models.User
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUsersUseCase @Inject constructor(
    private val repository: UsersRepository
) {
    suspend operator fun invoke(): Flow<List<User>> {
        return repository.getUsers()
    }
}