package pl.przemyslawpitus.mamyklocga.domain.game.rateChallengeUseCase

import pl.przemyslawpitus.mamyklocga.domain.game.hasPassedChallenge
import pl.przemyslawpitus.mamyklocga.domain.rooms.RoomChangedEvent
import pl.przemyslawpitus.mamyklocga.domain.rooms.RoomRepository
import pl.przemyslawpitus.mamyklocga.domain.rooms.RoomWatchingManager
import pl.przemyslawpitus.mamyklocga.domain.rooms.UserRoom
import pl.przemyslawpitus.mamyklocga.domain.rooms.getRoomUseCase.RoomToUserRoomMapper
import pl.przemyslawpitus.mamyklocga.domain.user.UserId
import pl.przemyslawpitus.mamyklocga.domain.user.UserRepository

class RateChallengeUseCase(
    private val roomRepository: RoomRepository,
    private val userRepository: UserRepository,
    private val roomToUserRoomMapper: RoomToUserRoomMapper,
    private val roomWatchingManager: RoomWatchingManager,
) {
    fun rateChallenge(roomCode: String, userId: UserId, ratedUserId: UserId, hasPassedChallenge: Boolean): UserRoom {
        val room = roomRepository.getByCode(roomCode = roomCode)
        if (room == null) throw RuntimeException("Room with code $roomCode not found")

        val user = checkNotNull(userRepository.getByUserId(userId)) { "User not found" }
        val guesser = checkNotNull(room.game?.currentRound?.guesser)

        val userBuildLens = room.userBuildLens(ratedUserId)

        if (guesser != user) {
            throw RuntimeException("User ${userId.value} is not a guesser in room with code $roomCode")
        }

        val updatedRoom = userBuildLens.hasPassedChallenge.set(room, hasPassedChallenge)

        val savedRoom = roomRepository.saveRoom(updatedRoom)
        roomWatchingManager.publish(RoomChangedEvent(savedRoom))

        return roomToUserRoomMapper.mapRoomToUserRoom(savedRoom, user)
    }
}
