package pl.przemyslawpitus.mamyklocga.domain.game.endRoundsUseCase

import pl.przemyslawpitus.mamyklocga.WithLogger
import pl.przemyslawpitus.mamyklocga.domain.rooms.Room
import pl.przemyslawpitus.mamyklocga.domain.rooms.RoomRepository
import pl.przemyslawpitus.mamyklocga.domain.rooms.RoomChangedEvent
import pl.przemyslawpitus.mamyklocga.domain.rooms.RoomState
import pl.przemyslawpitus.mamyklocga.domain.rooms.RoomWatchingManager
import java.time.Instant
import kotlin.time.toJavaDuration

class EndRoundsUseCase(
    private val roomRepository: RoomRepository,
    private val roomWatchingManager: RoomWatchingManager,
) {
    fun endRounds() {
        val rooms = roomRepository.getAll()
        val roomsWithActiveRound = findRoomsToEndRound(rooms)

        roomsWithActiveRound.forEach {
            val game = checkNotNull(it.game) { "Game should not be null when trying to end active round" }
            val isGameEnded = game.currentRound.roundNumber >= game.roundsTotal

            val updatedRoom = if (isGameEnded) {
                it.endGame()
            } else {
                it.endCurrentRound()
            }

            val savedRoom = roomRepository.saveRoom(updatedRoom)
            roomWatchingManager.publish(
                RoomChangedEvent(
                    room = savedRoom,
                )
            )
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

    return this.copy(
        game = game.copy(
            currentRound = game.currentRound.copy(
                isEnded = true,
            )
        ),
        updatedAt = Instant.now(),
    )
}

private fun Room.endGame(): Room {
    val game = checkNotNull(this.game) { "Game should not be null when trying to end active round" }

    return this.copy(
        state = RoomState.GAME_ENDED,
        game = game.copy(
            currentRound = game.currentRound.copy(
                isEnded = true,
            )
            // TODO: Make currentRound nullable and set it to null?
        ),
        updatedAt = Instant.now(),
    )
}