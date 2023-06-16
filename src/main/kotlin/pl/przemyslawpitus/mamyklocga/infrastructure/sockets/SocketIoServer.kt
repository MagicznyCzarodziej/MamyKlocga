package pl.przemyslawpitus.mamyklocga.infrastructure.sockets

import com.corundumstudio.socketio.Configuration
import com.corundumstudio.socketio.SocketIOServer
import com.corundumstudio.socketio.protocol.JacksonJsonSupport
import com.fasterxml.jackson.module.kotlin.kotlinModule
import jakarta.annotation.PreDestroy
import org.springframework.core.env.Environment
import org.springframework.core.io.ClassPathResource
import pl.przemyslawpitus.mamyklocga.WithLogger
import pl.przemyslawpitus.mamyklocga.config.SocketIoProperties
import pl.przemyslawpitus.mamyklocga.domain.SessionId
import pl.przemyslawpitus.mamyklocga.domain.User
import pl.przemyslawpitus.mamyklocga.domain.UserId
import pl.przemyslawpitus.mamyklocga.domain.bindSessionToUserUseCase.UserSessionBinder
import java.net.BindException

class SocketIoServer(
    private val socketIoProperties: SocketIoProperties,
    private val environment: Environment
) : UserSessionBinder {
    private var server: SocketIOServer;

    init {
        val config = Configuration()
        config.port = socketIoProperties.port
        config.jsonSupport = JacksonJsonSupport(kotlinModule())
        config.isRandomSession = true // Treat every browser tab as different client

        if (shouldUseHttps()) {
            config.keyStore = ClassPathResource(socketIoProperties.keyStoreResourcePath!!).inputStream
            config.keyStorePassword = socketIoProperties.keyStorePassword!!
        }

        server = SocketIOServer(config)

        try {
            server.start()
        } catch (exception: BindException) {
            logger.error("Cannot start SocketIO Server", exception)
        }
    }

    fun joinRoom(socketioRoomId: String, user: User) {
        if (user.session == null) {
            throw RuntimeException("User has no active socket-io session")
        }

        server.getClient(user.session.sessionId.asUUID()).joinRoom(socketioRoomId)

        logger.info("User ${user.userId.value} joined socket-io room for room $socketioRoomId")
    }

    fun leaveRoom(socketioRoomId: String, user: User) {
        if (user.session == null) {
            throw RuntimeException("User has no active socket-io session")
        }

        server.getClient(user.session.sessionId.asUUID()).leaveRoom(socketioRoomId)

        logger.info("User ${user.userId.value} left socket-io room for room $socketioRoomId")
    }

    fun <T> sendToRoom(socketioRoomId: String, event: Event<T>) {
        server.getRoomOperations(socketioRoomId)
            .sendEvent(event.name, event.payload)
        logger.info("Sent event ${event.name} to room $socketioRoomId")
    }

    override fun bindSessionIdToUser(handler: (userId: UserId, sessionId: SessionId) -> Unit) {
        server.addConnectListener { client ->
            val sessionId = SessionId(client.sessionId.toString())
            val userId = client.handshakeData.getSingleUrlParam("userId")?.let { UserId(it) }
                ?: throw RuntimeException("No userId passed on socket connection, sessionId: ${sessionId.value}")

            client.joinRoom(LOBBY_ROOM)
            logger.info("Socket connected, userId: ${userId.value}, sessionId: ${sessionId.value}")

            handler(userId, sessionId)
        }
    }

    private fun shouldUseHttps(): Boolean {
        return ("prod" in environment.activeProfiles)
    }

    @PreDestroy
    @Suppress("UnusedPrivateMember")
    private fun destroy() {
        logger.debug("Stopping SocketIO server")
        server.stop()
    }

    companion object : WithLogger() {
        const val LOBBY_ROOM = "LOBBY"
    }
}

open class Event<T>(
    val name: String,
    val payload: T,
)
