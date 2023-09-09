package pl.przemyslawpitus.mamyklocga.domain.rooms

import pl.przemyslawpitus.mamyklocga.domain.game.Build
import pl.przemyslawpitus.mamyklocga.domain.game.Challenge
import pl.przemyslawpitus.mamyklocga.domain.user.User
import java.time.Instant

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
        val builds: List<Build>,
        val role: UserRole,
        val guesser: User,
        val challenge: Challenge,
        val endsAt: Instant?,
        val state: UserRoundState,

        val users: Set<RoundUser>,
        val hasRatedGuesserGuess: Boolean,
        val hasRatedStolenGuess: Boolean,
    )

    data class RoundUser(
        val user: User,
        val role: UserRole,
        val hasPassedChallenge: Boolean,
        val hasGuessedCorrectly: Boolean,
    )

    enum class UserRole {
        BUILDER, GUESSER
    }

    enum class UserRoundState {
        WAITING_TO_START, IN_PROGRESS, ENDED
    }
}