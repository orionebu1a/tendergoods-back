package user;

import com.orion.model.LoginForm
import com.orion.model.UserDto
import com.orion.module
import com.orion.security.PasswordService
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Test
import IntegrationTest
import java.time.Instant
import kotlin.test.assertEquals

class UserTest : IntegrationTest() {
    @Test
    fun simpleUserTest() = testApplication {
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }
        val passwordService = PasswordService
        val password = "password"
        val user = UserDto(
            id = 1,
            email = "mcs@gmail.com",
            passwordHash = passwordService.hashPassword(password),
            firstName = "Alexey",
            lastName = "Petrov",
            age = 20,
            gender = "male",
            rating = 4.0,
            walletBalance = 1000.0,
            createdAt = Instant.now(),
            updatedAt = Instant.now(),
        )

        // Отправляем запрос на логин
        val loginResponse = client.post("/login") {
            contentType(ContentType.Application.Json)
            setBody(
                LoginForm(
                    "mcs@gmail.com",
                    password,
                )
            )
        }

        // Проверяем, что логин прошел успешно и получили JWT токен
        assertEquals(HttpStatusCode.OK, loginResponse.status)
        val token = loginResponse.bodyAsText()

        val response = client.get("/users/1") {
            contentType(ContentType.Application.Json)
            bearerAuth(token)
        }
        val responseBody = response.bodyAsText()

        val userById = Json.decodeFromString<UserDto>(responseBody)
        assertEquals(user.email, userById.email)
        assertEquals(user.firstName, userById.firstName)
        assertEquals(user.lastName, userById.lastName)
        assertEquals(user.age, userById.age)
        assertEquals(user.gender, userById.gender)
        assertEquals(user.rating, userById.rating)
        assertEquals(user.walletBalance, userById.walletBalance)
    }
}