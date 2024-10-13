package com.orion

import User
import UserRepository
import com.google.gson.GsonBuilder
import com.orion.api.userRouting
import com.orion.security.JwtConfig
import com.orion.security.JwtConfig.verifier
import com.orion.security.PasswordService
import com.orion.serializer.InstantSerializer
import com.orion.service.UserService
import io.ktor.http.*
import io.ktor.serialization.gson.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.Database
import java.time.Instant


fun main(args: Array<String>) {
    EngineMain.main(args)
}

fun Application.module() {
    val config = environment.config

    val dbUrl = environment.config.property("ktor.database.url").getString()
    val dbUser = config.property("ktor.database.user").getString()
    val dbPassword = config.property("ktor.database.password").getString()

    val flyway = Flyway.configure()
        .dataSource(dbUrl, dbUser, dbPassword)
        .load()

    flyway.migrate()

    Database.connect(
        url = dbUrl,
        driver = "org.postgresql.Driver",
        user = dbUser,
        password = dbPassword
    )

    val userService = UserService(UserRepository())

    install(CallLogging)
    install(ContentNegotiation) {
        gson {
            registerTypeAdapter(Instant::class.java, InstantSerializer())
        }
    }
    install(Authentication) {
        /**
         * Setup the JWT authentication to be used in [Routing].
         * If the token is valid, the corresponding [User] is fetched from the database.
         * The [User] can then be accessed in each [ApplicationCall].
         */
        jwt {
            verifier(verifier)
            realm = "ktor.io"
            validate {
                it.payload.getClaim("id").asInt()?.let(userService::getUserById)
            }
        }
    }

    install(Routing) {
        /**
         * A public login [Route] used to obtain JWTs
         */
        post("login") {
            val credentials = call.receive<UserPasswordCredential>()
            val user = userService.findByLogin(credentials.name)
            if (user != null) {
                call.respondText(JwtConfig.makeToken(user))
            } else {
                call.respond(HttpStatusCode.BadRequest)
            }
        }

        post("register") {
            val credentials = call.receive<UserPasswordCredential>()
            userService.registerUser(
                User(
                    email = credentials.name,
                    passwordHash = PasswordService.hashPassword(credentials.password),
                    createdAt = Instant.now(),
                    updatedAt = Instant.now(),
                    walletBalance = 0.0
                )
            )
            call.respond(HttpStatusCode.OK)
        }

        /**
         * All [Route]s in the authentication block are secured.
         */
        authenticate {
            userRouting(userService)
        }
    }
}