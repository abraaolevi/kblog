package http.exception

import io.ktor.http.HttpStatusCode

val UserNotFound = AppException(HttpStatusCode.NotFound, "The specified user could not be found")

val UserAlreadyExists = AppException(HttpStatusCode.Conflict, "The specified user already exists")