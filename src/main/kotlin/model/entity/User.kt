package model.entity

import io.ktor.auth.Principal

data class User(
        val id: Int?,
        val name: String,
        val email: String,
        val password: String
) : Principal