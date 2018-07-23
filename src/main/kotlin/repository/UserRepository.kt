package repository

import model.entity.User
import model.table.Users
import org.jetbrains.exposed.sql.*
import utility.Security

class UserRepository(val security: Security): AppRepository() {

    suspend fun getAllUsers(): List<User> {
        return query {
            Users.selectAll().map { toUser(it) }
        }
    }

    suspend fun getUserByEmail(email: String): User? {
        return query {
            Users.select { Users.email eq email }.mapNotNull { toUser(it) }.singleOrNull()
        }
    }

    suspend fun getUser(id: Int): User? {
        return query {
            Users.select { Users.id eq id }.mapNotNull { toUser(it) }.singleOrNull()
        }
    }

    suspend fun updateUser(user: User): User {
        val id = user.id
        return if (id == null) {
            addUser(user)
        } else {
            query {
                Users.update({ Users.id eq id }) {
                    it[name] = user.name
                    it[email] = user.email
                }
            }
            getUser(id)!!
        }
    }

    suspend fun addUser(user: User): User {
        var key: Int? = 0
        query {
            key = Users.insert {
                it[name] = user.name
                it[email] = user.email
                it[password] = security.hashPassword(user.password)
            } get Users.id
        }
        return getUser(key!!)!!
    }

    suspend fun deleteUser(id: Int): Boolean {
        return query {
            Users.deleteWhere { Users.id eq id } > 0
        }
    }

    private fun toUser(row: ResultRow): User =
            User(
                    id = row[Users.id],
                    name = row[Users.name],
                    email = row[Users.email],
                    password = row[Users.password]
            )

}