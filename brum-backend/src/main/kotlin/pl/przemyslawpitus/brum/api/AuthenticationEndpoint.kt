package pl.przemyslawpitus.brum.api

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseCookie
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pl.przemyslawpitus.brum.config.authentication.AuthenticationProperties
import pl.przemyslawpitus.brum.domain.entity.AuthenticationDetails
import pl.przemyslawpitus.brum.domain.entity.Credentials
import pl.przemyslawpitus.brum.domain.service.authentication.AuthenticationService
import pl.przemyslawpitus.brum.domain.service.authentication.InvalidPasswordException
import pl.przemyslawpitus.brum.domain.service.authentication.UserNotFoundException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("auth")
class AuthenticationEndpoint(
    val authenticationService: AuthenticationService,
    val authenticationProperties: AuthenticationProperties
) {
    @PostMapping("login", consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun login(@RequestBody credentials: CredentialsDto, response: HttpServletResponse): ResponseEntity<*> {
        try {
            val authentication = authenticationService.login(credentials.toDomain())

            val accessTokenCookie =
                createSecureCookie(authenticationProperties.accessTokenCookieName, authentication.accessToken)
            val refreshTokenCookie =
                createSecureCookie(authenticationProperties.refreshTokenCookieName, authentication.refreshToken)

            return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, accessTokenCookie)
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie)
                .body(authentication.toDto())
        } catch (exception: Exception) {
            if (exception is InvalidPasswordException || exception is UserNotFoundException) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                    InvalidLoginCredentialsDto(
                        error = "Invalid username or password"
                    )
                )
            }
            throw exception
        }
    }

    @PostMapping("refreshToken")
    fun refreshToken(request: HttpServletRequest, response: HttpServletResponse): ResponseEntity<*> {
        val refreshToken = request.cookies
            .find { it.name == authenticationProperties.refreshTokenCookieName }
            ?.value
            ?: throw MissingRefreshTokenException()

        val authentication = authenticationService.refreshToken(refreshToken = refreshToken)

        val accessTokenCookie =
            createSecureCookie(authenticationProperties.accessTokenCookieName, authentication.accessToken)
        val refreshTokenCookie =
            createSecureCookie(authenticationProperties.refreshTokenCookieName, authentication.refreshToken)

        return ResponseEntity
            .noContent()
            .header(HttpHeaders.SET_COOKIE, accessTokenCookie)
            .header(HttpHeaders.SET_COOKIE, refreshTokenCookie)
            .build<Void>()
    }

    private fun createSecureCookie(name: String, value: String) = ResponseCookie
        .from(
            name,
            value,
        )
        .secure(true)
        .httpOnly(true)
        .path("/")
        .build()
        .toString()
}

class MissingRefreshTokenException : RuntimeException("Missing refresh token")

private fun AuthenticationDetails.toDto() = LoginResponseDto(
    username = this.username
)

data class LoginResponseDto(
    val username: String,
)

data class InvalidLoginCredentialsDto(
    val error: String,
)

data class CredentialsDto(
    val username: String,
    val password: String,
) {
    fun toDomain() = Credentials(
        username = username,
        password = password,
    )
}