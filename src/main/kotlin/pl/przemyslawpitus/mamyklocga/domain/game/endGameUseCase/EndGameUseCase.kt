package pl.przemyslawpitus.mamyklocga.domain.game.endGameUseCase

import pl.przemyslawpitus.mamyklocga.domain.rooms.Room
import pl.przemyslawpitus.mamyklocga.domain.rooms.RoomChangedEvent
import pl.przemyslawpitus.mamyklocga.domain.rooms.RoomRepository
import pl.przemyslawpitus.mamyklocga.domain.rooms.RoomState
import pl.przemyslawpitus.mamyklocga.domain.rooms.RoomWatchingManager
import pl.przemyslawpitus.mamyklocga.domain.user.UserId
import java.time.Instant

class EndGameUseCase(
    private val roomRepository: RoomRepository,
    private val roomWatchingManager: RoomWatchingManager,
) {
    fun endGame(roomCode: String, userId: UserId) {
        val room = roomRepository.getByCode(roomCode = roomCode)
        if (room == null) throw RuntimeException("Room with code $roomCode not found")
        if (room.ownerUser.userId != userId) {
            throw RuntimeException("User ${userId.value} is not owner of room with code $roomCode")
        }

        val game = checkNotNull(room.game) { "Game should not be null when trying to end active round" }
        val isRoundEnded = game.currentRound.isEnded

        if (!isRoundEnded) {
            throw RuntimeException("Cannot end game in room with code $roomCode. Current round is not ended")
        }

        val updatedRoom = room.endGame()
        val savedRoom = roomRepository.saveRoom(updatedRoom)
        roomWatchingManager.publish(RoomChangedEvent(savedRoom))
    }
}


private fun Room.endGame(): Room {
    val game = checkNotNull(this.game) { "Game should not be null when trying to end active round" }

    return this.copy(
        state = RoomState.GAME_ENDED,
        game = game.copy(
            currentRound = game.currentRound.copy(
                isEnded = true,
            )
            // TODO: Make currentRound nullable and set it to null?
        ),
        updatedAt = Instant.now(),
    )
}