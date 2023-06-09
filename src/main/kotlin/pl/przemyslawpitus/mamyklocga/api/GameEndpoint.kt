package pl.przemyslawpitus.mamyklocga.api

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CookieValue
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pl.przemyslawpitus.mamyklocga.WithLogger
import pl.przemyslawpitus.mamyklocga.domain.RoomId
import pl.przemyslawpitus.mamyklocga.domain.UserId
import pl.przemyslawpitus.mamyklocga.domain.startGameUseCase.StartGameUseCase
import pl.przemyslawpitus.mamyklocga.domain.startRoundUseCase.StartRoundUseCase

@RestController
@RequestMapping("/rooms")
class GameEndpoint(
    private val startGameUseCase: StartGameUseCase,
    private val startRoundUseCase: StartRoundUseCase,
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

    private companion object : WithLogger()
}