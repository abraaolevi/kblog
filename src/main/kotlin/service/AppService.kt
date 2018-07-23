package service

import org.json.simple.JSONObject

open class AppService {
    private fun response(status: String, data: Any, code: Int): JSONObject {
        return JSONObject(
                mapOf(
                        "code" to code,
                        "status" to status,
                        "data" to data
                )
        )
    }

    protected fun success(data: Any, code: Int = 200): JSONObject {
        return response("success", data, code)
    }

    protected fun error(message: String, code: Int = 200): JSONObject {
        return response("error", mapOf("message" to message), code)
    }

    protected fun fail(message: String, code: Int = 400): JSONObject {
        return response("fail", mapOf("message" to message), code)
    }
}