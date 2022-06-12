package pl.przemyslawpitus.brum.domain.repository

import pl.przemyslawpitus.brum.domain.entity.RegisterUser
import pl.przemyslawpitus.brum.domain.entity.User

interface UserRepository {
    fun saveUser(user: RegisterUser)
    fun findByUsername(username: String): User?
}