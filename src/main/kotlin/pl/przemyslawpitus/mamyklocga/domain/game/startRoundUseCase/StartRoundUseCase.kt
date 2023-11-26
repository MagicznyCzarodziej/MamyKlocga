package pl.przemyslawpitus.mamyklocga.domain.game.startRoundUseCase

import pl.przemyslawpitus.mamyklocga.domain.game.Game
import pl.przemyslawpitus.mamyklocga.domain.rooms.Room
import pl.przemyslawpitus.mamyklocga.domain.rooms.RoomRepository
import pl.przemyslawpitus.mamyklocga.domain.user.UserId
import pl.przemyslawpitus.mamyklocga.domain.rooms.RoomChangedEvent
import pl.przemyslawpitus.mamyklocga.domain.rooms.RoomWatchingManager
import java.time.Instant

class StartRoundUseCase(
    private val roomRepository: RoomRepository,
    private val roomWatchingManager: RoomWatchingManager,
) {
    fun startRound(roomCode: String, userId: UserId) {
        val room = roomRepository.getByCode(roomCode = roomCode)
        if (room == null) throw RuntimeException("Room with code $roomCode not found")
        if (room.ownerUser.userId != userId) {
            throw RuntimeException("User ${userId.value} is not owner of room with code $roomCode")
        }

        val updatedRoom = room.startCurrentRound()
        val savedRoom = roomRepository.saveRoom(updatedRoom)
        roomWatchingManager.publish(
            RoomChangedEvent(
                room = savedRoom,
            )
        )
    }
}

private fun Room.startCurrentRound(): Room {
    val game = checkNotNull(this.game) { "Game should not be null when trying to end active round" }

    if (game.currentRound.isEnded) throw RuntimeException("Cannot start ended round, roomCode: ${this.code}")

    return this.copy(
        game = game.startCurrentRound(),
        updatedAt = Instant.now(),
    )
}

private fun Game.startCurrentRound(): Game {
    return copy(
        rounds = rounds.mapIndexed { index, round ->
            if (index == currentRoundIndex) {
                round.copy(startedAt = Instant.now())
            } else {
                round.copy()
            }
        }
    )
}
