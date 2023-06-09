package pl.przemyslawpitus.mamyklocga.infrastructure.sockets

import com.corundumstudio.socketio.Configuration
import com.corundumstudio.socketio.SocketIOClient
import com.corundumstudio.socketio.SocketIOServer
import com.corundumstudio.socketio.annotation.OnConnect
import com.corundumstudio.socketio.protocol.JacksonJsonSupport
import com.fasterxml.jackson.module.kotlin.kotlinModule
import jakarta.annotation.PreDestroy
import org.springframework.core.io.ClassPathResource
import pl.przemyslawpitus.mamyklocga.WithLogger
import pl.przemyslawpitus.mamyklocga.config.SocketIoProperties
import pl.przemyslawpitus.mamyklocga.domain.RoomId
import pl.przemyslawpitus.mamyklocga.domain.User
import pl.przemyslawpitus.mamyklocga.domain.UserRepository
import kotlin.reflect.KClass

class SocketIoServer(
    socketIoProperties: SocketIoProperties,
    private val userRepository: UserRepository,
) {
    private var server: SocketIOServer;

    init {
        val config = Configuration()
        config.port = socketIoProperties.port
        config.keyStore = ClassPathResource(socketIoProperties.keyStoreResourcePath).inputStream
        config.keyStorePassword = socketIoProperties.keyStorePassword
        config.jsonSupport = JacksonJsonSupport(kotlinModule())
        config.isRandomSession = true // Treat every browser tab as different client

        server = SocketIOServer(config)

        server.start()
    }

    fun <T : Any> listenFor(
        eventName: String,
        payloadClass: KClass<T>,
        handler: (user: User, payload: T) -> Unit
    ) {
        server.addEventListener(
            eventName,
            payloadClass.java
        ) { client, data, ackRequest ->
            val user = userRepository.getUserByClientSessionId(clientSessionId = client.sessionId)

            if (user == null) {
                logger.warn(
                    "Received event [$eventName] from unregistered user" +
                        " with clientSessionId: [${client.sessionId}]. Payload: $data"
                )
                handleUserNotFound(client = client)
            } else {
                logger.info("Received event [$eventName] from user with id [${user.userId.value}]. Payload: $data")
                handler(user, data)
            }
        }
    }

    fun joinRoom(roomId: RoomId, user: User) {
        if (user.session == null) {
            throw RuntimeException("User has no active socket-io session")
        }

        server.getClient(user.session.clientSessionId).joinRoom(roomId.value)

        logger.info("User ${user.userId.value} joined socket-io room for room ${roomId.value}")
    }

    fun leaveRoom(roomId: RoomId, user: User) {
        if (user.session == null) {
            throw RuntimeException("User has no active socket-io session")
        }

        server.getClient(user.session.clientSessionId).leaveRoom(roomId.value)

        logger.info("User ${user.userId.value} left socket-io room for room ${roomId.value}")
    }

    fun <T> sendToRoom(roomId: RoomId, event: Event<T>) {
        server.getRoomOperations(roomId.value)
            .sendEvent(event.name, event.payload)
        logger.info("Sent event ${event.name} to room ${roomId.value}")
    }

    fun <T> sendToAll(event: Event<T>) {
        server.broadcastOperations.sendEvent(event.name, event.payload)
    }

    fun <T> sendToUser(user: User, event: Event<T>) {
        server.getClient(user.session!!.clientSessionId).sendEvent(event.name, event.payload) // TODO: Remove "!!"
    }

    private fun handleUserNotFound(client: SocketIOClient) {
        client.sendEvent("ERROR", "User not found") // TODO: Use classes
    }

    @PreDestroy
    @Suppress("UnusedPrivateMember")
    private fun destroy() {
        logger.debug("Stopping SocketIO server")
        server.stop()
    }

    private companion object : WithLogger()
}

open class Event<T>(
    val name: String,
    val payload: T,
)
