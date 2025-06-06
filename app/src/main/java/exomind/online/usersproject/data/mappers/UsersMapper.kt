package exomind.online.usersproject.data.mappers

import exomind.online.usersproject.data.network.UserDto
import exomind.online.usersproject.domain.models.User
import javax.inject.Inject

class UsersMapper @Inject constructor() {

    fun dtoToDomain(dto: UserDto): User {
        return User(
            id = dto.id,
            name = dto.name,
            email = dto.email,
            gender = dto.gender
        )
    }

}
