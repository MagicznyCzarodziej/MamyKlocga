package pl.przemyslawpitus.mamyklocga.domain.startRoundUseCase

import pl.przemyslawpitus.mamyklocga.domain.Room
import pl.przemyslawpitus.mamyklocga.domain.RoomRepository
import pl.przemyslawpitus.mamyklocga.domain.UserId
import pl.przemyslawpitus.mamyklocga.domain.startGameUseCase.GameStatusPublisher
import java.time.Instant

class StartRoundUseCase(
    private val roomRepository: RoomRepository,
    private val gameStatusPublisher: GameStatusPublisher,
) {
    fun startRound(roomCode: String, userId: UserId) {
        val room = roomRepository.getByCode(roomCode = roomCode)
        if (room == null) throw RuntimeException("Room with code $roomCode not found")
        if (room.ownerUser.userId != userId) {
            throw RuntimeException("User ${userId.value} is not owner of room with code $roomCode")
        }

        val updatedRoom = room.startCurrentRound()
        gameStatusPublisher.roundStarted(updatedRoom)
        roomRepository.saveRoom(updatedRoom)
    }
}

private fun Room.startCurrentRound(): Room {
    val game = checkNotNull(this.game) { "Game should not be null when trying to end active round" }
    val currentRound = checkNotNull(game.currentRound) {
        "Current round should not be null when trying to end active round"
    }

    if (currentRound.isEnded) throw RuntimeException("Cannot start ended round, roomCode: ${this.code}")

    return this.copy(
        game = game.copy(
            currentRound = currentRound.copy(
                startedAt = Instant.now(),
            )
        ),
        updatedAt = Instant.now(),
    )
}