import com.auth0.jwt.JWTVerifier
import core.Injector
import core.database.Connection
import http.*
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.auth.*
import io.ktor.auth.jwt.jwt
import io.ktor.jackson.*
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
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
    install(ContentNegotiation) {
        jackson {}
    }

    connectDB()

    install(Routing) {
        auth()
        users()
    }
}

fun main(args: Array<String>) {
    val server = embeddedServer(
            Netty,
            8080,
            watchPaths = listOf("kblog"),
            module = Application::module
    )
    server.start()
}

