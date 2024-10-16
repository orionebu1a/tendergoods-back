package com.orion.api

import com.orion.converter.toDto
import com.orion.form.BidDto
import com.orion.form.ItemDto
import com.orion.form.UserDto
import com.orion.service.ItemService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.itemRouting(itemService: ItemService) {
    route("/items") {
        get {
            val items = itemService.getAllItems()
            call.respond(items)
        }

        get("{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id != null) {
                val item = itemService.getItemById(id)
                if (item != null) {
                    call.respond(item)
                } else {
                    call.respond(HttpStatusCode.NotFound)
                }
            } else {
                call.respond(HttpStatusCode.BadRequest, "Invalid item ID")
            }
        }

        post() {
            val item = call.receive<ItemDto>()
            val createdItem = itemService.createItem(item)
            call.respond(HttpStatusCode.Created, createdItem)
        }

        put("{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            val item = call.receive<ItemDto>()
            if (id != null) {
                val updatedItem = itemService.updateItem(id, item)
                call.respond(updatedItem)
            } else {
                call.respond(HttpStatusCode.BadRequest, "Invalid item ID")
            }
        }

        delete("{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id != null) {
                if (itemService.deleteItem(id)) {
                    call.respond(HttpStatusCode.NoContent)
                } else {
                    call.respond(HttpStatusCode.NotFound)
                }
            } else {
                call.respond(HttpStatusCode.BadRequest, "Invalid item ID")
            }
        }
    }
}