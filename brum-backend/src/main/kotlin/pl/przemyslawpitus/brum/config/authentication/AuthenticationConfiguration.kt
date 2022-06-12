package pl.przemyslawpitus.brum.config.authentication

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import pl.przemyslawpitus.brum.domain.service.authentication.TokenService

@Configuration
@EnableConfigurationProperties(AuthenticationProperties::class)
class AuthenticationConfiguration {
    @Bean
    fun passwordEncoder() = BCryptPasswordEncoder()
}

@ConstructorBinding
@ConfigurationProperties(prefix = "authentication.jwt")
data class AuthenticationProperties(
    val secret: String,
    val accessTokenCookieName: String,
    val refreshTokenCookieName: String,
)

@Configuration
@EnableWebSecurity
class SecurityConfiguration {
    private val unprotectedPaths = "/auth/**"

    @Bean
    fun jwtVerifierFilter(
        tokenService: TokenService,
        authenticationProperties: AuthenticationProperties,
    ) = JwtVerifierFilter(
        tokenService = tokenService,
        authenticationProperties = authenticationProperties,
        ignoredPaths = unprotectedPaths
    )

    @Bean
    fun springWebFilterChain(
        http: HttpSecurity,
        tokenService: TokenService,
        jwtVerifierFilter: JwtVerifierFilter,
    ): SecurityFilterChain {
        return http
            .csrf { it.disable() }
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeHttpRequests {
                it
                    .antMatchers(unprotectedPaths).permitAll()
                    .anyRequest().authenticated()
            }
            .addFilterBefore(
                jwtVerifierFilter,
                UsernamePasswordAuthenticationFilter::class.java,
            )
            .build()
    }
}

