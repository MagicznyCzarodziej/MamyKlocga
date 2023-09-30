package pl.przemyslawpitus.mamyklocga.domain.game.rateGuessUseCase

import arrow.optics.copy
import pl.przemyslawpitus.mamyklocga.WithLogger
import pl.przemyslawpitus.mamyklocga.domain.game.builder
import pl.przemyslawpitus.mamyklocga.domain.game.correctAnswerBy
import pl.przemyslawpitus.mamyklocga.domain.game.hasRatedGuesserGuess
import pl.przemyslawpitus.mamyklocga.domain.game.hasRatedStolenGuess
import pl.przemyslawpitus.mamyklocga.domain.rooms.Room
import pl.przemyslawpitus.mamyklocga.domain.rooms.RoomChangedEvent
import pl.przemyslawpitus.mamyklocga.domain.rooms.RoomRepository
import pl.przemyslawpitus.mamyklocga.domain.rooms.RoomWatchingManager
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
    fun rateGuess(
        roomCode: String,
        userId: UserId,
        ratedUserId: UserId?,
        hasGuessedCorrectly: Boolean,
    ): UserRoom {
        val room = roomRepository.getByCode(roomCode = roomCode)
        if (room == null) throw RuntimeException("Room with code $roomCode not found")

        val user = checkNotNull(userRepository.getByUserId(userId)) { "User not found" }
        val userBuildLens = room.userBuildLens(userId)

        if (userBuildLens.builder.getOrNull(room) != user) {
            throw RuntimeException("User ${userId.value} is not a builder in room with code $roomCode")
        }

        if (ratedUserId == null && hasGuessedCorrectly) {
            throw RuntimeException("Cannot rate as correct guess without rated user id")
        }

        val ratedUser = ratedUserId?.let { userRepository.getByUserId(it) }
        val guesser = checkNotNull(room.game?.currentRound?.guesser)
        logger.info("ratedUser: $ratedUser")
        logger.info("guesser: $guesser")

        val updatedRoom = if (ratedUser == null) {
            logger.info("no one guessed")
            room.setAllRated(userId)
        } else {
            if (ratedUser !in room.users) {
                throw RuntimeException("Rated user is not in the room")
            }

            if (ratedUser == user) {
                throw RuntimeException("Cant't guess your own build")
            }

            room
                .letIf(hasGuessedCorrectly) {
                    it.userBuildLens(userId)
                        .correctAnswerBy.set(it, ratedUser)
                        .setAllRated(userId)
                }.letIf(ratedUser == guesser) {
                    it.userBuildLens(userId).hasRatedGuesserGuess.set(it, true)
                }.letIf(ratedUser != guesser) {
                    it.setAllRated(userId)
                }
        }

        val savedRoom = roomRepository.saveRoom(updatedRoom)
        roomWatchingManager.publish(RoomChangedEvent(savedRoom))

        return roomToUserRoomMapper.mapRoomToUserRoom(savedRoom, user)
    }

    private fun Room.setAllRated(userId: UserId): Room {
        val room = this
        return copy {
            room.userBuildLens(userId).hasRatedGuesserGuess set true
            room.userBuildLens(userId).hasRatedStolenGuess set true
        }
    }

    private companion object : WithLogger()
}

private inline fun <T> T.letIf(condition: Boolean, block: (T) -> T): T {
    if (condition) return block(this)
    return this
}
