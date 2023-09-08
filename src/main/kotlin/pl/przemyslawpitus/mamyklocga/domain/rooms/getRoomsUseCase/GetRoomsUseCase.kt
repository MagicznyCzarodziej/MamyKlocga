package pl.przemyslawpitus.mamyklocga.domain.rooms.getRoomsUseCase

import pl.przemyslawpitus.mamyklocga.WithLogger
import pl.przemyslawpitus.mamyklocga.domain.rooms.Room
import pl.przemyslawpitus.mamyklocga.domain.rooms.RoomRepository
import pl.przemyslawpitus.mamyklocga.domain.rooms.RoomVisibility

class GetRoomsUseCase(
    private val roomRepository: RoomRepository,
) {
    fun getPublicRooms(): List<Room> {
        return roomRepository.getByVisibility(RoomVisibility.PUBLIC)
            .also { logger.info("Found ${it.size} public rooms") }
    }

    private companion object : WithLogger()
}