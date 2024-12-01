package com.orion.service

import com.orion.entity.User
import com.orion.converter.toDto
import com.orion.entity.Bid
import com.orion.entity.Message
import com.orion.errors.ResultWithError
import com.orion.errors.ServiceError
import com.orion.model.ChatsDto
import com.orion.model.MessageDto
import com.orion.table.MessageTable
import org.jetbrains.exposed.sql.or
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.Instant

class ChatService {
    fun sendMessageTo(bidId: Int, userId: Int, user: User, textToSend: String) : ResultWithError<Unit> = transaction {
        val bidToBind = Bid.findById(bidId) ?: return@transaction ResultWithError.Failure(ServiceError.Custom("Bid not found"))
        if (bidToBind.user.id != user.id &&
            bidToBind.lastUserBet != user.id) {
            return@transaction ResultWithError.Failure(ServiceError.Custom("Chat without buying or selling impossible"))
        }
        if (bidToBind.endTime > Instant.now()) {
            return@transaction ResultWithError.Failure(ServiceError.Custom("Bid still active"))
        }
        val reciever = User.findById(userId) ?: return@transaction ResultWithError.Failure(ServiceError.Custom("Reciever not found not found"))
        Message.new {
            sender = user
            receiver = reciever
            bid = bidToBind.id
            text = textToSend
            createdAt = Instant.now()
        }
        return@transaction ResultWithError.Success(Unit)
    }

    fun getAllChatsWithLastMessage(user: User) : ResultWithError<ChatsDto> = transaction {
        return@transaction ResultWithError.Success(ChatsDto(Message.find {
            (MessageTable.sender eq user.id) or (MessageTable.receiver eq user.id)
        }.groupBy { it.bid.value }.mapValues { entry ->
            entry.value.maxByOrNull { it.createdAt }
        }.map { it.key to it.value?.text }))
    }

    fun getChatHistory(user: User, bidId: Int) : ResultWithError<List<MessageDto>> = transaction {
        val bid = Bid.findById(bidId)
        if (bid?.user?.id != user.id &&
            bid?.lastUserBet != user.id) {
            return@transaction ResultWithError.Failure(ServiceError.Custom("Chat without buying or selling impossible"))
        }
        ResultWithError.Success(Message.find { MessageTable.bid eq bidId }
            .toList()
            .sortedByDescending { it.createdAt }
            .map { it.toDto() }
        )
    }
}