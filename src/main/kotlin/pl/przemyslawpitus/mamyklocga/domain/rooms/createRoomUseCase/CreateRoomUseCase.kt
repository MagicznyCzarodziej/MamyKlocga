package pl.przemyslawpitus.mamyklocga.domain.rooms.createRoomUseCase

import pl.przemyslawpitus.mamyklocga.WithLogger
import pl.przemyslawpitus.mamyklocga.domain.rooms.Room
import pl.przemyslawpitus.mamyklocga.domain.rooms.RoomChangedEvent
import pl.przemyslawpitus.mamyklocga.domain.rooms.RoomId
import pl.przemyslawpitus.mamyklocga.domain.rooms.RoomRepository
import pl.przemyslawpitus.mamyklocga.domain.rooms.RoomVisibility
import pl.przemyslawpitus.mamyklocga.domain.rooms.RoomWatchingManager
import pl.przemyslawpitus.mamyklocga.domain.user.UserId
import pl.przemyslawpitus.mamyklocga.domain.user.UserRepository
import pl.przemyslawpitus.mamyklocga.domain.rooms.leaveRoomUseCase.LeaveRoomUseCase
import java.util.UUID
import kotlin.random.Random
import kotlin.random.nextInt

class CreateRoomUseCase(
    private val roomRepository: RoomRepository,
    private val userRepository: UserRepository,
    private val leaveRoomUseCase: LeaveRoomUseCase,
    private val roomWatchingManager: RoomWatchingManager,
) {
    fun createRoom(
        roomName: String,
        ownerUserId: UserId,
    ): Room {
        val ownerUser = userRepository.getByUserId(ownerUserId) ?: throw RuntimeException("User ${ownerUserId.value} not found")

        leaveRoomUseCase.leaveAllRooms(ownerUser)

        val room = Room(
            roomId = RoomId(UUID.randomUUID().toString()),
            code = Random.nextInt(1000..9999).toString(),
            name = roomName,
            ownerUser = ownerUser,
            users = setOf(ownerUser),
            visibility = RoomVisibility.PUBLIC,
        )

        val savedRoom = roomRepository.saveRoom(room)

        roomWatchingManager.publish(RoomChangedEvent(savedRoom))

        logger.info(
            "Created new room, code: ${savedRoom.code}, roomId: ${savedRoom.roomId.value}" +
                    "roomName: ${savedRoom.name}, ownerUserId: ${savedRoom.ownerUser.userId.value}"
        )

        return savedRoom
    }

    private companion object : WithLogger()
}
