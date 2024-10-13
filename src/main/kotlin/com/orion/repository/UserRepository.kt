import com.orion.converter.fromUser
import com.orion.converter.toUser
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

class UserRepository {
    fun findAll(): List<User> = transaction {
        UserTable.selectAll().map { it.toUser() }
    }

    fun findById(id: Int): User? = transaction {
        UserTable.select { UserTable.id eq id }
            .mapNotNull { it.toUser() }
            .singleOrNull()
    }

    fun create(user: User): User = transaction {
        val id = UserTable.insertAndGetId {
            it.fromUser(user)
        }.value

        user.copy(id = id)
    }

    fun findByLogin(login: String): User? = transaction {
        UserTable.select { UserTable.email eq login }
            .mapNotNull { it.toUser() }
            .singleOrNull()
    }

    fun update(user: User): Boolean = transaction {
        UserTable.update({ UserTable.id eq user.id }) {
            it.fromUser(user)
        } > 0
    }

    fun delete(id: Int): Boolean = transaction {
        UserTable.deleteWhere { UserTable.id eq EntityID(id, UserTable) } > 0
    }

}