package pl.przemyslawpitus.mamyklocga.domain.rooms.leaveRoomUseCase

import pl.przemyslawpitus.mamyklocga.WithLogger
import pl.przemyslawpitus.mamyklocga.domain.rooms.Room
import pl.przemyslawpitus.mamyklocga.domain.rooms.RoomRepository
import pl.przemyslawpitus.mamyklocga.domain.user.User
import pl.przemyslawpitus.mamyklocga.domain.user.UserId
import pl.przemyslawpitus.mamyklocga.domain.user.UserRepository
import pl.przemyslawpitus.mamyklocga.domain.rooms.RoomState
import pl.przemyslawpitus.mamyklocga.domain.rooms.RoomWatchingManager
import java.time.Instant

class LeaveRoomUseCase(
    private val roomRepository: RoomRepository,
    private val userRepository: UserRepository,
    private val roomWatchingManager: RoomWatchingManager,
) {
    fun leaveRoom(room: Room, userId: UserId): Room {
        val user = userRepository.getByUserId(userId = userId)
        if (user == null) throw RuntimeException("User ${userId.value} not found")

        var updatedRoom = room.removeUser(userId)
        if (updatedRoom.ownerUser.userId == user.userId) {
            logger.info("Closing room ${room.roomId.value} ${room.code}")

            updatedRoom = room.closeRoom()
        }
        val savedRoom = roomRepository.saveRoom(updatedRoom)

//        logger.info("Publishing event in leaving room ${room.code}")
//        roomWatchingManager.publish(
//            RoomChangedEvent(
//                room = savedRoom
//            )
//        )

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

private fun Room.removeUser(userId: UserId): Room {
    return copy(
        users = users.filter { it.userId != userId }.toSet(),
        updatedAt = Instant.now(),
    )
}


private fun Room.closeRoom(): Room {
    return copy(
        state = RoomState.CLOSED,
        updatedAt = Instant.now(),
    )
}