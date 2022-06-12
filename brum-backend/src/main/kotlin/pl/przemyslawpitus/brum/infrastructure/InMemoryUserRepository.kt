package pl.przemyslawpitus.brum.infrastructure

import org.springframework.stereotype.Repository
import pl.przemyslawpitus.brum.domain.entity.RegisterUser
import pl.przemyslawpitus.brum.domain.entity.User
import pl.przemyslawpitus.brum.domain.repository.UserRepository

@Repository
class InMemoryUserRepository : UserRepository {
    private var nextUserId: Int = 1
    private val users: MutableList<User> = mutableListOf()

    override fun saveUser(user: RegisterUser) {
        val userWithId = User(
            id = nextUserId,
            username = user.username,
            passwordHash = user.passwordHash
        )
        users.add(userWithId)
        nextUserId++
    }

    override fun findByUsername(username: String): User? {
        return users.find { it.username == username }
    }
}