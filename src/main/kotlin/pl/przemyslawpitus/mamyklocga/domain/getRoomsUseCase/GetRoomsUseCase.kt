package pl.przemyslawpitus.mamyklocga.domain.getRoomsUseCase

import pl.przemyslawpitus.mamyklocga.WithLogger
import pl.przemyslawpitus.mamyklocga.domain.Room
import pl.przemyslawpitus.mamyklocga.domain.RoomRepository
import pl.przemyslawpitus.mamyklocga.domain.RoomVisibility

class GetRoomsUseCase(
    private val roomRepository: RoomRepository,
) {
    fun getPublicRooms(): List<Room> {
        return roomRepository.getByVisibility(RoomVisibility.PUBLIC)
            .also { logger.info("Found ${it.size} public rooms") }
    }

    private companion object : WithLogger()
}