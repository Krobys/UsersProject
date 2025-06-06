package exomind.online.usersproject.data.mappers

import exomind.online.usersproject.data.network.UserDto
import org.junit.Assert.assertEquals
import org.junit.Test

class UsersMapperTest {

    private val mapper = UsersMapper()

    @Test
    fun `dtoToDomain maps all fields correctly`() {
        // GIVEN
        val dto = UserDto(
            id = 42,
            name = "Jane Doe",
            email = "jane.doe@example.com",
            gender = "female"
        )

        // WHEN
        val domain = mapper.dtoToDomain(dto)

        // THEN
        assertEquals(42, domain.id)
        assertEquals("Jane Doe", domain.name)
        assertEquals("jane.doe@example.com", domain.email)
        assertEquals("female", domain.gender)
    }
}
