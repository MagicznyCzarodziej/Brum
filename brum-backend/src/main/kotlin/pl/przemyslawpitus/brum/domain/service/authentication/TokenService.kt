package pl.przemyslawpitus.brum.domain.service.authentication

import io.jsonwebtoken.Claims
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.stereotype.Service
import pl.przemyslawpitus.brum.config.authentication.AuthenticationProperties
import pl.przemyslawpitus.brum.config.authentication.InvalidTokenException
import java.time.Duration
import java.time.Instant
import java.time.temporal.TemporalAmount
import java.util.Date

val ACCESS_TOKEN_EXPIRATION_TIME: TemporalAmount = Duration.ofHours(1)
val REFRESH_TOKEN_EXPIRATION_TIME: TemporalAmount = Duration.ofDays(7)

@Service
class TokenService(
    private val authenticationProperties: AuthenticationProperties,
) {
    fun createAccessToken(id: Int, username: String): String {
       return Jwts.builder()
            .setIssuer(id.toString())
            .setSubject(username)
            .setExpiration(Date.from(Instant.now().plus(ACCESS_TOKEN_EXPIRATION_TIME)))
            .signWith(SignatureAlgorithm.HS256, authenticationProperties.secret)
            .compact()
    }

    fun createRefreshToken(id: Int, username: String): String {
        return Jwts.builder()
            .setIssuer(id.toString())
            .setSubject(username)
            .setExpiration(Date.from(Instant.now().plus(REFRESH_TOKEN_EXPIRATION_TIME)))
            .signWith(SignatureAlgorithm.HS256, authenticationProperties.secret)
            .compact()
    }

    fun verifyTokenAndGetClaims(jws: String): Claims {
        try {
            return Jwts.parser()
                .setSigningKey(authenticationProperties.secret)
                .parseClaimsJws(jws)
                .body
        } catch (exception: JwtException) {
            throw InvalidTokenException(exception)
        }
    }
}

