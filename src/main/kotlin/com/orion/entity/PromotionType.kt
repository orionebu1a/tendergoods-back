import com.orion.table.PromotionTypesTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class PromotionType(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<PromotionType>(PromotionTypesTable)

    var name by PromotionTypesTable.name
    var promotionClass by PromotionTypesTable.promotionClass
    var promotionPlus by PromotionTypesTable.promotionPlus
    var durationDays by PromotionTypesTable.durationDays
    var price by PromotionTypesTable.price
}