package exomind.online.usersproject.data.network

import exomind.online.GetUsersQuery
import javax.inject.Inject

class NetworkMapper @Inject constructor() {
    fun nodeToDto(node: GetUsersQuery.Node): UserDto {
        return UserDto(
            id = node.id,
            name = node.name,
            email = node.email,
            gender = node.gender
        )
    }
}
