package pl.przemyslawpitus.mamyklocga.domain.getRoomUseCase

import pl.przemyslawpitus.mamyklocga.domain.Room
import pl.przemyslawpitus.mamyklocga.domain.RoomRepository
import pl.przemyslawpitus.mamyklocga.domain.RoomState
import pl.przemyslawpitus.mamyklocga.domain.User
import pl.przemyslawpitus.mamyklocga.domain.UserId
import pl.przemyslawpitus.mamyklocga.domain.UserRepository
import pl.przemyslawpitus.mamyklocga.domain.game.Challenge
import pl.przemyslawpitus.mamyklocga.domain.game.Game
import pl.przemyslawpitus.mamyklocga.domain.game.PointsCounter
import pl.przemyslawpitus.mamyklocga.domain.game.Round
import java.time.Instant
import kotlin.time.toJavaDuration

class GetRoomUseCase(
    private val roomRepository: RoomRepository,
    private val userRepository: UserRepository,
    private val pointsCounter: PointsCounter,
) {
    fun getRoom(roomCode: String, userId: UserId): UserRoom {
        val user = userRepository.getByUserId(userId)
        if (user == null) throw RuntimeException("User ${userId.value} not found")

        val room = roomRepository.getByCode(roomCode)
        if (room == null) throw RuntimeException("Room with code $roomCode not found")

        val isUserInRoom = room.users.any {it.userId == user.userId}
        if (!isUserInRoom) throw RuntimeException("User ${userId.value} is not in room $roomCode. User: $user, Users in room: ${room.users.map { it.toString() }}")

        val userRoom = mapRoomToUserRoom(
            room = room,
            user = user,
        )

        return userRoom
    }

    private fun mapRoomToUserRoom(
        room: Room,
        user: User,
    ) = UserRoom(
        code = room.code,
        name = room.name,
        users = room.users,
        state = room.state,
        game = room.game?.let { mapGame(
            game = it,
            user = user
        ) },
    )

    private fun mapGame(game: Game, user: User): UserRoom.UserGame {
        val userPoints = pointsCounter.countPointsForGame(game).getValue(user.userId)

        return UserRoom.UserGame(
            roundsTotal = game.roundsTotal,
            currentRound = mapCurrentRound(
                currentRound = game.currentRound!!,
                user = user,
            ),
            myPoints = userPoints,
            words = game.wordsPerUser.getValue(user.userId).map { it.text }
        )
    }

    private fun mapCurrentRound(currentRound: Round, user: User) = UserRoom.UserRound(
        roundNumber = currentRound.roundNumber,
        role = getUserRole(
            round = currentRound,
            user = user
        ),
        guesser = currentRound.guesser,
        challenge = currentRound.challenge,
        endsAt = currentRound.startedAt?.let { it + currentRound.timeTotal.toJavaDuration() }
    )

    private fun getUserRole(round: Round, user: User) =
        if (round.guesser == user) UserRoom.UserRole.GUESSER
        else UserRoom.UserRole.BUILDER
}

data class UserRoom(
    val code: String,
    val name: String,
    val users: Set<User>,
    val state: RoomState,
    val game: UserGame?,
) {

    data class UserGame(
        val roundsTotal: Int,
        val currentRound: UserRound,
        val myPoints: Int,
        val words: List<String>,
    )

    data class UserRound(
        val roundNumber: Int,
        val role: UserRole,
        val guesser: User,
        val challenge: Challenge,
        val endsAt: Instant?,
    )

    enum class UserRole {
        BUILDER, GUESSER
    }
}