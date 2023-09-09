package pl.przemyslawpitus.mamyklocga.api.rooms

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CookieValue
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pl.przemyslawpitus.mamyklocga.WithLogger
import pl.przemyslawpitus.mamyklocga.domain.game.rateGuessUseCase.RateGuessUseCase
import pl.przemyslawpitus.mamyklocga.domain.user.UserId
import pl.przemyslawpitus.mamyklocga.domain.game.startGameUseCase.StartGameUseCase
import pl.przemyslawpitus.mamyklocga.domain.game.startRoundUseCase.StartRoundUseCase

@RestController
@RequestMapping("/rooms")
class GameEndpoint(
    private val startGameUseCase: StartGameUseCase,
    private val startRoundUseCase: StartRoundUseCase,
    private val rateGuessUseCase: RateGuessUseCase,
) {
    @PostMapping("/{roomCode}/start")
    fun startGame(
        @CookieValue userId: String,
        @PathVariable roomCode: String,
    ): ResponseEntity<*> {
        logger.info("Start game in room roomCode: $roomCode, userId: $userId")

        startGameUseCase.startGame(
            userId = UserId(userId),
            roomCode = roomCode,
        )

        return ResponseEntity.ok().body(Unit)
    }

    @PostMapping("/{roomCode}/startRound")
    fun startCurrentRound(
        @CookieValue userId: String,
        @PathVariable roomCode: String,
    ): ResponseEntity<*> {
        logger.info("Start current round in room roomCode: $roomCode, userId: $userId")

        startRoundUseCase.startRound(
            roomCode = roomCode,
            userId = UserId(userId),
        )

        return ResponseEntity.ok().body(Unit)
    }

    @PostMapping("/{roomCode}/rate-guess/{rate}")
    fun rateGuess(
        @CookieValue userId: String,
        @PathVariable roomCode: String,
        @PathVariable rate: String,
    ): ResponseEntity<*> {
        logger.info("Rate guess in room roomCode: $roomCode, userId: $userId")

        if (rate !in listOf("yes", "no")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Unit)
        }

        val hasGuesserGuessedCorrectly = rate == "yes"

        val room = rateGuessUseCase.rateGuess(
            roomCode = roomCode,
            userId = UserId(userId),
            hasGuesserGuessedCorrectly = hasGuesserGuessedCorrectly,
        )

        return ResponseEntity.ok().body(room.toGetRoomResponse())
    }

    private companion object : WithLogger()
}