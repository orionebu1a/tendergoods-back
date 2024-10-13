import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.javatime.timestamp
import java.time.Instant

object Users : IntIdTable("users") {
    val email = varchar("email", 255).uniqueIndex()
    val passwordHash = varchar("password_hash", 255)
    val firstName = varchar("first_name", 100)
    val lastName = varchar("last_name", 100)
    val age = integer("age").nullable()
    val gender = varchar("gender", 10).nullable()
    val rating = double("rating").default(0.0)
    val walletBalance = double("wallet_balance").default(0.0)
    val createdAt = timestamp("created_at").default(Instant.now())
    val updatedAt = timestamp("updated_at").default(Instant.now())
}


@Serializable
data class User(
    val id: Int,
    val email: String,
    val passwordHash: String,
    val firstName: String,
    val lastName: String,
    val age: Int?,
    val gender: String?,
    val rating: Double,
    val walletBalance: Double,
    val createdAt: Instant,
    val updatedAt: Instant
)

fun ResultRow.toUser(): User = User(
    id = this[Users.id].value,
    email = this[Users.email],
    passwordHash = this[Users.passwordHash],
    firstName = this[Users.firstName],
    lastName = this[Users.lastName],
    age = this[Users.age],
    gender = this[Users.gender],
    rating = this[Users.rating].toDouble(),
    walletBalance = this[Users.walletBalance].toDouble(),
    createdAt = this[Users.createdAt],
    updatedAt = this[Users.updatedAt]
)