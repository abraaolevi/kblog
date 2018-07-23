package http

import core.Injector
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.post
import io.ktor.routing.route
import org.kodein.di.generic.instance
import service.AuthService

data class LoginRequest(val email: String, val password: String)

fun Route.auth() {
    route("auth") {

        val authService by Injector.get.instance<AuthService>()

        post("/") {
            val data = call.receive<LoginRequest>()
            val response = authService.authenticate(data)
            call.respond(HttpStatusCode.fromValue(response["code"] as Int), response)
        }
    }
}