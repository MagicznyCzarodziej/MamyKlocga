package pl.przemyslawpitus.mamyklocga.domain

interface RoomRepository {
    fun saveRoom(room: Room): Room
    fun getByRoomId(roomId: RoomId): Room?
    fun getByUserId(userId: UserId): List<Room>
    fun getByVisibility(visibility: RoomVisibility): List<Room>
}