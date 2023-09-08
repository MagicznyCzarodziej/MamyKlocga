package pl.przemyslawpitus.mamyklocga.api

import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CookieValue
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pl.przemyslawpitus.mamyklocga.WithLogger
import pl.przemyslawpitus.mamyklocga.domain.user.User
import pl.przemyslawpitus.mamyklocga.domain.user.UserId
import pl.przemyslawpitus.mamyklocga.domain.user.helloUseCase.HelloUseCase
import pl.przemyslawpitus.mamyklocga.domain.user.setUsernameUseCase.SetUsernameUseCase

@RestController
@RequestMapping("/me")
class UserEndpoint(
    private val helloUseCase: HelloUseCase,
    private val setUsernameUseCase: SetUsernameUseCase,
) {

    @PostMapping("/hello")
    fun hello(
        @CookieValue userId: String?,
        response: HttpServletResponse,
    ): ResponseEntity<*> {
        logger.info("Hello, userId: $userId")

        val result = helloUseCase.getOrCreateUser(
            userId = userId,
        )

        val userIdCookie = createUserIdCookie(userId = result.user.userId)
        response.addCookie(userIdCookie)

        val status = if (result.isNewUser) HttpStatus.CREATED else HttpStatus.OK

        return ResponseEntity
            .status(status)
            .body((result.user.toResponse()))
    }

    @PostMapping("/username")
    fun setUsername(
        @CookieValue userId: String,
        @RequestBody request: SetUsernameRequest,
    ): ResponseEntity<*> {
        logger.info("Set username $userId: ${request.username}")

        val user = setUsernameUseCase.setUsername(
            userId = UserId(userId),
            username = request.username
        )

        return ResponseEntity.ok().body(
            SetUsernameResponse(username = user.username!!)
        )
    }

    private fun createUserIdCookie(userId: UserId): Cookie {
        val cookie = Cookie(USER_ID_COOKIE_NAME, userId.value)
        cookie.path = "/"
        cookie.isHttpOnly = true
        return cookie
    }

    private companion object : WithLogger() {
        const val USER_ID_COOKIE_NAME = "userId"
    }
}

data class UserResponse(
    val userId: String,
    val username: String?,
)

private fun User.toResponse() = UserResponse(
    userId = this.userId.value,
    username = this.username,
)

data class SetUsernameRequest(
    val username: String,
)

data class SetUsernameResponse(
    val username: String,
)