package pl.przemyslawpitus.brum.domain.entity

data class RegisterUser(
    val username: String,
    val passwordHash: String,
)

typealias UserId = Int

data class User(
    val id: UserId,
    val username: String,
    val passwordHash: String,
)

data class UserDetails(
    val id: UserId,
    val username: String,
)