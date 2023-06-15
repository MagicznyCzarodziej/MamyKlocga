package pl.przemyslawpitus.mamyklocga.domain.helloUseCase

import pl.przemyslawpitus.mamyklocga.WithLogger
import pl.przemyslawpitus.mamyklocga.domain.User
import pl.przemyslawpitus.mamyklocga.domain.UserId
import pl.przemyslawpitus.mamyklocga.domain.UserRepository
import java.util.UUID

class HelloUseCase(
    private val userRepository: UserRepository,
) {
    fun getOrCreateUser(userId: String?): HelloResult {
        if (userId == null) {
            return createUser()
        }

        val existingUser = userRepository.getByUserId(UserId(userId))
        if (existingUser == null) {
            return createUser()
        }

        return HelloResult(
            user = existingUser,
            isNewUser = false,
        )
    }

    private fun createUser(): HelloResult {
        val userId = UserId(UUID.randomUUID().toString())
        val user = User(
            userId = userId,
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

data class HelloResult(
    val user: User,
    val isNewUser: Boolean,
)