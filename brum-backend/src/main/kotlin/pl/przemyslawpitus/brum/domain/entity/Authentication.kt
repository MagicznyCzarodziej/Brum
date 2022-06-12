package pl.przemyslawpitus.brum.domain.entity

data class Credentials(
    val username: String,
    val password: String,
)

data class AuthenticationDetails(
    val username: String,
    val accessToken: String,
    val refreshToken: String,
)
