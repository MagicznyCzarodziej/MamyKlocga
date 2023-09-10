package pl.przemyslawpitus.mamyklocga.domain.game.getPointsUseCase

import pl.przemyslawpitus.mamyklocga.domain.game.PointsCounter
import pl.przemyslawpitus.mamyklocga.domain.rooms.RoomRepository
import pl.przemyslawpitus.mamyklocga.domain.user.User
import pl.przemyslawpitus.mamyklocga.domain.user.UserId

class GetPointsUseCase(
    private val roomRepository: RoomRepository,
    private val pointsCounter: PointsCounter,
) {
    fun getPoints(roomCode: String, userId: UserId): List<UserPoints> {
        val room = roomRepository.getByCode(roomCode = roomCode)
        if (room == null) throw RuntimeException("Room with code $roomCode not found")
        if (userId !in room.users.map { it.userId }) {
            throw RuntimeException("User ${userId.value} is not in the room with code $roomCode")
        }

        val game = checkNotNull(room.game) { "Game is missing" }
        val pointsPerUserId = pointsCounter.countPointsForGame(game)
        val pointsPerUser = pointsPerUserId.map { (userId, points) ->
            UserPoints(
                user = checkNotNull(room.users.find { it.userId == userId }) { "Cannot count points - user ${userId.value} is not in the room" },
                points = points
            )
        }

        return pointsPerUser
    }
}

data class UserPoints(
    val user: User,
    val points: Int,
)