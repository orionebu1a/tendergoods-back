import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Table.Dual.nullable

import org.jetbrains.exposed.sql.javatime.timestamp

object UserTable : IntIdTable("users") {
    val email = varchar("email", 255).uniqueIndex()
    val passwordHash = varchar("password_hash", 255)
    val firstName = varchar("first_name", 100).nullable()
    val lastName = varchar("last_name", 100).nullable()
    val age = integer("age").nullable()
    val gender = varchar("gender", 10).nullable()
    val rating = double("rating").default(0.0).nullable()
    val walletBalance = double("wallet_balance").default(0.0)
    val createdAt = timestamp("created_at")
    val updatedAt = timestamp("updated_at")
}