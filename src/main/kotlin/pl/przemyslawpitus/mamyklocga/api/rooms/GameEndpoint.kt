package pl.przemyslawpitus.mamyklocga.api.rooms

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CookieValue
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pl.przemyslawpitus.mamyklocga.WithLogger
import pl.przemyslawpitus.mamyklocga.domain.game.endGameUseCase.EndGameUseCase
import pl.przemyslawpitus.mamyklocga.domain.game.getPointsUseCase.GetPointsUseCase
import pl.przemyslawpitus.mamyklocga.domain.game.getPointsUseCase.UserPoints
import pl.przemyslawpitus.mamyklocga.domain.game.nextRoundUseCase.NextRoundUseCase
import pl.przemyslawpitus.mamyklocga.domain.game.rateChallengeUseCase.RateChallengeUseCase
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
    private val rateChallengeUseCase: RateChallengeUseCase,
    private val nextRoundUseCase: NextRoundUseCase,
    private val getPointsUseCase: GetPointsUseCase,
    private val endGameUseCase: EndGameUseCase,
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

        return ResponseEntity.ok().build<Unit>()
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

        return ResponseEntity.ok().build<Unit>()
    }

    @PostMapping("/{roomCode}/nextRound")
    fun nextRound(
        @CookieValue userId: String,
        @PathVariable roomCode: String,
    ): ResponseEntity<*> {
        logger.info("Proceed to the next round in room roomCode: $roomCode, userId: $userId")

        nextRoundUseCase.nextRound(
            roomCode = roomCode,
            userId = UserId(userId),
        )

        return ResponseEntity.ok().build<Unit>()
    }

    @PostMapping("/{roomCode}/guess")
    fun rateGuess(
        @CookieValue userId: String,
        @PathVariable roomCode: String,
        @RequestBody request: GuessRequest,
    ): ResponseEntity<*> {
        logger.info("Rate guess in room roomCode: $roomCode, userId: $userId")

        val room = rateGuessUseCase.rateGuess(
            roomCode = roomCode,
            userId = UserId(userId),
            ratedUserId = request.ratedUserId?.let { UserId(it) },
            hasGuessedCorrectly = request.hasGuessedCorrectly,
        )

        return ResponseEntity.ok().body(room.toGetRoomResponse()) // TODO Return nothing - client is watching the room
    }

    @PostMapping("/{roomCode}/challenge/{ratedUserId}/{rate}")
    fun rateChallenge(
        @CookieValue userId: String,
        @PathVariable roomCode: String,
        @PathVariable ratedUserId: String,
        @PathVariable rate: String,
    ): ResponseEntity<*> {
        logger.info("Rate challenge in room roomCode: $roomCode, userId: $userId")

        if (rate !in listOf("yes", "no")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build<Unit>()
        }

        val hasPassedChallenge = rate == "yes"

        rateChallengeUseCase.rateChallenge(
            roomCode = roomCode,
            userId = UserId(userId),
            ratedUserId = UserId(ratedUserId),
            hasPassedChallenge = hasPassedChallenge,
        )

        return ResponseEntity.ok().build<Unit>()
    }

    @PostMapping("/{roomCode}/end-game")
    fun getPoints(
        @CookieValue userId: String,
        @PathVariable roomCode: String,
    ): ResponseEntity<*> {
        logger.info("End game for room roomCode: $roomCode, userId: $userId")

        endGameUseCase.endGame(
            roomCode = roomCode,
            userId = UserId(userId),
        )

        return ResponseEntity.ok().build<Unit>()
    }

    @GetMapping("/{roomCode}/points")
    fun endGame(
        @CookieValue userId: String,
        @PathVariable roomCode: String,
    ): ResponseEntity<*> {
        logger.info("Get points for room roomCode: $roomCode, userId: $userId")

        val points = getPointsUseCase.getPoints(
            roomCode = roomCode,
            userId = UserId(userId),
        )

        return ResponseEntity.ok().body(points.toResponse())
    }

    private companion object : WithLogger()
}

data class PunctuationResponse(
    val pointsPerUser: List<UserPoints>,
) {
    data class UserPoints(
        val userId: String,
        val username: String,
        val points: Int,
    )
}

private fun List<UserPoints>.toResponse() = PunctuationResponse(
    pointsPerUser = this.map {
        PunctuationResponse.UserPoints(
            userId = it.user.userId.value,
            username = it.user.requiredUsername,
            points = it.points,
        )
    }
)

data class GuessRequest(
    val ratedUserId: String?,
    val hasGuessedCorrectly: Boolean,
)