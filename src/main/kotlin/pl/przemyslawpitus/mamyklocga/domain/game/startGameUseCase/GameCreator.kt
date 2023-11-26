package pl.przemyslawpitus.mamyklocga.domain.game.startGameUseCase

import pl.przemyslawpitus.mamyklocga.domain.user.User
import pl.przemyslawpitus.mamyklocga.domain.user.UserId
import pl.przemyslawpitus.mamyklocga.domain.game.Game
import pl.przemyslawpitus.mamyklocga.domain.game.RoundsCreator
import pl.przemyslawpitus.mamyklocga.domain.game.Word

const val WORDS_PER_ROUND_COUNT = 10

class GameCreator(
    private val roundsCreator: RoundsCreator,
    private val wordsProvider: WordsProvider,
) {
    fun createGame(users: Set<User>): Game {
        val rounds = roundsCreator.createRounds(users)

        return Game(
            roundsTotal = rounds.size,
            rounds = rounds,
            wordsPerUser = getWordsPerUser(users),
        )
    }


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