package com.orion

import UserRepository
import com.orion.api.userRouting
import com.orion.plugins.*
import com.orion.service.UserService
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.plugins.contentnegotiation.*
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.Database


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

    install(ContentNegotiation) {
        json()
    }

    configureSecurity()
    configureRouting()

    Database.connect(
        url = dbUrl,
        driver = "org.postgresql.Driver",
        user = dbUser,
        password = dbPassword
    )
    userRouting(userService = UserService(UserRepository()))
}