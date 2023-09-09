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
import pl.przemyslawpitus.mamyklocga.domain.user.UserId
import pl.przemyslawpitus.mamyklocga.domain.rooms.createRoomUseCase.CreateRoomUseCase
import pl.przemyslawpitus.mamyklocga.domain.rooms.Room
import pl.przemyslawpitus.mamyklocga.domain.user.User
import pl.przemyslawpitus.mamyklocga.domain.rooms.getRoomUseCase.GetRoomUseCase
import pl.przemyslawpitus.mamyklocga.domain.rooms.UserRoom
import pl.przemyslawpitus.mamyklocga.domain.rooms.getRoomsUseCase.GetRoomsUseCase
import pl.przemyslawpitus.mamyklocga.domain.rooms.joinRoomUseCase.JoinRoomUseCase

@RestController
@RequestMapping("/rooms")
class RoomsEndpoint(
    private val createRoomUseCase: CreateRoomUseCase,
    private val joinRoomUseCase: JoinRoomUseCase,
    private val getRoomsUseCase: GetRoomsUseCase,
    private val getRoomUseCase: GetRoomUseCase,
) {

    @GetMapping("")
    fun getPublicRooms(
        @CookieValue userId: String,
    ): ResponseEntity<*> {
        logger.info("Get list of public rooms, userId: $userId")
        val rooms = getRoomsUseCase.getPublicRooms()
        return ResponseEntity.ok().body(rooms.toRoomsResponse())
    }

    @PostMapping
    fun createRoom(
        @CookieValue userId: String,
        @RequestBody request: CreateRoomRequest,
    ): ResponseEntity<*> {
        logger.info("Create new room, userId: $userId, roomName: ${request.roomName}")
        val room = createRoomUseCase.createRoom(
            roomName = request.roomName,
            ownerUserId = UserId(userId),
        )
        return ResponseEntity.status(HttpStatus.CREATED).body(room.toRoomCreatedResponse())
    }

    @PostMapping("/{roomCode}/join")
    fun joinRoom(
        @CookieValue userId: String,
        @PathVariable roomCode: String,
    ): ResponseEntity<*> {
        logger.info("Join room, userId: $userId, roomCode: $roomCode")
        joinRoomUseCase.joinRoom(
            roomCode = roomCode,
            userId = UserId(userId),
        )

        return ResponseEntity.ok().body(Unit)
    }

    @GetMapping("/{roomCode}")
    fun getRoom(
        @CookieValue userId: String,
        @PathVariable roomCode: String,
    ): ResponseEntity<*> {
        logger.info("Get room, userId: $userId, roomCode: $roomCode")
        val userRoom = getRoomUseCase.getRoom(
            roomCode = roomCode,
            userId = UserId(userId),
        )

        return ResponseEntity.ok().body(userRoom.toGetRoomResponse())
    }

    private companion object : WithLogger()
}

data class CreateRoomRequest(
    val roomName: String,
)

data class RoomCreatedResponse(
    val code: String,
    val name: String,
)

private fun Room.toRoomCreatedResponse() = RoomCreatedResponse(
    code = this.code,
    name = this.name,
)

data class RoomsResponse(
    val rooms: List<RoomResponse>,
)

data class RoomResponse(
    val code: String,
    val name: String,
    val usersCount: Int,
)

private fun List<Room>.toRoomsResponse() = RoomsResponse(
    rooms = this.map { it.toRoomResponse() }
)

private fun Room.toRoomResponse() = RoomResponse(
    code = this.code,
    name = this.name,
    usersCount = this.users.size,
)

fun UserRoom.toGetRoomResponse() = GetRoomResponse(
    code = this.code,
    name = this.name,
    isRoomOwner = this.isRoomOwner,
    users = this.users.map { it.toGetRoomUser() },
    state = this.state.name,
    game = this.game?.toGetRoomGame(),
)

private fun User.toGetRoomUser() = GetRoomResponse.User(
    username = this.requiredUsername,
)

private fun UserRoom.UserGame.toGetRoomGame() = GetRoomResponse.Game(
    roundsTotal = this.roundsTotal,
    currentRound = this.currentRound.toGetRoomRound(),
    myPoints = this.myPoints,
    words = this.words,
)

private fun UserRoom.UserRound.toGetRoomRound() = GetRoomResponse.Round(
    roundNumber = this.roundNumber,
    users = this.users.map { it.toGetRoomRoundUser() },
    role = this.role.name,
    guesser = this.guesser.toGetRoomUser(),
    challenge = this.challenge.text,
    endsAt = this.endsAt?.toString(),
    state = this.state.name,
    hasRatedGuesserGuess = this.hasRatedGuesserGuess,
    hasRatedStolenGuess = this.hasRatedStolenGuess,
)

private fun UserRoom.RoundUser.toGetRoomRoundUser() = GetRoomResponse.RoundUser(
    userId = this.user.userId.value,
    username = this.user.requiredUsername,
    role = this.role.name,
    hasPassedChallenge = this.hasPassedChallenge,
    hasGuessedCorrectly = this.hasGuessedCorrectly,
)