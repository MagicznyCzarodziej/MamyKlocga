package pl.przemyslawpitus.mamyklocga.domain.rooms.getRoomUseCase

import pl.przemyslawpitus.mamyklocga.domain.game.Game
import pl.przemyslawpitus.mamyklocga.domain.game.PointsCounter
import pl.przemyslawpitus.mamyklocga.domain.game.Round
import pl.przemyslawpitus.mamyklocga.domain.rooms.Room
import pl.przemyslawpitus.mamyklocga.domain.rooms.UserRoom
import pl.przemyslawpitus.mamyklocga.domain.user.User
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
                currentRound = game.currentRound,
                user = user,
            ),
            myPoints = userPoints,
            words = game.wordsPerUser.getValue(user.userId).map { it.text }
        )
    }

    private fun mapCurrentRound(currentRound: Round, user: User): UserRoom.UserRound {
        val build = currentRound.getBuildOfUser(user)

        return UserRoom.UserRound(
            roundNumber = currentRound.roundNumber,
            builds = currentRound.builds,
            role = getUserRole(round = currentRound, user = user),
            guesser = currentRound.guesser,
            challenge = currentRound.challenge,
            endsAt = getEndAt(round = currentRound),
            state = getRoundState(round = currentRound),

            users = getRoundUsers(round = currentRound),
            hasRatedGuesserGuess = build?.hasRatedGuesserGuess ?: false,
            hasRatedStolenGuess = build?.hasRatedStolenGuess ?: false,
        )
    }

    private fun getRoundUsers(round: Round): Set<UserRoom.RoundUser> {
        val guesser = UserRoom.RoundUser(
            user = round.guesser,
            role = UserRoom.UserRole.GUESSER,
            hasPassedChallenge = false,
            hasGuessedCorrectly = round.builds.also {
                println(it)
            }.any { it.correctAnswerBy == round.guesser },
        )
        val builders = round.builds.map {
            UserRoom.RoundUser(
                user = it.builder,
                role = UserRoom.UserRole.BUILDER,
                hasPassedChallenge = checkNotNull(round.getBuildOfUser(it.builder)).hasPassedChallenge,
                hasGuessedCorrectly = false,
            )
        }
        return setOf(guesser) + builders
    }

    private fun getUserRole(round: Round, user: User) =
        if (round.guesser.userId == user.userId) UserRoom.UserRole.GUESSER
        else UserRoom.UserRole.BUILDER

    private fun getRoundState(round: Round): UserRoom.UserRoundState =
        if (round.isEnded) UserRoom.UserRoundState.ENDED
        else if (round.startedAt != null) UserRoom.UserRoundState.IN_PROGRESS
        else UserRoom.UserRoundState.WAITING_TO_START

    private fun getEndAt(round: Round) =
        round.startedAt?.let { it + round.timeTotal.toJavaDuration() }
}
