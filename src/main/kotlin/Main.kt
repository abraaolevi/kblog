import io.ktor.application.*
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import server.module

fun main(args: Array<String>) {
    val server = embeddedServer(
            Netty,
            8080,
            watchPaths = listOf("kblog"),
            module = Application::module
    )
    server.start()
}