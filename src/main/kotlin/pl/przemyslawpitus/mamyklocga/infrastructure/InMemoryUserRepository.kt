package pl.przemyslawpitus.mamyklocga.infrastructure

import pl.przemyslawpitus.mamyklocga.domain.SessionId
import pl.przemyslawpitus.mamyklocga.domain.User
import pl.przemyslawpitus.mamyklocga.domain.UserId
import pl.przemyslawpitus.mamyklocga.domain.UserRepository

class InMemoryUserRepository : UserRepository {
    private val users: MutableMap<UserId, User> = mutableMapOf()

    override fun saveUser(user: User): User {
        users[user.userId] = user
        return user
    }

    override fun getByUserId(userId: UserId): User? {
        return users[userId]
    }

    override fun getBySessionId(sessionId: SessionId): User? {
        return users.values.find { it.session?.sessionId == sessionId }
    }
}