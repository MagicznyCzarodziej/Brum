package pl.przemyslawpitus.brum.domain.service.authentication

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import pl.przemyslawpitus.brum.domain.entity.AuthenticationDetails
import pl.przemyslawpitus.brum.domain.entity.Credentials
import pl.przemyslawpitus.brum.domain.entity.RegisterUser
import pl.przemyslawpitus.brum.domain.entity.User
import pl.przemyslawpitus.brum.domain.repository.UserRepository

@Service
class AuthenticationService(
    val userRepository: UserRepository,
    val passwordEncoder: BCryptPasswordEncoder,
    val tokenService: TokenService,
) {
    init {
        register("deko", "pass")
        register("gram", "pass")
    }

    fun register(username: String, password: String) {
        val passwordHash = passwordEncoder.encode(password)
        val user = RegisterUser(username = username, passwordHash = passwordHash)

        println("Saving user $username")
        userRepository.saveUser(user)
    }

    fun login(credentials: Credentials): AuthenticationDetails {
        val user = userRepository.findByUsername(credentials.username)
            ?: throw UserNotFoundException(credentials.username)

        val passwordsMatch = passwordEncoder.matches(
            credentials.password,
            user.passwordHash,
        )
        if (!passwordsMatch) throw InvalidPasswordException(credentials.username)

        return createAuthenticationDetails(user = user)
    }

    fun refreshToken(refreshToken: String): AuthenticationDetails {
        val claims = tokenService.verifyTokenAndGetClaims(refreshToken)
        val user = userRepository.findByUsername(claims.subject)
            ?: throw UserNotFoundException(username = claims.subject)

        return createAuthenticationDetails(user = user)
    }

    private fun createAuthenticationDetails(user: User): AuthenticationDetails {
        val accessToken = tokenService.createAccessToken(
            id = user.id,
            username = user.username,
        )
        val refreshToken = tokenService.createRefreshToken(
            id = user.id,
            username = user.username,
        )

        return AuthenticationDetails(
            username = user.username,
            accessToken = accessToken,
            refreshToken = refreshToken,
        )
    }
}

data class UserNotFoundException(val username: String) : RuntimeException("User $username not found")

data class InvalidPasswordException(val username: String) : RuntimeException("Invalid password for user $username")