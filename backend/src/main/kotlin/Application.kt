package team.mediagroup

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.event.Level
import team.mediagroup.models.Departments
import team.mediagroup.models.Employees
import team.mediagroup.models.HolidayHistories
import team.mediagroup.models.Role
import team.mediagroup.models.UserPrincipal
import team.mediagroup.models.Users
import team.mediagroup.services.AdminService
import team.mediagroup.services.AuthService
import team.mediagroup.services.EmployeeService
import io.ktor.server.plugins.cors.routing.*
import io.ktor.http.*
import io.ktor.server.plugins.calllogging.CallLogging

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        module()
    }.start(wait = true)
}

fun Application.module() {
    install(ContentNegotiation) {
        json()
    }
    install(CallLogging) {
        level = Level.INFO // or Level.DEBUG for more detail
        filter { call -> true } // log all requests
    }

    install(CORS) {
        anyHost()  // for development only
        allowCredentials = true
        allowHeader(HttpHeaders.ContentType)
        allowHeader(HttpHeaders.Authorization)
        allowMethod(HttpMethod.Get)
        allowMethod(HttpMethod.Post)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Delete)
    }
    // Initialize database connection
    Database.connect(
        url = "jdbc:postgresql://localhost:5432/evms_db",
        driver = "org.postgresql.Driver",
        user = "postgres",
        password = "1234"
    )

    transaction {
        println("Creating tables...")
        SchemaUtils.create(
            Departments,
            Users,
            Employees,
            HolidayHistories
        )
    }
    install(io.ktor.server.plugins.statuspages.StatusPages) {
        exception<Throwable> { call, cause ->
            cause.printStackTrace()
            call.respondText("Server error: ${cause.message}", status = io.ktor.http.HttpStatusCode.InternalServerError)
        }
    }
    val jwtSecret = "super-secret-key"
    val authService = AuthService(jwtSecret)
    val adminService = AdminService(authService)
    val employeeService = EmployeeService()

    install(Authentication) {
        jwt("auth-jwt") {
            realm = "evms-app"
            verifier(authService.getVerifier())
            validate { credential ->
                val userId = credential.payload.getClaim("userId").asInt()
                val roleStr = credential.payload.getClaim("role").asString()
                if (userId != null && roleStr != null) {
                    val deptId = transaction {
                        Users.select { Users.id eq userId }
                            .singleOrNull()?.get(Users.departmentId)
                    }
                    UserPrincipal(id = userId, role = Role.valueOf(roleStr), departmentId = deptId)
                } else null
            }
        }
    }

    configureRouting(authService, adminService, employeeService)
}
