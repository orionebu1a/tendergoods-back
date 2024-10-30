package com.orion.converter

import com.orion.entity.Message
import com.orion.model.MessageDto

fun Message.toDto(): MessageDto {
    return MessageDto(
        text = text,
        createdAt = createdAt,
        sender = sender.value,
    )
}