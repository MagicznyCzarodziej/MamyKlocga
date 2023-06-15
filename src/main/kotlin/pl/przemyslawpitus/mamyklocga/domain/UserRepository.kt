package pl.przemyslawpitus.mamyklocga.domain

interface UserRepository {
    fun saveUser(user: User): User
    fun getByUserId(userId: UserId): User?
    fun getBySessionId(sessionId: SessionId): User?
}
