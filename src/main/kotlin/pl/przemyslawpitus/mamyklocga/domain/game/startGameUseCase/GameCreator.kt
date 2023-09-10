package pl.przemyslawpitus.mamyklocga.domain.game.startGameUseCase

import pl.przemyslawpitus.mamyklocga.domain.user.User
import pl.przemyslawpitus.mamyklocga.domain.user.UserId
import pl.przemyslawpitus.mamyklocga.domain.game.Build
import pl.przemyslawpitus.mamyklocga.domain.game.ChallengeProvider
import pl.przemyslawpitus.mamyklocga.domain.game.Game
import pl.przemyslawpitus.mamyklocga.domain.game.Round
import pl.przemyslawpitus.mamyklocga.domain.game.RoundCreator
import pl.przemyslawpitus.mamyklocga.domain.game.Word
import kotlin.time.DurationUnit
import kotlin.time.toDuration

const val WORDS_PER_ROUND_COUNT = 10

class GameCreator(
    private val roundCreator: RoundCreator,
    private val wordsProvider: WordsProvider,
) {
    fun createGame(users: Set<User>): Game {
        val round = roundCreator.createRound(users)

        return Game(
            roundsTotal = getTotalRounds(users),
            currentRound = round,
            pastRounds = listOf(round),
            wordsPerUser = getWordsPerUser(users),
        )
    }

    private fun getTotalRounds(users: Set<User>): Int = 2
//        if (users.size > 3) {
//            users.size
//        } else {
//            users.size * 2
//        }

    private fun getWordsPerUser(users: Set<User>): Map<UserId, List<Word>> {
        val words = wordsProvider.getWords()
            .shuffled()
            .chunked(WORDS_PER_ROUND_COUNT)

        return users
            .map { it.userId }
            .zip(words)
            .toMap()
    }
}