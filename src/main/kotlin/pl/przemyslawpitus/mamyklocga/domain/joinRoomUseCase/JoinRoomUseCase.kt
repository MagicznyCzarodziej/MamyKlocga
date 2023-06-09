package pl.przemyslawpitus.mamyklocga.domain.joinRoomUseCase

import pl.przemyslawpitus.mamyklocga.WithLogger
import pl.przemyslawpitus.mamyklocga.domain.RoomRepository
import pl.przemyslawpitus.mamyklocga.domain.User
import pl.przemyslawpitus.mamyklocga.domain.UserId
import pl.przemyslawpitus.mamyklocga.domain.UserRepository
import pl.przemyslawpitus.mamyklocga.domain.Room
import pl.przemyslawpitus.mamyklocga.domain.RoomId
import pl.przemyslawpitus.mamyklocga.domain.leaveRoomUseCase.LeaveRoomUseCase
import pl.przemyslawpitus.mamyklocga.domain.setUsernameUseCase.SetUsernameUseCase
import pl.przemyslawpitus.mamyklocga.domain.startGameUseCase.GameStatusPublisher
import java.time.Instant

class JoinRoomUseCase(
    private val roomRepository: RoomRepository,
    private val userRepository: UserRepository,
    private val setUsernameUseCase: SetUsernameUseCase,
    private val leaveRoomUseCase: LeaveRoomUseCase,
    private val gameStatusPublisher: GameStatusPublisher,
) {
    fun joinRoom(
        roomId: RoomId,
        userId: UserId,
        username: String,
    ) {
        val user = userRepository.getByUserId(userId)
        if (user == null) throw RuntimeException("User not found") // TODO

        val room = roomRepository.getByRoomId(roomId = roomId)
        if (room == null) throw RuntimeException("Room not found") // TODO

        val updatedUser = setUsernameUseCase.setUsername(user, username)

        leaveRoomUseCase.leaveAllRooms(updatedUser)

        val updatedRoom = room.addUser(updatedUser)
        gameStatusPublisher.joinRoom(
            roomId = updatedRoom.roomId,
            user = updatedUser,
        )
        roomRepository.saveRoom(room = updatedRoom)

        logger.info("User ${updatedUser.userId} joined room ${room.roomId.value}")
    }

    private companion object : WithLogger()
}

private fun Room.addUser(user: User): Room {
    return copy(
        users = this.users + user,
        updatedAt = Instant.now(),
    )
}