package service

import http.LoginRequest
import org.json.simple.JSONObject
import repository.UserRepository
import utility.Jwt
import utility.Security

class AuthService(
        private val security: Security,
        private val jwt: Jwt,
        private val userRepo: UserRepository
) : AppService() {

    suspend fun authenticate(data: LoginRequest): JSONObject {
        val user = userRepo.getUserByEmail(data.email)
        if (user == null || !security.checkPassword(data.password, user.password)) {
            return error("Invalid user or password")
        }
        return success(mapOf("token" to jwt.makeToken(user)))
    }
}