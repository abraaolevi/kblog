package http.exception

import io.ktor.http.HttpStatusCode

open class AppException(val status: HttpStatusCode, override val message: String) : Exception()