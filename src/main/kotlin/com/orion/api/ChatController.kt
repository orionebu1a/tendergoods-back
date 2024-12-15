package com.orion.api

import com.orion.entity.User
import com.orion.model.MessageForm
import com.orion.service.ChatService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import com.orion.errors.respondWithErrorProcessing

fun Route.chatRouting(chatService: ChatService) {
    route("/chats") {
        post("sendMessage") {
            val messageForm = call.receive<MessageForm>()
            val user = call.principal<User>()
            if (user == null) {
                call.respond(HttpStatusCode.Unauthorized, "com.orion.entity.User not authenticated")
            }
            val result = chatService.sendMessageTo(messageForm.bidId, messageForm.receiverId, user!!, messageForm.text)
            call.respondWithErrorProcessing(result)
        }
        get("allUserChats") {
            val user = call.principal<User>()
            if (user == null) {
                call.respond(HttpStatusCode.Unauthorized, "com.orion.entity.User not authenticated")
            }
            val result = chatService.getAllChatsWithLastMessage(user!!)
            call.respondWithErrorProcessing(result)
        }
        get("{id}") {
            val id = call.parameters["id"]!!.toInt()
            val user = call.principal<User>()
            if (user == null) {
                call.respond(HttpStatusCode.Unauthorized, "com.orion.entity.User not authenticated")
            }
            val result = chatService.getChatHistory(user!!, id)
            call.respondWithErrorProcessing(result)
        }
    }
}