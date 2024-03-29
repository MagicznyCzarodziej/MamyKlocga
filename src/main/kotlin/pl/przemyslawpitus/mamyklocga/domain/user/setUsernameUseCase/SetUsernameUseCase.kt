package pl.przemyslawpitus.mamyklocga.domain.user.setUsernameUseCase

import pl.przemyslawpitus.mamyklocga.WithLogger
import pl.przemyslawpitus.mamyklocga.domain.user.User
import pl.przemyslawpitus.mamyklocga.domain.user.UserId
import pl.przemyslawpitus.mamyklocga.domain.user.UserRepository
import java.time.Instant

private const val MIN_USERNAME_LENGTH = 3
private const val MAX_USERNAME_LENGTH = 20

class SetUsernameUseCase(
    private val userRepository: UserRepository,
) {

    fun setUsername(userId: UserId, username: String): User {
        val user = userRepository.getByUserId(userId)
        if (user == null) throw RuntimeException("User with id ${userId.value} not found")

        val cleanedUsername = cleanUsername(username) // TODO Improve this
        validateUsername(cleanedUsername)

        val updatedUser = user.updateUsername(cleanedUsername)

        return userRepository
            .saveUser(updatedUser)
            .also { logger.info("User ${it.userId.value} username was set to: ${it.username}") }
    }

    private fun cleanUsername(username: String) = username.filter { it.isLetterOrDigit() }

    private fun validateUsername(username: String) {
        if (username.length < MIN_USERNAME_LENGTH) throw RuntimeException("Username $username is too short")
        if (username.length > MAX_USERNAME_LENGTH) throw RuntimeException("Username $username is too long")
    }

    private companion object : WithLogger()
}

private fun User.updateUsername(username: String): User {
    return copy(
        username = username,
        updatedAt = Instant.now(),
    )
}