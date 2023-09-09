package pl.przemyslawpitus.mamyklocga.domain.user

import java.time.Instant

data class User(
    val userId: UserId,
    val username: String? = null,
    val createdAt: Instant = Instant.now(),
    val updatedAt: Instant = Instant.now(),
    val version: Long = 0,
) {
    val requiredUsername: String
        get() = checkNotNull(username)
}

@JvmInline
value class UserId(val value: String)
