import com.orion.entity.User
import com.orion.model.LoginForm
import com.orion.security.PasswordService
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.config.*
import io.ktor.server.testing.*
import junit.framework.TestCase.assertEquals
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.BeforeAll
import java.time.Instant

open class IntegrationTest {
    companion object {
        var user1: User? = null
        var user2: User? = null
        var user3: User? = null
        var user4: User? = null

        @JvmStatic
        @BeforeAll
        fun setupDatabase() = testApplication {
            System.setProperty("KTOR_ENV", "test")
            application {
                val passwordService = PasswordService
                val config = environment.config
                val dbUser = config.property("ktor.database.user").getString()
                val dbPassword = config.property("ktor.database.password").getString()
                val dataSource = org.h2.jdbcx.JdbcDataSource().apply {
                    setURL("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;")
                    user = dbUser
                    password = dbPassword
                }

                Flyway.configure()
                    .cleanDisabled(false)
                    .dataSource(dataSource)
                    .load()
                    .clean()

                Flyway.configure()
                    .dataSource(dataSource)
                    .load()
                    .migrate()

                Database.connect(dataSource)

                transaction {
                    val password = "password"
                    user1 = User.new {
                        email = "mcs@gmail.com"
                        passwordHash = passwordService.hashPassword(password)
                        firstName = "Alexey"
                        lastName = "Petrov"
                        age = 20
                        gender = "male"
                        rating = 4.0
                        walletBalance = 1000.0
                        createdAt = Instant.now()
                        updatedAt = Instant.now()
                    }

                    user2 = User.new {
                        email = "ivan@gmail.com"
                        passwordHash = passwordService.hashPassword(password)
                        firstName = "Ivan"
                        lastName = "Ivanov"
                        age = 30
                        gender = "male"
                        rating = 4.0
                        walletBalance = 10.0
                        createdAt = Instant.now()
                        updatedAt = Instant.now()
                    }

                    user3 = User.new {
                        email = "daria@gmail.com"
                        passwordHash = passwordService.hashPassword(password)
                        firstName = "Daria"
                        lastName = "Petrova"
                        age = 40
                        gender = "female"
                        rating = 4.0
                        walletBalance = 21.0
                        createdAt = Instant.now()
                        updatedAt = Instant.now()
                    }

                    user4 = User.new {
                        email = "marina@gmail.com"
                        passwordHash = passwordService.hashPassword(password)
                        firstName = "Marina"
                        lastName = "Petrova"
                        age = 20
                        gender = "female"
                        rating = 4.0
                        walletBalance = 21.0
                        createdAt = Instant.now()
                        updatedAt = Instant.now()
                    }
                }
            }
        }
    }

    suspend fun ApplicationTestBuilder.tokenLoginHelper(): List<String> {
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }
        val password1 = "password"
        val email1 = "mcs@gmail.com"
        val loginResponse1 = client.post("/login") {
            contentType(ContentType.Application.Json)
            setBody(
                LoginForm(
                    email = email1,
                    password = password1
                )
            )
        }
        assertEquals(HttpStatusCode.OK, loginResponse1.status)
        val token1 = loginResponse1.bodyAsText()

        val password2 = "password"
        val email2 = "ivan@gmail.com"
        val loginResponse2 = client.post("/login") {
            contentType(ContentType.Application.Json)
            setBody(
                LoginForm(
                    email = email2,
                    password = password2
                )
            )
        }
        assertEquals(HttpStatusCode.OK, loginResponse2.status)
        val token2 = loginResponse2.bodyAsText()

        val password3 = "password"
        val email3 = "daria@gmail.com"
        val loginResponse3 = client.post("/login") {
            contentType(ContentType.Application.Json)
            setBody(
                LoginForm(
                    email = email3,
                    password = password3
                )
            )
        }
        assertEquals(HttpStatusCode.OK, loginResponse3.status)
        val token3 = loginResponse3.bodyAsText()

        val password4 = "password"
        val email4 = "marina@gmail.com"
        val loginResponse4 = client.post("/login") {
            contentType(ContentType.Application.Json)
            setBody(
                LoginForm(
                    email = email4,
                    password = password4
                )
            )
        }
        assertEquals(HttpStatusCode.OK, loginResponse3.status)
        val token4 = loginResponse4.bodyAsText()

        return listOf(token1, token2, token3, token4)
    }

}