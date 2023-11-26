package pl.przemyslawpitus.mamyklocga.domain.game

import pl.przemyslawpitus.mamyklocga.domain.user.User
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class RoundsCreator(
    private val challengeProvider: ChallengeProvider,
) {
    fun createRounds(users: Set<User>): List<Round> {
        // Double rounds if no more than 3 players
        val roundsToCreate = if (users.size > 3) {
            users
        } else {
            listOf(users, users).flatten()
        }

        return roundsToCreate
            .shuffled()
            .mapIndexed { index, user ->
                createRound(
                    guesser = user,
                    users = users,
                    roundNumber = index + 1,
                )
            }
    }

    private fun createRound(guesser: User, users: Set<User>, roundNumber: Int): Round {
        val builders = users.filterNot { it == guesser }

        val builds = createBuilds(builders = builders)

        return Round(
            roundNumber = roundNumber,
            guesser = guesser,
            builds = builds,
            challenge = challengeProvider.getRandomChallenge(),
            timeTotal = 1.toDuration(DurationUnit.MINUTES),
            startedAt = null,
            isEnded = false,
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