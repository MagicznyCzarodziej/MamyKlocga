package pl.przemyslawpitus.mamyklocga.api

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
import pl.przemyslawpitus.mamyklocga.domain.UserId
import pl.przemyslawpitus.mamyklocga.domain.createRoomUseCase.CreateRoomUseCase
import pl.przemyslawpitus.mamyklocga.domain.Room
import pl.przemyslawpitus.mamyklocga.domain.RoomId
import pl.przemyslawpitus.mamyklocga.domain.getRoomsUseCase.GetRoomsUseCase
import pl.przemyslawpitus.mamyklocga.domain.joinRoomUseCase.JoinRoomUseCase

@RestController
@RequestMapping("/rooms")
class RoomsEndpoint(
    private val createRoomUseCase: CreateRoomUseCase,
    private val joinRoomUseCase: JoinRoomUseCase,
    private val getRoomsUseCase: GetRoomsUseCase,
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

    @PostMapping("/{roomId}/join")
    fun joinRoom(
        @CookieValue userId: String,
        @PathVariable roomId: String,
        @RequestBody request: JoinRoomRequest,
    ): ResponseEntity<*> {
        logger.info("Join room, userId: $userId, roomId: $roomId")
        joinRoomUseCase.joinRoom(
            roomId = RoomId(roomId),
            userId = UserId(userId),
        )

        return ResponseEntity.ok().body(Unit)
    }

    private companion object : WithLogger()
}

data class CreateRoomRequest(
    val roomName: String,
)

data class RoomCreatedResponse(
    val roomId: String,
    val code: String,
    val name: String,
)

private fun Room.toRoomCreatedResponse() = RoomCreatedResponse(
    roomId = this.roomId.value,
    code = this.code,
    name = this.name,
)

class JoinRoomRequest()

data class RoomsResponse(
    val rooms: List<RoomResponse>,
)

data class RoomResponse(
    val roomId: String,
    val code: String,
    val name: String,
    val usersCount: Int,
)

private fun List<Room>.toRoomsResponse() = RoomsResponse(
    rooms = this.map { it.toRoomResponse() }
)

private fun Room.toRoomResponse() = RoomResponse(
    roomId = this.roomId.value,
    code = this.code,
    name = this.name,
    usersCount = this.users.size,
)
