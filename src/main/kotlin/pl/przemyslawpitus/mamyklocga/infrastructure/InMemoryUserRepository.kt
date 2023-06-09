package pl.przemyslawpitus.mamyklocga.infrastructure

import pl.przemyslawpitus.mamyklocga.domain.User
import pl.przemyslawpitus.mamyklocga.domain.UserId
import pl.przemyslawpitus.mamyklocga.domain.UserRepository
import java.util.*

class InMemoryUserRepository : UserRepository {
    private val users: MutableMap<UserId, User> = mutableMapOf()

    override fun saveUser(user: User): User {
        users[user.userId] = user
        return user
    }

    override fun getByUserId(userId: UserId): User? {
        return users[userId]
    }

    override fun getUserByClientSessionId(clientSessionId: UUID): User? {
        return users.values.find { it.session?.clientSessionId == clientSessionId }
    }
}