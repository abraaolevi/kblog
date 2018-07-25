package server

import com.auth0.jwt.JWTVerifier
import core.Injector
import core.database.Connection
import http.auth
import http.users
import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.auth.Authentication
import io.ktor.auth.jwt.jwt
import io.ktor.features.*
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.jackson.jackson
import io.ktor.routing.Routing
import org.kodein.di.generic.instance
import repository.UserRepository
import utility.Jwt


fun authVerifier(): JWTVerifier {
    val jwt: Jwt by Injector.get.instance()
    return jwt.verifier
}

fun connectDB() {
    val connection by Injector.get.instance<Connection>()
    connection.connect()
    connection.prepareTables()
}

fun Application.module() {
    install(Compression)
    install(AutoHeadResponse)
    install(CallLogging)
    install(CORS) {
        method(HttpMethod.Options)
        method(HttpMethod.Put)
        method(HttpMethod.Delete)
        method(HttpMethod.Patch)
        header(HttpHeaders.Authorization)
        header("MyCustomHeader")
        allowCredentials = true
        anyHost()
    }
    install(DefaultHeaders)
    install(ContentNegotiation) {
        jackson {}
    }
    install(StatusPages) {
        setup()
    }
    install(Authentication) {
        jwt("jwt") {
            verifier(authVerifier())
            realm = "kblog.dev"
            validate {
                val userRepository: UserRepository by Injector.get.instance()
                it.payload.getClaim("id").asInt()?.let { userRepository.getUser(it) }
            }
        }
    }

    connectDB()

    install(Routing) {
        auth()
        users()
    }
}