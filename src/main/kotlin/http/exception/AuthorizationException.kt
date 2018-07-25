package http.exception

import io.ktor.http.HttpStatusCode

val UserForbidden = AppException(HttpStatusCode.Forbidden, HttpStatusCode.Forbidden.description)

val UserUnauthorized = AppException(HttpStatusCode.Unauthorized, HttpStatusCode.Unauthorized.description)