package exomind.online.usersproject.data.network

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.cache.normalized.FetchPolicy
import com.apollographql.apollo.cache.normalized.fetchPolicy
import exomind.online.CreateUserMutation
import exomind.online.DeleteUserMutation
import exomind.online.GetUsersQuery
import exomind.online.type.CreateUserInput
import exomind.online.usersproject.domain.models.AddUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UsersApi @Inject constructor(
    private val apolloClient: ApolloClient,
    private val mapper: NetworkMapper,
) {
    fun getUsers(): Flow<List<UserDto>> {
        return apolloClient
            .query(GetUsersQuery())
            .fetchPolicy(FetchPolicy.NetworkFirst)
            .toFlow()
            .map { response ->
                val errors = response.errors
                if (!errors.isNullOrEmpty()) {
                    throw Exception(errors.joinToString { it.message })
                }

                response.data
                    ?.users
                    ?.nodes
                    ?.filterNotNull()
                    ?.map(mapper::nodeToDto)
                    ?: emptyList()
            }
    }

    suspend fun addUser(addUser: AddUser) {
        val input = CreateUserInput(
            name = addUser.name,
            email = addUser.email,
            gender = addUser.gender,
            status = "active"
        )

        val response = apolloClient
            .mutation(CreateUserMutation(input))
            .execute()

        response.exception?.let { throw it }

        val errors = response.errors
        if (!errors.isNullOrEmpty()) {
            throw Exception(errors.joinToString { it.message })
        }
    }

    suspend fun deleteUser(id: Int) {
        val response = apolloClient
            .mutation(DeleteUserMutation(id = id))
            .execute()

        response.exception?.let { throw it }
        response.errors?.takeIf { it.isNotEmpty() }
            ?.let { throw Exception(it.joinToString { e -> e.message }) }
    }
}
