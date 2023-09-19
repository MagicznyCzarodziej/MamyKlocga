package pl.przemyslawpitus.mamyklocga.domain.rooms

import arrow.optics.optics
import pl.przemyslawpitus.mamyklocga.domain.user.User
import pl.przemyslawpitus.mamyklocga.domain.game.Game
import java.time.Instant

@optics
data class Room(
    val roomId: RoomId,
    val code: String,
    val name: String,
    val ownerUser: User,
    val users: Set<User>,
    val visibility: RoomVisibility,
    val state: RoomState = RoomState.CREATED,
    val game: Game? = null,
    val createdAt: Instant = Instant.now(),
    val updatedAt: Instant = Instant.now(),
    val version: Long = 0,
) {
    companion object
}

enum class RoomVisibility {
    PUBLIC, PRIVATE
}

enum class RoomState {
    CREATED, IN_GAME, GAME_ENDED, ABORTED, CLOSED
}

@JvmInline
value class RoomId(val value: String)
