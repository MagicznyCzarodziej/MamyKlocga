package pl.przemyslawpitus.mamyklocga.infrastructure.sockets

import pl.przemyslawpitus.mamyklocga.WithLogger
import pl.przemyslawpitus.mamyklocga.domain.Room
import pl.przemyslawpitus.mamyklocga.domain.RoomId
import pl.przemyslawpitus.mamyklocga.domain.User
import pl.przemyslawpitus.mamyklocga.domain.startGameUseCase.GameStatusPublisher

class SocketIoGameStatusPublisher(
    private val socketIoServer: SocketIoServer,
) : GameStatusPublisher {
    override fun gameStarted(room: Room) {
        val event = GameStartedEvent()

        socketIoServer.sendToRoom(
            roomId = room.roomId,
            event = event,
        )
    }

    override fun roundStarted(room: Room) {
        val event = RoundStartedEvent(roundNumber = room.game!!.currentRound!!.roundNumber)

        socketIoServer.sendToRoom(
            roomId = room.roomId,
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

    override fun joinRoom(roomId: RoomId, user: User) {
        socketIoServer.joinRoom(roomId = roomId, user = user)
    }

    override fun leaveRoom(roomId: RoomId, user: User) {
        socketIoServer.leaveRoom(roomId = roomId, user = user)
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