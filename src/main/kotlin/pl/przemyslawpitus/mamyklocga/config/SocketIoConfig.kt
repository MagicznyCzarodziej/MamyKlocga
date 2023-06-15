package pl.przemyslawpitus.mamyklocga.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import pl.przemyslawpitus.mamyklocga.domain.bindSessionToUserUseCase.BindSessionToUserUseCase
import pl.przemyslawpitus.mamyklocga.infrastructure.sockets.SocketIoServer

@Configuration
@EnableConfigurationProperties(SocketIoProperties::class)
class SocketIoConfig {
    @Bean
    fun socketIoServer(
        socketIoProperties: SocketIoProperties,
        environment: Environment,
    ) = SocketIoServer(
        socketIoProperties = socketIoProperties,
        environment = environment,
    )
}

@ConfigurationProperties("socket-io")
data class SocketIoProperties(
    val port: Int,
    val keyStoreResourcePath: String?,
    val keyStorePassword: String?,
)
