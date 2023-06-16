package pl.przemyslawpitus.mamyklocga.infrastructure

import pl.przemyslawpitus.mamyklocga.domain.Room
import pl.przemyslawpitus.mamyklocga.domain.RoomId
import pl.przemyslawpitus.mamyklocga.domain.RoomRepository
import pl.przemyslawpitus.mamyklocga.domain.RoomVisibility
import pl.przemyslawpitus.mamyklocga.domain.UserId

class InMemoryRoomRepository : RoomRepository {
    private val rooms: MutableMap<RoomId, Room> = mutableMapOf()

    override fun saveRoom(room: Room): Room {
        rooms[room.roomId] = room
        return room
    }

    override fun getByRoomId(roomId: RoomId): Room? {
        return rooms[roomId]
    }

    override fun getByCode(roomCode: String): Room? {
        return rooms.values.find { it.code == roomCode }
    }


    override fun getByUserId(userId: UserId): List<Room> {
        return rooms.values.filter {
            it.users.any {
                user -> user.userId == userId
            }
        }
    }

    override fun getByVisibility(visibility: RoomVisibility): List<Room> {
        return rooms.values.filter { it.visibility == visibility }
    }
}