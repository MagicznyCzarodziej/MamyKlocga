package pl.przemyslawpitus.mamyklocga.domain.game.endRoundsUseCase

import pl.przemyslawpitus.mamyklocga.WithLogger
import pl.przemyslawpitus.mamyklocga.domain.rooms.Room
import pl.przemyslawpitus.mamyklocga.domain.rooms.RoomRepository
import pl.przemyslawpitus.mamyklocga.domain.rooms.RoomChangedEvent
import pl.przemyslawpitus.mamyklocga.domain.rooms.RoomWatchingManager
import java.time.Instant
import kotlin.time.toJavaDuration

class EndRoundsUseCase(
    private val roomRepository: RoomRepository,
    private val roomWatchingManager: RoomWatchingManager,
) {
    fun endRounds() {
        val rooms = roomRepository.getAll()
//        logger.info("Rooms: ${rooms.map { it.roomId.value }}")
        val roomsWithActiveRound = findRoomsToEndRound(rooms)
//        logger.info("Rooms with active rounds: ${roomsWithActiveRound.map { it.roomId.value }}")
        roomsWithActiveRound.forEach {
            val updatedRoom = it.endCurrentRound()

            val savedRoom = roomRepository.saveRoom(updatedRoom)
            roomWatchingManager.publish(RoomChangedEvent(
                room = savedRoom,
            ))
        }
    }

    private fun findRoomsToEndRound(rooms: List<Room>): List<Room> =
        rooms.filter {
            val currentRound = it.game?.currentRound ?: return@filter false
            if (currentRound.isEnded) return@filter false
            if (currentRound.startedAt == null) return@filter false
            currentRound.startedAt + currentRound.timeTotal.toJavaDuration() < Instant.now()
        }

    private companion object : WithLogger()
}

private fun Room.endCurrentRound(): Room {
    val game = checkNotNull(this.game) { "Game should not be null when trying to end active round" }
    val currentRound = checkNotNull(game.currentRound) {
        "Current round should not be null when trying to end active round"
    }

    return this.copy(
        game = game.copy(
            currentRound = currentRound.copy(
                isEnded = true,
            )
        ),
        updatedAt = Instant.now(),
    )
}