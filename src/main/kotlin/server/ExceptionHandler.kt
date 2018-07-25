package server

import http.exception.AppException
import io.ktor.application.call
import io.ktor.features.StatusPages
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import service.AppService

fun StatusPages.Configuration.setup() {
    exception<Exception> { internal ->
        val status = when (internal) {
            is AppException -> internal.status
            else -> HttpStatusCode.InternalServerError
        }

        when {
            status.value.toString().startsWith("5") -> {
                call.respond(status, AppService().error(message = InternalServerError.message ?: "", code = InternalServerError.status.value))
            }
            else -> {
                call.respond(status, AppService().error(message = internal.message ?: "", code = status.value))
            }
        }
    }
}

val InternalServerError =
        StatusException("Sorry, we encountered an error and are working on it.", HttpStatusCode.InternalServerError)

open class StatusException(
        message: String?,
        open val status: HttpStatusCode = HttpStatusCode.InternalServerError,
        cause: Throwable? = null
) : Exception(message, cause)