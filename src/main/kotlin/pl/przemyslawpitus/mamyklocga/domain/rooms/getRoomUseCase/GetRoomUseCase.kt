package pl.przemyslawpitus.mamyklocga.domain.rooms.getRoomUseCase

import pl.przemyslawpitus.mamyklocga.domain.rooms.RoomRepository
import pl.przemyslawpitus.mamyklocga.domain.user.UserId
import pl.przemyslawpitus.mamyklocga.domain.user.UserRepository

class GetRoomUseCase(
    private val roomRepository: RoomRepository,
    private val userRepository: UserRepository,
    private val roomToUserRoomMapper: RoomToUserRoomMapper,
) {
    fun getRoom(roomCode: String, userId: UserId): UserRoom {
        val user = userRepository.getByUserId(userId)
        if (user == null) throw RuntimeException("User ${userId.value} not found")

        val room = roomRepository.getByCode(roomCode)
        if (room == null) throw RuntimeException("Room with code $roomCode not found")

        val isUserInRoom = room.users.any { it.userId == user.userId }
        if (!isUserInRoom) throw RuntimeException("User ${userId.value} is not in room $roomCode. User: $user, Users in room: ${room.users.map { it.toString() }}")

        val userRoom = roomToUserRoomMapper.mapRoomToUserRoom(
            room = room,
            user = user,
        )

        return userRoom
    }


}

