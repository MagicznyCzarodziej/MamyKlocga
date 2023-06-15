package pl.przemyslawpitus.mamyklocga.domain.bindSessionToUserUseCase

import pl.przemyslawpitus.mamyklocga.domain.SessionId
import pl.przemyslawpitus.mamyklocga.domain.UserId

interface UserSessionBinder {
    fun bindSessionIdToUser(handler: (userId: UserId, sessionId: SessionId) -> Unit)
}