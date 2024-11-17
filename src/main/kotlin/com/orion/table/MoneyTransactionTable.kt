package com.orion.table

import UserTable
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.CurrentTimestamp
import org.jetbrains.exposed.sql.javatime.timestamp

object MoneyTransactionTable : IntIdTable("money_transactions") {
    val sender = reference("sender_id", UserTable)
    val receiver = reference("receiver_id", UserTable)
    val money = double("money")
    val transactionType = text("transactionType")
    val time = timestamp("time").defaultExpression(CurrentTimestamp())
}