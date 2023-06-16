package pl.przemyslawpitus.mamyklocga.domain.joinRoomUseCase

import pl.przemyslawpitus.mamyklocga.WithLogger
import pl.przemyslawpitus.mamyklocga.domain.RoomRepository
import pl.przemyslawpitus.mamyklocga.domain.User
import pl.przemyslawpitus.mamyklocga.domain.UserId
import pl.przemyslawpitus.mamyklocga.domain.UserRepository
import pl.przemyslawpitus.mamyklocga.domain.Room
import pl.przemyslawpitus.mamyklocga.domain.RoomState
import pl.przemyslawpitus.mamyklocga.domain.leaveRoomUseCase.LeaveRoomUseCase
import pl.przemyslawpitus.mamyklocga.domain.startGameUseCase.GameStatusPublisher
import java.time.Instant

class JoinRoomUseCase(
    private val roomRepository: RoomRepository,
    private val userRepository: UserRepository,
    private val leaveRoomUseCase: LeaveRoomUseCase,
    private val gameStatusPublisher: GameStatusPublisher,
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
        gameStatusPublisher.joinRoom(
            roomId = updatedRoom.roomId,
            user = user,
        )
        roomRepository.saveRoom(room = updatedRoom)

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