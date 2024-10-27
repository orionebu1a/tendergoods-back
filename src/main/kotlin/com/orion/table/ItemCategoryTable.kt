package com.orion.table

import org.jetbrains.exposed.dao.id.IntIdTable

object ItemCategoryTable : IntIdTable("item_category") {
    val name = varchar("name", 20)
}