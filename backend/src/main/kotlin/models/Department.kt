package team.mediagroup.models

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

object Departments : IntIdTable() {
    val name = varchar("name", 100).uniqueIndex()
}

class Department(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Department>(Departments)
    var name by Departments.name
}
