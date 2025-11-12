package team.mediagroup.routes

import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route

fun Route.apiRoutes() {
    route("/api") {
        get("/hello") {
            call.respond(mapOf("message" to "Hello from backend!"))
        }
    }
}
