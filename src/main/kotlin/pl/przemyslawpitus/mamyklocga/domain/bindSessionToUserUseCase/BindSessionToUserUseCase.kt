package pl.przemyslawpitus.mamyklocga.domain.bindSessionToUserUseCase

import pl.przemyslawpitus.mamyklocga.WithLogger
import pl.przemyslawpitus.mamyklocga.domain.RoomRepository
import pl.przemyslawpitus.mamyklocga.domain.Session
import pl.przemyslawpitus.mamyklocga.domain.SessionId
import pl.przemyslawpitus.mamyklocga.domain.User
import pl.przemyslawpitus.mamyklocga.domain.UserRepository
import pl.przemyslawpitus.mamyklocga.domain.startGameUseCase.GameStatusPublisher
import java.time.Instant

class BindSessionToUserUseCase(
    private val userRepository: UserRepository,
    private val userSessionBinder: UserSessionBinder,
    private val roomRepository: RoomRepository,
    private val gameStatusPublisher: GameStatusPublisher,

    ) {
    init {
        createBinding()
    }

    private fun createBinding() {
        userSessionBinder.bindSessionIdToUser { userId, sessionId ->
            val user = userRepository.getByUserId(userId)
            if (user == null) throw RuntimeException("User with id ${userId.value} not found")

            val updatedUser = user.updateSessionId(sessionId)

            rejoinRoom(updatedUser)

            userRepository.saveUser(updatedUser)
            logger.info("Updated sessionId to ${sessionId.value} for user ${updatedUser.userId}")
        }
    }

    private fun rejoinRoom(user: User) {
        val userRoom = roomRepository.getByUserId(userId = user.userId).firstOrNull()
        if (userRoom != null) {
            gameStatusPublisher.joinRoom(
                roomId = userRoom.roomId,
                user = user
            )
        }
    }

    private companion object : WithLogger()
}

private fun User.updateSessionId(sessionId: SessionId) =
    this.copy(
        session = Session(
            sessionId = sessionId,
        ),
        updatedAt = Instant.now(),
    )
