package com.orion

import com.orion.entity.User
import com.orion.api.*
import com.orion.model.LoginForm
import com.orion.model.UserForm
import com.orion.security.JwtConfig
import com.orion.security.JwtConfig.verifier
import com.orion.security.PasswordService
import com.orion.serializer.InstantSerializer
import com.orion.service.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.serializersModuleOf
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.Database
import java.time.Instant


fun main(args: Array<String>) {
    EngineMain.main(args)
}

fun Application.module() {
    val config = environment.config

    val dbUser = config.property("ktor.database.user").getString()
    val dbPassword = config.property("ktor.database.password").getString()

    val isTestEnvironment = config.property("ktor.test.enabled").getString().toBoolean()

    val dbUrl = if (isTestEnvironment) {
        config.property("ktor.test.database.url").getString()
    } else {
        config.property("ktor.database.url").getString()
    }

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

    val moneyTransactionService = InternalMoneyTransactionService()
    val actionService = InternalActionService()
    val adviceService = InternalAdviceService()
    val promotionService = PromotionService(moneyTransactionService)
    val userService = UserService()
    val itemService = ItemService(actionService)
    val bidService = BidService(actionService, adviceService, promotionService)
    val chatService = ChatService()
    val betService = BetService(actionService, moneyTransactionService)
    val reviewService = ReviewService()

    install(CallLogging)
    install(ContentNegotiation) {
        json(
            Json {
                serializersModule = serializersModuleOf(Instant::class, InstantSerializer)
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            }
        )
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
                it.payload.getClaim("id").asInt()?.let(userService::findPrincipalById)
            }
        }

    }

    install(Routing) {
        /**
         * A public login [Route] used to obtain JWTs
         */
        post("login") {
            val loginForm = call.receive<LoginForm>()
            val user = userService.findPrincipalByLogin(loginForm.email)
            if (user != null && PasswordService.checkPassword(loginForm.password, user.passwordHash)) {
                call.respondText(JwtConfig.makeToken(user))
            } else {
                call.respond(HttpStatusCode.BadRequest, "Invalid username or password")
            }
        }

        post("register") {
            val userForm = call.receive<UserForm>()
            userService.create(
                userForm
            )
            call.respond(HttpStatusCode.OK)
        }

        /**
         * All [Route]s in the authentication block are secured.
         */
        authenticate {
            userRouting(userService)
            bidRouting(bidService)
            itemRouting(itemService)
            betRouting(betService)
            chatRouting(chatService)
            reviewRouting(reviewService)
            promotionRouting(promotionService)
        }
    }
}