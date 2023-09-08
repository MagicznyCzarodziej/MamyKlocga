package pl.przemyslawpitus.mamyklocga.domain.rooms.getRoomUseCase

import pl.przemyslawpitus.mamyklocga.domain.game.Challenge
import pl.przemyslawpitus.mamyklocga.domain.game.Game
import pl.przemyslawpitus.mamyklocga.domain.game.PointsCounter
import pl.przemyslawpitus.mamyklocga.domain.game.Round
import pl.przemyslawpitus.mamyklocga.domain.rooms.Room
import pl.przemyslawpitus.mamyklocga.domain.rooms.RoomState
import pl.przemyslawpitus.mamyklocga.domain.user.User
import java.time.Instant
import kotlin.time.toJavaDuration

class RoomToUserRoomMapper(
    private val pointsCounter: PointsCounter,
) {
    fun mapRoomToUserRoom(
        room: Room,
        user: User,
    ) = UserRoom(
        code = room.code,
        name = room.name,
        isRoomOwner = room.ownerUser.userId == user.userId,
        users = room.users,
        state = room.state,
        game = room.game?.let {
            mapGame(
                game = it,
                user = user
            )
        },
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
        endsAt = currentRound.startedAt?.let { it + currentRound.timeTotal.toJavaDuration() },
        state =
        if (currentRound.isEnded) UserRoom.UserRoundState.ENDED
        else if (currentRound.startedAt != null) UserRoom.UserRoundState.IN_PROGRESS
        else UserRoom.UserRoundState.WAITING_TO_START
    )

    private fun getUserRole(round: Round, user: User) =
        if (round.guesser.userId == user.userId) UserRoom.UserRole.GUESSER
        else UserRoom.UserRole.BUILDER
}

data class UserRoom(
    val code: String,
    val name: String,
    val isRoomOwner: Boolean,
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
        val state: UserRoundState,
    )

    enum class UserRole {
        BUILDER, GUESSER
    }

    enum class UserRoundState {
        WAITING_TO_START, IN_PROGRESS, ENDED
    }
}