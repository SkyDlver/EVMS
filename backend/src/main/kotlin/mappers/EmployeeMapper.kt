package team.mediagroup.mappers

import team.mediagroup.dto.EmployeeResponse
import team.mediagroup.models.Employee
import java.time.format.DateTimeFormatter

fun Employee.toResponse(
    formatter: DateTimeFormatter = DateTimeFormatter.ISO_DATE
): EmployeeResponse = EmployeeResponse(
    id = id.value,
    firstName = firstName,
    lastName = lastName,
    middleName = middleName,
    departmentId = department.id.value,
    roleInCompany = roleInCompany,
    hiredAt = hiredAt.format(formatter),
    isOnHoliday = isOnHoliday
)
