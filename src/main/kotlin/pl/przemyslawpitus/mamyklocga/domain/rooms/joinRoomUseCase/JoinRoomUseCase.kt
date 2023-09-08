package pl.przemyslawpitus.mamyklocga.domain.rooms.joinRoomUseCase

import pl.przemyslawpitus.mamyklocga.WithLogger
import pl.przemyslawpitus.mamyklocga.domain.rooms.RoomRepository
import pl.przemyslawpitus.mamyklocga.domain.user.User
import pl.przemyslawpitus.mamyklocga.domain.user.UserId
import pl.przemyslawpitus.mamyklocga.domain.user.UserRepository
import pl.przemyslawpitus.mamyklocga.domain.rooms.Room
import pl.przemyslawpitus.mamyklocga.domain.rooms.RoomState
import pl.przemyslawpitus.mamyklocga.domain.rooms.leaveRoomUseCase.LeaveRoomUseCase
import pl.przemyslawpitus.mamyklocga.domain.game.startGameUseCase.GameStatusPublisher
import pl.przemyslawpitus.mamyklocga.domain.rooms.RoomChangedEvent
import pl.przemyslawpitus.mamyklocga.domain.rooms.RoomWatchingManager
import java.time.Instant

class JoinRoomUseCase(
    private val roomRepository: RoomRepository,
    private val userRepository: UserRepository,
    private val leaveRoomUseCase: LeaveRoomUseCase,
    private val roomWatchingManager: RoomWatchingManager,
) {
    fun joinRoom(
        roomCode: String,
        userId: UserId,
    ) {
        val user = userRepository.getByUserId(userId)
        if (user == null) throw RuntimeException("User not found") // TODO

        val room = roomRepository.getByCode(roomCode = roomCode)
        if (room == null) throw RuntimeException("Room not found") // TODO

        if (room.state != RoomState.CREATED) {
            throw RuntimeException("User ${userId.value} cant join room $roomCode, because game has already started")
        }

        leaveRoomUseCase.leaveAllRooms(user)

        val updatedRoom = room.addUser(user)

        val savedRoom = roomRepository.saveRoom(room = updatedRoom)

        roomWatchingManager.publish(
            RoomChangedEvent(
                room = room
            )
        )

        logger.info("User ${user.userId} joined room ${room.code}")
    }

    private companion object : WithLogger()
}

private fun Room.addUser(user: User): Room {
    return copy(
        users = this.users + user,
        updatedAt = Instant.now(),
    )
}