package exomind.online.usersproject.data.network

import exomind.online.GetUsersQuery
import org.junit.Assert.assertEquals
import org.junit.Test

class NetworkMapperTest {

    private val mapper = NetworkMapper()

    @Test
    fun `nodeToDto maps all fields correctly`() {
        // GIVEN
        val node = GetUsersQuery.Node(
            id = 42,
            name = "Bob",
            email = "bob@example.test",
            gender = "male",
            status = "active"
        )

        // WHEN
        val dto = mapper.nodeToDto(node)

        // THEN
        assertEquals(42, dto.id)
        assertEquals("Bob", dto.name)
        assertEquals("bob@example.test", dto.email)
        assertEquals("male", dto.gender)
    }
}
