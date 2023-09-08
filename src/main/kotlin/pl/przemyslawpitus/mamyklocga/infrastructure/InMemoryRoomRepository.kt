package pl.przemyslawpitus.mamyklocga.infrastructure

import pl.przemyslawpitus.mamyklocga.domain.rooms.Room
import pl.przemyslawpitus.mamyklocga.domain.rooms.RoomId
import pl.przemyslawpitus.mamyklocga.domain.rooms.RoomRepository
import pl.przemyslawpitus.mamyklocga.domain.rooms.RoomVisibility
import pl.przemyslawpitus.mamyklocga.domain.user.UserId

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

    override fun getAll(): List<Room> {
        return rooms.values.toList()
    }
}