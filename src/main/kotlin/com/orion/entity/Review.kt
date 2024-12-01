package com.orion.entity

import com.orion.table.ReviewTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class Review(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Review>(ReviewTable)
    var reviewer by User referencedOn ReviewTable.reviewer
    var reviewed by User referencedOn ReviewTable.reviewed
    var rating by ReviewTable.rating
    var reviewText by ReviewTable.reviewText
    var createdAt by ReviewTable.createdAt
}