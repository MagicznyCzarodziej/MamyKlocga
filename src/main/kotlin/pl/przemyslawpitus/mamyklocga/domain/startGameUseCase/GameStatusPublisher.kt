package pl.przemyslawpitus.mamyklocga.domain.startGameUseCase

import pl.przemyslawpitus.mamyklocga.domain.Room
import pl.przemyslawpitus.mamyklocga.domain.RoomId
import pl.przemyslawpitus.mamyklocga.domain.User

interface GameStatusPublisher {
    fun gameStarted(room: Room)
    fun roundStarted(room: Room)
    fun roundEnded(room: Room)
    fun guessesUpdate(room: Room)
    fun gameEnded(room: Room)

    fun joinRoom(roomId: RoomId, user: User)
    fun leaveRoom(roomId: RoomId, user: User)
}
