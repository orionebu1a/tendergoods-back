package com.orion.table

import UserTable
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.timestamp

object ReviewTable : IntIdTable("reviews") {
    val reviewer = reference("reviewer_id", UserTable)
    val reviewed = reference("reviewed_id", UserTable)
    val rating = double("rating")
    val reviewText = text("review_text").nullable()
    val createdAt = timestamp("created_at")
}