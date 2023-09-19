package pl.przemyslawpitus.mamyklocga.domain.game

import pl.przemyslawpitus.mamyklocga.domain.user.User
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class RoundCreator(
    private val challengeProvider: ChallengeProvider,
) {
    fun createRound(users: Set<User>, roundNumber: Int = 1): Round {
        // TODO: Cycle users in next rounds instead of shuffling
        val shuffledUsers = users.shuffled()
        val guesser = shuffledUsers.first()
        val builders = shuffledUsers.drop(1)

        val builds = createBuilds(builders = builders)

        return Round(
            roundNumber = roundNumber,
            guesser = guesser,
            builds = builds,
            challenge = challengeProvider.getRandomChallenge(),
            timeTotal = 5.toDuration(DurationUnit.MINUTES),
            startedAt = null,
            isEnded = false
        )
    }

    private fun createBuilds(builders: List<User>): List<Build> {
        return builders.map {
            Build(
                builder = it,
                hasPassedChallenge = false,
                hasRatedGuesserGuess = false,
                hasRatedStolenGuess = false,
                correctAnswerBy = null,
            )
        }
    }
}