package http

import core.Injector
import io.ktor.application.call
import io.ktor.auth.authenticate
import io.ktor.auth.authentication
import io.ktor.auth.principal
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.*
import model.entity.User
import org.kodein.di.generic.instance

import service.UserService


fun Route.users() {

    val userService: UserService by Injector.get.instance()

    route("/users") {

        post("/") {
            val data = call.receive<User>()
            val response = userService.addUser(data)
            call.respond(HttpStatusCode.fromValue(response["code"] as Int), response)
        }

        authenticate("jwt") {

            get("/") {
                val response = userService.getAllUsers()
                call.respond(HttpStatusCode.fromValue(response["code"] as Int), response)
            }

            get("/{id}") {
                val id = call.parameters["id"]?.toInt()!!

                userService.loggedUser = call.authentication.principal()
                val response = userService.getUser(id)

                call.respond(HttpStatusCode.fromValue(response["code"] as Int), response)
            }

            put("/") {
                val data = call.receive<User>()
                val response = userService.updateUser(data)
                call.respond(HttpStatusCode.fromValue(response["code"] as Int), response)
            }

            delete("/{id}") {
                val id = call.parameters["id"]?.toInt()!!
                val response = userService.deleteUser(id)
                call.respond(HttpStatusCode.fromValue(response["code"] as Int), response)
            }
        }
    }

}