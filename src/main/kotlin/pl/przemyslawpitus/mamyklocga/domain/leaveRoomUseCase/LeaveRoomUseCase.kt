package pl.przemyslawpitus.mamyklocga.domain.leaveRoomUseCase

import pl.przemyslawpitus.mamyklocga.WithLogger
import pl.przemyslawpitus.mamyklocga.domain.Room
import pl.przemyslawpitus.mamyklocga.domain.RoomRepository
import pl.przemyslawpitus.mamyklocga.domain.User
import pl.przemyslawpitus.mamyklocga.domain.UserId
import pl.przemyslawpitus.mamyklocga.domain.UserRepository
import pl.przemyslawpitus.mamyklocga.domain.startGameUseCase.GameStatusPublisher
import java.time.Instant

class LeaveRoomUseCase(
    private val roomRepository: RoomRepository,
    private val userRepository: UserRepository,
    private val gameStatusPublisher: GameStatusPublisher,
) {
    fun leaveRoom(room: Room, userId: UserId): Room {
        val updatedRoom = room.removeUser(userId)
        val user = userRepository.getByUserId(userId = userId)

        if (user == null) throw RuntimeException("User ${userId.value} not found")

        gameStatusPublisher.leaveRoom(
            roomId = updatedRoom.roomId,
            user = user,
        )
        val savedRoom = roomRepository.saveRoom(updatedRoom)

        logger.info("User ${userId.value} left room ${room.roomId.value}")
        return savedRoom
    }

    fun leaveAllRooms(user: User) {
        val userRooms = roomRepository.getByUserId(userId = user.userId)
        logger.info("User ${user.userId.value} leaving all (${userRooms.size}) rooms")
        userRooms.forEach {
            leaveRoom(room = it, userId = user.userId)
        }
    }

    private companion object : WithLogger()
}

fun Room.removeUser(userId: UserId): Room {
    return copy(
        users = users.filter { it.userId != userId }.toSet(),
        updatedAt = Instant.now(),
    )
}