package pl.przemyslawpitus.mamyklocga.domain.createRoomUseCase

import pl.przemyslawpitus.mamyklocga.WithLogger
import pl.przemyslawpitus.mamyklocga.domain.Room
import pl.przemyslawpitus.mamyklocga.domain.RoomId
import pl.przemyslawpitus.mamyklocga.domain.RoomRepository
import pl.przemyslawpitus.mamyklocga.domain.RoomVisibility
import pl.przemyslawpitus.mamyklocga.domain.UserId
import pl.przemyslawpitus.mamyklocga.domain.UserRepository
import pl.przemyslawpitus.mamyklocga.domain.leaveRoomUseCase.LeaveRoomUseCase
import pl.przemyslawpitus.mamyklocga.domain.startGameUseCase.GameStatusPublisher
import java.util.UUID

class CreateRoomUseCase(
    private val roomRepository: RoomRepository,
    private val userRepository: UserRepository,
    private val leaveRoomUseCase: LeaveRoomUseCase,
    private val gameStatusPublisher: GameStatusPublisher,
) {
    fun createRoom(
        roomName: String,
        ownerUserId: UserId,
    ): Room {
        val ownerUser = userRepository.getByUserId(ownerUserId) ?: throw RuntimeException("User ${ownerUserId.value} not found")

        leaveRoomUseCase.leaveAllRooms(ownerUser)

        val room = Room(
            roomId = RoomId(UUID.randomUUID().toString()),
            code = "1234",
            name = roomName,
            ownerUser = ownerUser,
            users = setOf(ownerUser),
            visibility = RoomVisibility.PUBLIC,
        )

        gameStatusPublisher.joinRoom(
            roomId = room.roomId,
            user = ownerUser,
        )

        val savedRoom = roomRepository.saveRoom(room)
        logger.info(
            "Created new room, roomId: ${savedRoom.roomId.value}, " +
                    "roomName: ${savedRoom.name}, ownerUserId: ${savedRoom.ownerUser.userId.value}"
        )
        return savedRoom
    }

    private companion object : WithLogger()
}
