package com.orion.table

import org.jetbrains.exposed.dao.id.IntIdTable

object ItemCategoryTable : IntIdTable("items") {
    val name = varchar("name", 20)
}