package utility

import org.mindrot.jbcrypt.BCrypt

class Security {
    fun hashPassword(plaintext: String): String {
        return BCrypt.hashpw(plaintext, BCrypt.gensalt(12))
    }

    fun checkPassword(plaintext: String, hashed: String): Boolean {
        return BCrypt.checkpw(plaintext, hashed)
    }
}