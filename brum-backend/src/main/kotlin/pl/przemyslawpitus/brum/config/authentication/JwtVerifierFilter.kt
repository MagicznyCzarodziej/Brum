package pl.przemyslawpitus.brum.config.authentication

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.web.filter.OncePerRequestFilter
import pl.przemyslawpitus.brum.domain.entity.UserDetails
import pl.przemyslawpitus.brum.domain.service.authentication.TokenService
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JwtVerifierFilter(
    private val tokenService: TokenService,
    private val authenticationProperties: AuthenticationProperties,
    private val ignoredPaths: String,
) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        if (AntPathRequestMatcher(ignoredPaths).matches(request)) {
            return filterChain.doFilter(request, response)
        }

        try {
            authenticate(request)
        } catch (exception: InvalidTokenException) {
            println(exception)
        } catch (exception: MissingAccessTokenException) {
            println(exception)
        }

        filterChain.doFilter(request, response)
    }

    private fun authenticate(request: HttpServletRequest) {
        val accessToken = extractAccessToken(request)
            ?: throw MissingAccessTokenException()

        val claims = tokenService.verifyTokenAndGetClaims(accessToken)

        val authentication = UsernamePasswordAuthenticationToken(
            UserDetails(
                id = claims.issuer.toInt(),
                username = claims.subject
            ),
            null,
            emptyList(),
        )

        SecurityContextHolder.getContext().authentication = authentication
    }

    private fun extractAccessToken(request: HttpServletRequest): String? {
        return request.cookies
            ?.find { it.name == authenticationProperties.accessTokenCookieName }
            ?.value
    }
}

class InvalidTokenException(exception: Exception) : RuntimeException("Invalid token", exception)

class MissingAccessTokenException : RuntimeException("Missing access token")