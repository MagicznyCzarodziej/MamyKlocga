package pl.przemyslawpitus.mamyklocga.domain.setUsernameUseCase

import pl.przemyslawpitus.mamyklocga.WithLogger
import pl.przemyslawpitus.mamyklocga.domain.User
import pl.przemyslawpitus.mamyklocga.domain.UserRepository
import java.time.Instant

private const val MIN_USERNAME_LENGTH = 3
private const val MAX_USERNAME_LENGTH = 20

class SetUsernameUseCase(
    private val userRepository: UserRepository,
) {

    fun setUsername(user: User, username: String): User {
        val cleanedUsername = cleanUsername(username) // TODO Improve this
        validateUsername(cleanedUsername)

        val updatedUser = user.updateUsername(cleanedUsername)

        return userRepository
            .saveUser(updatedUser)
            .also { logger.info("Set user ${it.userId.value} username: ${it.username}") }
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