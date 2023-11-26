package pl.przemyslawpitus.mamyklocga.domain.game.nextRoundUseCase

import pl.przemyslawpitus.mamyklocga.domain.rooms.Room
import pl.przemyslawpitus.mamyklocga.domain.rooms.RoomChangedEvent
import pl.przemyslawpitus.mamyklocga.domain.rooms.RoomRepository
import pl.przemyslawpitus.mamyklocga.domain.rooms.RoomWatchingManager
import pl.przemyslawpitus.mamyklocga.domain.user.UserId
import java.time.Instant

class NextRoundUseCase(
    private val roomRepository: RoomRepository,
    private val roomWatchingManager: RoomWatchingManager,
) {
    fun nextRound(roomCode: String, userId: UserId) {
        val room = roomRepository.getByCode(roomCode = roomCode)
        if (room == null) throw RuntimeException("Room with code $roomCode not found")
        if (room.ownerUser.userId != userId) {
            throw RuntimeException("User ${userId.value} is not owner of room with code $roomCode")
        }

        val updatedRoom = room.nextRound()

        val savedRoom = roomRepository.saveRoom(updatedRoom)
        roomWatchingManager.publish(RoomChangedEvent(savedRoom))
    }

    private fun Room.nextRound(): Room {
        val game = checkNotNull(this.game) { "Game should not be null when trying to proceed to next round" }

        if (game.currentRound.roundNumber >= game.roundsTotal) {
            throw RuntimeException(
                "Cannot proceed to next round, current round was the last round. roomCode: ${this.code}"
            )
        }

        if (!game.currentRound.isEnded) {
            throw RuntimeException(
                "Cannot proceed to next round, current round is not ended. roomCode: ${this.code}"
            )
        }

        return this.copy(
            game = game.copy(
                currentRoundIndex = game.currentRoundIndex + 1,
            ),
            updatedAt = Instant.now(),
        )
    }
}
