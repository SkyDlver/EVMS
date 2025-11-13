package team.mediagroup

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.http.*
import io.ktor.server.plugins.calllogging.CallLogging
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.event.Level
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger
import org.koin.ktor.ext.inject
import team.mediagroup.database.DatabaseFactory
import team.mediagroup.di.appModule
import team.mediagroup.models.*
import team.mediagroup.services.AuthService
import team.mediagroup.services.AdminService
import team.mediagroup.services.EmployeeService

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        module()
    }.start(wait = true)
}

fun Application.module() {

    // Initialize database
    DatabaseFactory.init()

    // Install Koin for DI
    install(Koin) {
        slf4jLogger()
        modules(appModule)
    }

    // Inject services via Koin
    val authService: AuthService by inject()
    val adminService: AdminService by inject()
    val employeeService: EmployeeService by inject()

    // Install standard Ktor features
    install(ContentNegotiation) { json() }
    install(CallLogging) {
        level = Level.INFO
        filter { true }
    }
    install(CORS) {
        anyHost()
        allowCredentials = true
        allowHeader(HttpHeaders.ContentType)
        allowHeader(HttpHeaders.Authorization)
        allowMethod(HttpMethod.Get)
        allowMethod(HttpMethod.Post)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Delete)
    }

    // Create tables if they don't exist
    transaction {
        println("Creating tables...")
        SchemaUtils.create(
            Departments,
            Users,
            Employees,
            HolidayHistories
        )
    }

    // Global error handling
    install(io.ktor.server.plugins.statuspages.StatusPages) {
        exception<Throwable> { call, cause ->
            cause.printStackTrace()
            call.respondText("Server error: ${cause.message}", status = io.ktor.http.HttpStatusCode.InternalServerError)
        }
    }

    // JWT authentication
    install(Authentication) {
        jwt("auth-jwt") {
            realm = "evms-app"
            verifier(authService.getVerifier())
            validate { credential ->
                val userId = credential.payload.getClaim("userId").asInt()
                val roleStr = credential.payload.getClaim("role").asString()
                if (userId != null && roleStr != null) {
                    val userData = transaction {
                        Users.select { Users.id eq userId }
                            .singleOrNull()
                    }

                    if (userData != null) {
                        val deptId = userData[Users.departmentId]
                        val username = userData[Users.username]
                        UserPrincipal(
                            id = userId,
                            role = Role.valueOf(roleStr),
                            departmentId = deptId,
                            username = username
                        )
                    } else null
                } else null
            }
        }
    }

    // Configure routing with injected services
    configureRouting(authService, adminService, employeeService)
}
