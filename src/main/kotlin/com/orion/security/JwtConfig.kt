package com.orion.security

import com.orion.entity.User
import com.auth0.jwt.*
import com.auth0.jwt.algorithms.*
import java.util.*

object JwtConfig {

    private const val secret = "my_secret_code"
    private const val issuer = "ktor.io"
    private const val validityInMs = 36_000_00 * 10 // 10 hours
    private val algorithm = Algorithm.HMAC512(secret)

    val verifier: JWTVerifier = JWT
            .require(algorithm)
            .withIssuer(issuer)
            .build()

    /**
     * Produce a token for this combination of com.orion.entity.User and Account
     */
    fun makeToken(user: User): String = JWT.create()
            .withSubject("Authentication")
            .withIssuer(issuer)
            .withClaim("id", user.id.value)
            .withExpiresAt(getExpiration())
            .sign(algorithm)

    /**
     * Calculate the expiration Date based on current time + the given validity
     */
    private fun getExpiration() = Date(System.currentTimeMillis() + validityInMs)
}