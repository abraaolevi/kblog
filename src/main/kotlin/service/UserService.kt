package service

import model.entity.User
import org.json.simple.JSONObject
import repository.UserRepository

class UserService(
        private val userRepo: UserRepository
) : AppService() {

    suspend fun getAllUsers(): JSONObject {
        return success(userRepo.getAllUsers())
    }

    suspend fun getUser(id: Int): JSONObject {
        val user = userRepo.getUser(id) ?: return error("User not found", 404)
        return success(user)
    }

    suspend fun addUser(user: User): JSONObject {
        if (userRepo.getUserByEmail(user.email) != null) {
            return error("User already registered")
        }
        return success(userRepo.addUser(user), 201)
    }

    suspend fun updateUser(user: User): JSONObject {
        return success(userRepo.updateUser(user))
    }

    suspend fun deleteUser(id: Int): JSONObject {
        val isRemoved = userRepo.deleteUser(id)
        return if (isRemoved) {
            success(mapOf("removed" to true))
        } else {
            error("User not found", 404)
        }
    }
}