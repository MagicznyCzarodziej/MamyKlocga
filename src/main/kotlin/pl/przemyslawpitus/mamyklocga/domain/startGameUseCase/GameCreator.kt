package pl.przemyslawpitus.mamyklocga.domain.startGameUseCase

import pl.przemyslawpitus.mamyklocga.domain.User
import pl.przemyslawpitus.mamyklocga.domain.game.Build
import pl.przemyslawpitus.mamyklocga.domain.game.ChallengeProvider
import pl.przemyslawpitus.mamyklocga.domain.game.Game
import pl.przemyslawpitus.mamyklocga.domain.game.Round
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class GameCreator(
    private val challengeProvider: ChallengeProvider,
) {
    fun createGame(users: Set<User>): Game {
        val round = createRound(users)

        return Game(
            roundsTotal = getTotalRounds(users),
            currentRound = round,
            rounds = listOf(round),
            wordsPerUser = mapOf()
        )
    }

    private fun getTotalRounds(users: Set<User>): Int =
        if (users.size > 3) {
            users.size
        } else {
            users.size * 2
        }

    private fun createRound(users: Set<User>): Round {
        val shuffledUsers = users.shuffled()
        val guesser = shuffledUsers.first()
        val builders = shuffledUsers.drop(1)

        val builds = createBuilds(builders = builders)

        return Round(
            roundNumber = 2373,
            guesser = guesser,
            builds = builds,
            challenge = challengeProvider.getRandomChallenge(),
            timeTotal = 1.toDuration(DurationUnit.MINUTES),
            startedAt = null,
            isEnded = false
        )
    }

    private fun createBuilds(builders: List<User>): List<Build> {
        return builders.map {
            Build(
                builder = it,
                hasPassedChallenge = false,
                correctAnswerBy = null
            )
        }
    }
}