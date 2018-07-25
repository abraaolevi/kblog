package service

import http.exception.UserAlreadyExists
import http.exception.UserForbidden
import http.exception.UserNotFound
import model.entity.User
import org.json.simple.JSONObject
import repository.UserRepository

class UserService(
        private val userRepo: UserRepository
) : AppService() {

    suspend fun getAllUsers() = success(userRepo.getAllUsers())

    suspend fun getUser(id: Int): JSONObject {
        requireLogin(id)
        val user = userRepo.getUser(id) ?: throw UserNotFound
        return success(user)
    }

    suspend fun addUser(user: User): JSONObject {
        if (userRepo.getUser(user.email) != null) {
            throw UserAlreadyExists
        }
        return success(userRepo.addUser(user), 201)
    }

    suspend fun updateUser(user: User): JSONObject {
        requireLogin(user.id!!)
        return success(userRepo.updateUser(user))
    }

    suspend fun deleteUser(id: Int): JSONObject {
        requireLogin(id)
        val isRemoved = userRepo.deleteUser(id)
        return if (isRemoved) {
            success(mapOf("removed" to true))
        } else {
            error("User not found", 404)
        }
    }
}