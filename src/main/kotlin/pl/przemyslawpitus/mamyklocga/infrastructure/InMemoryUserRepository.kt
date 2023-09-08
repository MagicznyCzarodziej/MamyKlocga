package pl.przemyslawpitus.mamyklocga.infrastructure

import pl.przemyslawpitus.mamyklocga.domain.user.SessionId
import pl.przemyslawpitus.mamyklocga.domain.user.User
import pl.przemyslawpitus.mamyklocga.domain.user.UserId
import pl.przemyslawpitus.mamyklocga.domain.user.UserRepository

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