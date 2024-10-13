import io.ktor.server.auth.*
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.javatime.timestamp
import java.time.Instant

@Serializable
data class User(
    val id: Int? = null,
    val email: String,
    val passwordHash: String,
    val firstName: String? = null,
    val lastName: String? = null,
    val age: Int? = null,
    val gender: String? = null,
    val rating: Double? = null,
    val walletBalance: Double,
    val createdAt: Instant,
    val updatedAt: Instant
): Principal