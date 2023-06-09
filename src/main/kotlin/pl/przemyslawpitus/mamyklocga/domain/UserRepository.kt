package pl.przemyslawpitus.mamyklocga.domain

import java.util.UUID

interface UserRepository {
    fun saveUser(user: User): User
    fun getByUserId(userId: UserId): User?
    fun getUserByClientSessionId(clientSessionId: UUID): User?
}
