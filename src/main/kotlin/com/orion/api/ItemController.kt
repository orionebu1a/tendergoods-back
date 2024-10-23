package com.orion.api

import User
import com.orion.model.ItemDto
import com.orion.model.ItemForm
import com.orion.service.ItemService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.itemRouting(itemService: ItemService) {
    route("/items") {
        get("{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id != null) {
                val item = itemService.findById(id)
                if (item != null) {
                    call.respond(item)
                } else {
                    call.respond(HttpStatusCode.NotFound)
                }
            } else {
                call.respond(HttpStatusCode.BadRequest, "Invalid item ID")
            }
        }

        get("/ownedByUser/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id != null) {
                val item = itemService.findAllUserItems(id)
                call.respond(item)
            } else {
                call.respond(HttpStatusCode.BadRequest, "Invalid user ID")
            }
        }

        post {
            val item = call.receive<ItemForm>()
            val principal = call.principal<User>()
            val createdItem = itemService.create(item, principal!!)
            call.respond(HttpStatusCode.Created, createdItem)
        }

        put("{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            val item = call.receive<ItemForm>()
            if (id != null) {
                val existing = itemService.findById(id)
                val principal = call.principal<User>()
                if (existing?.userId == principal?.id?.value) {
                    val updatedItem = itemService.update(id, item)
                    call.respond(updatedItem)
                }
            } else {
                call.respond(HttpStatusCode.BadRequest, "Invalid item ID")
            }
        }

        delete("{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id != null) {
                val existing = itemService.findById(id)
                val principal = call.principal<User>()
                if (existing?.userId == principal?.id?.value) {
                    if (itemService.delete(id)) {
                        call.respond(HttpStatusCode.NoContent)
                    } else {
                        call.respond(HttpStatusCode.NotFound)
                    }
                }
            } else {
                call.respond(HttpStatusCode.BadRequest, "Invalid item ID")
            }
        }
    }
}