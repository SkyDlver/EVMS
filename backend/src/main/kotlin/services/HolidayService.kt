package team.mediagroup.services

import team.mediagroup.models.*
import org.jetbrains.exposed.sql.transactions.transaction
import team.mediagroup.dto.HolidayRequest

class HolidayService {

    // Add holiday with a 10-month restriction and HR override
    fun addHoliday(user: UserPrincipal, request: HolidayRequest): Boolean = transaction {
        val employee = Employee.findById(request.employeeId) ?: return@transaction false

        // Permission check
        if (user.role != Role.ADMIN && (user.role != Role.HR || user.departmentId != employee.department.id.value)) {
            return@transaction false
        }

        // Get last holiday end date
        val lastHolidayEnd = HolidayHistory.find { HolidayHistories.employee eq employee.id }
            .orderBy(HolidayHistories.end to org.jetbrains.exposed.sql.SortOrder.DESC)
            .limit(1)
            .firstOrNull()?.end

        val minNextHoliday = lastHolidayEnd?.plusMonths(10) ?: employee.hiredAt.plusMonths(10)

        // Check the 10-month rule unless overridden
        if (!request.override10MonthRule && request.start < minNextHoliday) return@transaction false

        // Create a new holiday entry
        HolidayHistory.new {
            this.employee = employee
            this.start = request.start
            this.end = request.end
            this.createdByHr = User.findById(user.id)!!
        }

        true
    }


}
