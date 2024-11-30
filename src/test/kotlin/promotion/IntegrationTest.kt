package promotion

import com.orion.security.PasswordService
import io.ktor.server.testing.*
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import java.time.Instant
import kotlin.test.assertEquals

open class IntegrationTest {
    companion object {
        @JvmStatic
        @BeforeAll
        fun setupDatabase() = testApplication {
            System.setProperty("ktor.test.enabled", "true")
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
                    val tables =
                        exec("SELECT table_name FROM information_schema.tables WHERE table_schema = 'PUBLIC'") { rs ->
                            generateSequence {
                                if (rs.next()) rs.getString("table_name") else null
                            }.toList()
                        }
                    println("Список таблиц в базе данных: $tables")

                    val password = "password"
                    User.new {
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

                    User.new {
                        email = "petr@gmail.com"
                        passwordHash = passwordService.hashPassword(password)
                        firstName = "Ivan"
                        lastName = "Ivanov"
                        age = 30
                        gender = "male"
                        rating = 5.0
                        walletBalance = 10.0
                        createdAt = Instant.now()
                        updatedAt = Instant.now()
                    }
                }
            }
        }
    }
}