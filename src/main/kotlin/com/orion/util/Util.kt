package com.orion.util

import com.orion.entity.User
import io.ktor.server.application.*
import io.ktor.server.auth.*

val ApplicationCall.user get() = authentication.principal<User>()