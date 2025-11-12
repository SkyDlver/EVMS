package team.mediagroup.models

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.javatime.date

// Table definition
object HolidayHistories : IntIdTable() {
    val employee   = reference("employee_id", Employees)
    val start      = date("start")
    val end        = date("end")
    val createdByHr= reference("created_by_hr", Users)
}

// Entity class
class HolidayHistory(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<HolidayHistory>(HolidayHistories)

    var employee by Employee referencedOn HolidayHistories.employee
    var start by HolidayHistories.start
    var end by HolidayHistories.end
    var createdByHr by User referencedOn HolidayHistories.createdByHr
}
