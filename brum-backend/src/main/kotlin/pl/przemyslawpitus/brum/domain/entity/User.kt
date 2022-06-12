package pl.przemyslawpitus.brum.domain.entity

data class RegisterUser(
    val username: String,
    val passwordHash: String,
)

data class User(
    val id: Int,
    val username: String,
    val passwordHash: String,
)