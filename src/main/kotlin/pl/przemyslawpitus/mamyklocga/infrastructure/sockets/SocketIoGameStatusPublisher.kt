package pl.przemyslawpitus.mamyklocga.infrastructure.sockets

import pl.przemyslawpitus.mamyklocga.WithLogger
import pl.przemyslawpitus.mamyklocga.domain.Room
import pl.przemyslawpitus.mamyklocga.domain.RoomId
import pl.przemyslawpitus.mamyklocga.domain.User
import pl.przemyslawpitus.mamyklocga.domain.startGameUseCase.GameStatusPublisher
import pl.przemyslawpitus.mamyklocga.infrastructure.sockets.SocketIoServer.Companion.LOBBY_ROOM

class SocketIoGameStatusPublisher(
    private val socketIoServer: SocketIoServer,
) : GameStatusPublisher {
    override fun gameStarted(room: Room) {
        val event = GameStartedEvent()

        socketIoServer.sendToRoom(
            socketioRoomId = room.roomId.value,
            event = event,
        )
    }

    override fun roundStarted(room: Room) {
        val event = RoundStartedEvent(roundNumber = room.game!!.currentRound!!.roundNumber)

        socketIoServer.sendToRoom(
            socketioRoomId = room.roomId.value,
            event = event,
        )
    }

    override fun roundEnded(room: Room) {
        TODO("Not yet implemented")
    }

    override fun guessesUpdate(room: Room) {
        TODO("Not yet implemented")
    }

    override fun gameEnded(room: Room) {
        TODO("Not yet implemented")
    }

    override fun newRoom(room: Room) {
        socketIoServer.sendToRoom(
            socketioRoomId = LOBBY_ROOM,
            NewRoomEvent(room = room),
        )
    }

    override fun roomClosed(room: Room) {
        TODO("Not yet implemented")
    }

    override fun joinRoom(roomId: RoomId, user: User) {
        socketIoServer.leaveRoom(
            socketioRoomId = LOBBY_ROOM,
            user = user,
        )
        socketIoServer.joinRoom(
            socketioRoomId = roomId.value,
            user = user,
        )
    }

    override fun leaveRoom(roomId: RoomId, user: User) {
        socketIoServer.leaveRoom(
            socketioRoomId = roomId.value,
            user = user,
        )
        socketIoServer.joinRoom(
            socketioRoomId = LOBBY_ROOM,
            user = user,
        )
    }

    private companion object : WithLogger()
}

class GameStartedEvent : Event<Unit>(
    name = "GAME_STARTED",
    payload = Unit,
)

class RoundStartedEvent(roundNumber: Int) : Event<RoundStartedPayload>(
    name = "ROUND_STARTED",
    payload = RoundStartedPayload(
        roundNumber = roundNumber,
    )
)

data class RoundStartedPayload(
    val roundNumber: Int,
)

class NewRoomEvent(room: Room) : Event<NewRoomPayload>(
    name = "NEW_ROOM",
    payload = NewRoomPayload(
        code = room.code,
        name = room.name,
        usersCount = room.users.size,
    )
)

data class NewRoomPayload(
    val code: String,
    val name: String,
    val usersCount: Int,
)