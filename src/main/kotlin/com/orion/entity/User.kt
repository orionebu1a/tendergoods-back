import com.orion.entity.Bid
import io.ktor.server.auth.*
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class User(id: EntityID<Int>) : IntEntity(id), Principal {
    companion object : IntEntityClass<User>(UserTable)

    var email by UserTable.email
    var passwordHash by UserTable.passwordHash
    var firstName by UserTable.firstName
    var lastName by UserTable.lastName
    var age by UserTable.age
    var gender by UserTable.gender
    var rating by UserTable.rating
    var walletBalance by UserTable.walletBalance
    var createdAt by UserTable.createdAt
    var updatedAt by UserTable.updatedAt
}