package com.orion.entity

import com.orion.table.MoneyTransactionTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class MoneyTransaction(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<MoneyTransaction>(MoneyTransactionTable)
    var receiver by MoneyTransactionTable.receiver
    var sender by MoneyTransactionTable.sender
    var transactionType by MoneyTransactionTable.transactionType
    var money by MoneyTransactionTable.money
    var time by MoneyTransactionTable.time
}