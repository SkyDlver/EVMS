package team.mediagroup

import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.routing.*
import team.mediagroup.routes.apiRoutes
import team.mediagroup.routes.authRoutes
import team.mediagroup.routes.adminRoutes
import team.mediagroup.routes.employeeRoutes
import team.mediagroup.services.AuthService
import team.mediagroup.services.AdminService

fun Application.configureRouting(authService: AuthService, adminService: AdminService,employeeService: team.mediagroup.services.EmployeeService) {
    routing {
        // Public routes
        apiRoutes()

        // Auth (login/register)
        authRoutes(authService)

        // Protected admin routes
        adminRoutes(adminService)

        // Employee routes (requires EmployeeService)
        employeeRoutes(employeeService)

        // Static files
        staticResources("/", "static") {
            default("index.html")
        }
    }
}
