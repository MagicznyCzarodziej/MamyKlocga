package pl.przemyslawpitus.mamyklocga.rounds.utils

import pl.przemyslawpitus.mamyklocga.domain.user.User
import pl.przemyslawpitus.mamyklocga.domain.user.UserId
import java.util.UUID

fun randomId() = UUID.randomUUID().toString()

fun randomUserId() = UserId("userId-${randomId()}")
fun randomUser() = User(randomUserId())