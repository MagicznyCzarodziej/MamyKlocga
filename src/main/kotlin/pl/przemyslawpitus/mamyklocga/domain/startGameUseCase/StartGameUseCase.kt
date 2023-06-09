package pl.przemyslawpitus.mamyklocga.domain.startGameUseCase

import pl.przemyslawpitus.mamyklocga.WithLogger
import pl.przemyslawpitus.mamyklocga.domain.Room
import pl.przemyslawpitus.mamyklocga.domain.RoomId
import pl.przemyslawpitus.mamyklocga.domain.RoomRepository
import pl.przemyslawpitus.mamyklocga.domain.RoomState
import pl.przemyslawpitus.mamyklocga.domain.UserId
import java.time.Instant

class StartGameUseCase(
    private val roomRepository: RoomRepository,
    private val gameCreator: GameCreator,
    private val gameStatusPublisher: GameStatusPublisher,
) {
    fun startGame(userId: UserId, roomId: RoomId) {
        val room = roomRepository.getByRoomId(roomId = roomId)

        if (room == null) {
            throw RuntimeException("Room ${roomId.value} not found")
        }

        if (room.ownerUser.userId != userId) {
            throw RuntimeException("User ${userId.value} is not owner of room ${room.roomId.value}")
        }

        val updatedRoom = room.startGame()

        val savedRoom = roomRepository.saveRoom(updatedRoom)
        logger.info("Game started, roomId: ${room.roomId.value}")

        gameStatusPublisher.gameStarted(room = savedRoom)
    }

    private fun Room.startGame(): Room {
        val now = Instant.now()

        return copy(
            state = RoomState.IN_GAME,
            game = gameCreator.createGame(users = users),
            updatedAt = now
        )
    }

    private companion object : WithLogger()
}

