package pl.przemyslawpitus.mamyklocga.domain.rooms

import arrow.optics.Optional
import arrow.optics.dsl.index
import arrow.optics.optics
import arrow.optics.typeclasses.Index
import pl.przemyslawpitus.mamyklocga.domain.game.Build
import pl.przemyslawpitus.mamyklocga.domain.user.User
import pl.przemyslawpitus.mamyklocga.domain.game.Game
import pl.przemyslawpitus.mamyklocga.domain.game.builds
import pl.przemyslawpitus.mamyklocga.domain.game.rounds
import pl.przemyslawpitus.mamyklocga.domain.user.UserId
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
    fun userBuildLens(userId: UserId): Optional<Room, Build> {
        val buildIndex = checkNotNull(this.game?.currentRound?.builds?.indexOfFirst { it.builder.userId == userId })
        val currentRoundIndex = checkNotNull(this.game?.currentRoundIndex)

        return Room.game.rounds.index(Index.list(), currentRoundIndex).builds.index(Index.list(), buildIndex)
    }

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
