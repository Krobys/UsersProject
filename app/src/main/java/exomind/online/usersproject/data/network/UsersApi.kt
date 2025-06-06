package exomind.online.usersproject.data.network

import com.apollographql.apollo.ApolloClient
import exomind.online.GetUsersQuery
import javax.inject.Inject

class UsersApi @Inject constructor(
    private val apolloClient: ApolloClient,
    private val mapper: NetworkMapper,
) {
    suspend fun getUsers(): List<UserDto> {
        val response = apolloClient
            .query(GetUsersQuery())
            .execute()

        response.exception?.let { throw it }

        val errors = response.errors
        if (!errors.isNullOrEmpty()) {
            throw Exception(errors.joinToString { it.message })
        }

        return response.data
            ?.users
            ?.nodes
            ?.filterNotNull()
            ?.map(mapper::nodeToDto)
            ?: emptyList()
    }
}
