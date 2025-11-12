package team.mediagroup.routes

import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import team.mediagroup.dto.HolidayRequest
import team.mediagroup.models.UserPrincipal
import team.mediagroup.services.HolidayService

fun Route.holidayRoutes(holidayService: HolidayService) {
    authenticate("auth-jwt") {
        route("/api/holidays") {
            post {
                val principal = call.principal<UserPrincipal>()!!
                val request = call.receive<HolidayRequest>()

                val success = holidayService.addHoliday(principal, request)
                if (success) call.respondText("Holiday added")
                else call.respondText("Not allowed or violates 10-month rule", status = io.ktor.http.HttpStatusCode.Forbidden)
            }
        }
    }
}
