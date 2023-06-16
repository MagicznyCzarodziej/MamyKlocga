package pl.przemyslawpitus.mamyklocga.domain.game

import pl.przemyslawpitus.mamyklocga.domain.User
import pl.przemyslawpitus.mamyklocga.domain.UserId
import java.time.Instant
import kotlin.time.Duration

data class Game(
    val roundsTotal: Int,
    val currentRound: Round?,
    val rounds: List<Round>,
    val wordsPerUser: Map<UserId, List<Word>>,
)

data class Round(
    val roundNumber: Int,
    val guesser: User,
    val builds: List<Build>,
    val challenge: Challenge,
    val timeTotal: Duration,
    val startedAt: Instant?,
    val isEnded: Boolean,
)

data class Build(
    val builder: User,
    val hasPassedChallenge: Boolean,
    val correctAnswerBy: User?,
)

data class Word(
    val wordId: String, // TODO make WordId
    val text: String,
)