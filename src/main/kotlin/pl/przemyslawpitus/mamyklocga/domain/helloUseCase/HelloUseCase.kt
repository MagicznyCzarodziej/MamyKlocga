package pl.przemyslawpitus.mamyklocga.domain.helloUseCase

import pl.przemyslawpitus.mamyklocga.WithLogger
import pl.przemyslawpitus.mamyklocga.domain.Session
import pl.przemyslawpitus.mamyklocga.domain.User
import pl.przemyslawpitus.mamyklocga.domain.UserId
import pl.przemyslawpitus.mamyklocga.domain.UserRepository
import java.time.Instant
import java.util.*

class HelloUseCase(
    private val userRepository: UserRepository,
) {
    fun getOrCreateUser(userId: String?, clientSessionId: UUID): HelloResult {
        if (userId == null) {
            return createUser(clientSessionId)
        }

        val existingUser = userRepository.getByUserId(UserId(userId))
        if (existingUser == null) {
            return createUser(clientSessionId)
        }

        val updatedUser = existingUser.updateSession(clientSessionId)
        val savedUser = userRepository.saveUser(updatedUser)

        return HelloResult(
            user = savedUser,
            isNewUser = false,
        )
    }

    private fun createUser(clientSessionId: UUID): HelloResult {
        val userId = UserId(UUID.randomUUID().toString())
        val user = User(
            userId = userId,
            session = Session(
                clientSessionId = clientSessionId,
            )
        )

        val savedUser = userRepository.saveUser(user)

        logger.info("Created new user, userId: ${savedUser.userId.value}")

        return HelloResult(
            user = savedUser,
            isNewUser = true
        )
    }

    private companion object : WithLogger()
}

private fun User.updateSession(clientSessionId: UUID): User {
    return copy(
        session = Session(
            clientSessionId = clientSessionId,
        ),
        updatedAt = Instant.now(),
    )
}

data class HelloResult(
    val user: User,
    val isNewUser: Boolean,
)