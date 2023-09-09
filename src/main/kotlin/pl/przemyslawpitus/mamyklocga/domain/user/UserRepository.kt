package pl.przemyslawpitus.mamyklocga.domain.user

interface UserRepository {
    fun saveUser(user: User): User
    fun getByUserId(userId: UserId): User?
}
