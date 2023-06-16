package pl.przemyslawpitus.mamyklocga.domain

import java.time.Instant
import java.util.UUID

data class User(
    val userId: UserId,
    val username: String? = null,
    val session: Session? = null,
    val createdAt: Instant = Instant.now(),
    val updatedAt: Instant = Instant.now(),
    val version: Long = 0,
) {
    val requiredUsername: String
        get() = checkNotNull(username)
}

@JvmInline
value class UserId(val value: String)

data class Session(
    val sessionId: SessionId,
)

@JvmInline
value class SessionId(val value: String) {
    fun asUUID() = UUID.fromString(this.value)
}