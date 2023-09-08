package pl.przemyslawpitus.mamyklocga.domain.game.startGameUseCase

import pl.przemyslawpitus.mamyklocga.domain.rooms.Room
import pl.przemyslawpitus.mamyklocga.domain.rooms.RoomId
import pl.przemyslawpitus.mamyklocga.domain.user.User

interface GameStatusPublisher {
    fun gameStarted(room: Room)
    fun roundStarted(room: Room)
    fun roundEnded(room: Room)
    fun guessesUpdate(room: Room)
    fun gameEnded(room: Room)

    fun newRoom(room: Room)
    fun roomClosed(room: Room)

    fun joinRoom(roomId: RoomId, user: User)
    fun leaveRoom(roomId: RoomId, user: User)
}

class NoOpGameStatusPublisher(): GameStatusPublisher {
    override fun gameStarted(room: Room) {

    }

    override fun roundStarted(room: Room) {
    }

    override fun roundEnded(room: Room) {
    }

    override fun guessesUpdate(room: Room) {
    }

    override fun gameEnded(room: Room) {
    }

    override fun newRoom(room: Room) {
    }

    override fun roomClosed(room: Room) {
    }

    override fun joinRoom(roomId: RoomId, user: User) {
    }

    override fun leaveRoom(roomId: RoomId, user: User) {
    }

}