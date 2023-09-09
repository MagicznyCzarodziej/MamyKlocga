package pl.przemyslawpitus.mamyklocga.domain.game

import arrow.optics.optics
import pl.przemyslawpitus.mamyklocga.domain.user.User
import pl.przemyslawpitus.mamyklocga.domain.user.UserId
import java.time.Instant
import kotlin.time.Duration

@optics
data class Game(
    val roundsTotal: Int,
    val currentRound: Round,
    val pastRounds: List<Round>,
    val wordsPerUser: Map<UserId, List<Word>>,
) {
    companion object
}

@optics
data class Round(
    val roundNumber: Int,
    val guesser: User,
    val builds: List<Build>,
    val challenge: Challenge,
    val timeTotal: Duration,
    val startedAt: Instant?,
    val isEnded: Boolean,
) {
    fun getBuildOfUser(user: User): Build? =
        this.builds.find { it.builder.userId == user.userId }

    companion object
}

@optics
data class Build(
    val builder: User,
    val hasPassedChallenge: Boolean,
    val hasRatedGuesserGuess: Boolean,
    val hasRatedStolenGuess: Boolean,
    val correctAnswerBy: User?,
) {
    companion object
}

data class Word(
    val wordId: String, // TODO make WordId
    val text: String,
)