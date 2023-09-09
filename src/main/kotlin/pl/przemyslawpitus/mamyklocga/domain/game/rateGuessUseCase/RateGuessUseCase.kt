package pl.przemyslawpitus.mamyklocga.domain.game.rateGuessUseCase

import arrow.optics.Optional
import arrow.optics.dsl.index
import arrow.optics.typeclasses.Index
import pl.przemyslawpitus.mamyklocga.domain.game.Build
import pl.przemyslawpitus.mamyklocga.domain.game.builder
import pl.przemyslawpitus.mamyklocga.domain.game.builds
import pl.przemyslawpitus.mamyklocga.domain.game.correctAnswerBy
import pl.przemyslawpitus.mamyklocga.domain.game.currentRound
import pl.przemyslawpitus.mamyklocga.domain.game.hasRatedGuesserGuess
import pl.przemyslawpitus.mamyklocga.domain.rooms.Room
import pl.przemyslawpitus.mamyklocga.domain.rooms.RoomChangedEvent
import pl.przemyslawpitus.mamyklocga.domain.rooms.RoomRepository
import pl.przemyslawpitus.mamyklocga.domain.rooms.RoomWatchingManager
import pl.przemyslawpitus.mamyklocga.domain.rooms.game
import pl.przemyslawpitus.mamyklocga.domain.rooms.getRoomUseCase.RoomToUserRoomMapper
import pl.przemyslawpitus.mamyklocga.domain.rooms.UserRoom
import pl.przemyslawpitus.mamyklocga.domain.user.UserId
import pl.przemyslawpitus.mamyklocga.domain.user.UserRepository

class RateGuessUseCase(
    private val roomRepository: RoomRepository,
    private val userRepository: UserRepository,
    private val roomToUserRoomMapper: RoomToUserRoomMapper,
    private val roomWatchingManager: RoomWatchingManager,
) {
    fun rateGuess(roomCode: String, userId: UserId, hasGuesserGuessedCorrectly: Boolean): UserRoom {
        val room = roomRepository.getByCode(roomCode = roomCode)
        if (room == null) throw RuntimeException("Room with code $roomCode not found")

        val user = checkNotNull(userRepository.getByUserId(userId)) { "User not found" }
        val userBuildLens = userBuildLens(room, userId)

        if (userBuildLens.builder.getOrNull(room) != user) {
            throw RuntimeException("User ${userId.value} is not a builder in room with code $roomCode")
        }

        val updatedRoom = room
            .letIf(hasGuesserGuessedCorrectly) {
                val guesser = checkNotNull(it.game?.currentRound?.guesser)
                userBuildLens.correctAnswerBy.set(it, guesser)
            }
            .let {
                userBuildLens.hasRatedGuesserGuess.set(it, true)
            }


        val savedRoom = roomRepository.saveRoom(updatedRoom)
        roomWatchingManager.publish(RoomChangedEvent(savedRoom))

        return roomToUserRoomMapper.mapRoomToUserRoom(savedRoom, user)
    }

    private fun userBuildLens(room: Room, userId: UserId): Optional<Room, Build> {
        val buildIndex = checkNotNull(room.game?.currentRound?.builds?.indexOfFirst { it.builder.userId == userId })
        return Room.game.currentRound.builds.index(Index.list(), buildIndex)
    }
}

private inline fun <T> T.letIf(condition: Boolean, block: (T) -> T): T {
    if (condition) return block(this)
    return this
}
