package com.orion.util

import User
import io.ktor.server.application.*
import io.ktor.server.auth.*

val ApplicationCall.user get() = authentication.principal<User>()