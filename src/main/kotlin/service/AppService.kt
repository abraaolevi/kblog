package service

import http.exception.UserForbidden
import http.exception.UserUnauthorized
import model.entity.User
import org.json.simple.JSONObject

open class AppService {

    var loggedUser: User? = null

    protected fun requireLogin(checkUserId: Int) {
        loggedUser ?: throw UserUnauthorized
        if (loggedUser!!.id != checkUserId) {
            throw UserForbidden
        }
    }

    private fun response(status: String, data: Any, code: Int) = JSONObject(
            mapOf(
                    "code" to code,
                    "status" to status,
                    "data" to data
            )
    )

    fun success(data: Any, code: Int = 200) = response("success", data, code)

    fun error(message: String, code: Int = 200) = response("error", mapOf("message" to message), code)
}