package pl.przemyslawpitus.mamyklocga.domain.startGameUseCase

import pl.przemyslawpitus.mamyklocga.domain.game.Word
import java.util.UUID

class WordsProvider {
    private val words = rawWords.map {
        Word(
            wordId = UUID.randomUUID().toString(),
            text = it,
        )
    }

    fun getWords(): List<Word> = words
}