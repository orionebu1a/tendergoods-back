package com.orion.api

import User
import com.orion.model.MessageForm
import com.orion.service.ChatService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import respondWithError

fun Route.chatRouting(chatService: ChatService) {
    route("/chat") {
        post("sendMessage") {
            val messageForm = call.receive<MessageForm>()
            val principal = call.principal<User>()
            if (principal == null) {
                call.respond(HttpStatusCode.Unauthorized, "User not authenticated")
            }
            val result = chatService.sendMessageTo(messageForm.bidId, messageForm.receiverId, principal!!, messageForm.text)
            call.respondWithError(result)
        }
        get("allChats") {
            val principal = call.principal<User>()
            if (principal == null) {
                call.respond(HttpStatusCode.Unauthorized, "User not authenticated")
            }
            val result = chatService.getAllChatsWithLastMessage(principal!!)
            call.respondWithError(result)
        }
    }
}