package pl.przemyslawpitus.mamyklocga.domain.game.startGameUseCase

import pl.przemyslawpitus.mamyklocga.WithLogger
import pl.przemyslawpitus.mamyklocga.domain.rooms.Room
import pl.przemyslawpitus.mamyklocga.domain.rooms.RoomChangedEvent
import pl.przemyslawpitus.mamyklocga.domain.rooms.RoomRepository
import pl.przemyslawpitus.mamyklocga.domain.rooms.RoomState
import pl.przemyslawpitus.mamyklocga.domain.rooms.RoomWatchingManager
import pl.przemyslawpitus.mamyklocga.domain.user.UserId
import java.time.Instant

class StartGameUseCase(
    private val roomRepository: RoomRepository,
    private val gameCreator: GameCreator,
    private val roomWatchingManager: RoomWatchingManager,
) {
    fun startGame(userId: UserId, roomCode: String) {
        val room = roomRepository.getByCode(roomCode = roomCode)

        if (room == null) {
            throw RuntimeException("Room $roomCode not found")
        }

        if (room.ownerUser.userId != userId) {
            throw RuntimeException("User ${userId.value} is not owner of room ${room.roomId.value}")
        }

        if (room.users.size < 2) {
            throw RuntimeException("Cannot start a game without at least 2 users. Room ${room.roomId.value}")
        }

        val updatedRoom = room.startGame()

        val savedRoom = roomRepository.saveRoom(updatedRoom)

        roomWatchingManager.publish(
            RoomChangedEvent(
                room = savedRoom
            )
        )

        logger.info("Game started, roomId: ${room.roomId.value}")
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
