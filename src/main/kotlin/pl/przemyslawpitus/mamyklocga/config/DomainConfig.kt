package pl.przemyslawpitus.mamyklocga.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import pl.przemyslawpitus.mamyklocga.domain.UserRepository
import pl.przemyslawpitus.mamyklocga.domain.createRoomUseCase.CreateRoomUseCase
import pl.przemyslawpitus.mamyklocga.domain.RoomRepository
import pl.przemyslawpitus.mamyklocga.domain.bindSessionToUserUseCase.BindSessionToUserUseCase
import pl.przemyslawpitus.mamyklocga.domain.bindSessionToUserUseCase.UserSessionBinder
import pl.przemyslawpitus.mamyklocga.domain.game.ChallengeProvider
import pl.przemyslawpitus.mamyklocga.domain.getRoomsUseCase.GetRoomsUseCase
import pl.przemyslawpitus.mamyklocga.domain.helloUseCase.HelloUseCase
import pl.przemyslawpitus.mamyklocga.domain.joinRoomUseCase.JoinRoomUseCase
import pl.przemyslawpitus.mamyklocga.domain.leaveRoomUseCase.LeaveRoomUseCase
import pl.przemyslawpitus.mamyklocga.domain.setUsernameUseCase.SetUsernameUseCase
import pl.przemyslawpitus.mamyklocga.domain.startGameUseCase.GameCreator
import pl.przemyslawpitus.mamyklocga.domain.startGameUseCase.GameStatusPublisher
import pl.przemyslawpitus.mamyklocga.domain.startGameUseCase.StartGameUseCase
import pl.przemyslawpitus.mamyklocga.infrastructure.InMemoryRoomRepository
import pl.przemyslawpitus.mamyklocga.infrastructure.InMemoryUserRepository
import pl.przemyslawpitus.mamyklocga.infrastructure.sockets.SocketIoGameStatusPublisher
import pl.przemyslawpitus.mamyklocga.infrastructure.sockets.SocketIoServer

@Configuration
@Suppress("TooManyFunctions")
class DomainConfig {
    @Bean
    fun userRepository() = InMemoryUserRepository()

    @Bean
    fun roomRepository() = InMemoryRoomRepository()

    @Bean
    fun helloUseCase(
        userRepository: UserRepository,
    ) = HelloUseCase(
        userRepository = userRepository,
    )

    @Bean
    fun setUsernameUseCase(
        userRepository: UserRepository,
    ) = SetUsernameUseCase(
        userRepository = userRepository,
    )

    @Bean
    fun getRoomsUseCase(
        roomRoomRepository: RoomRepository,
    ) = GetRoomsUseCase(
        roomRepository = roomRoomRepository,
    )

    @Bean
    fun createRoomUseCase(
        roomRepository: RoomRepository,
        userRepository: UserRepository,
        leaveRoomUseCase: LeaveRoomUseCase,
        gameStatusPublisher: GameStatusPublisher,
    ) = CreateRoomUseCase(
        roomRepository = roomRepository,
        userRepository = userRepository,
        leaveRoomUseCase = leaveRoomUseCase,
        gameStatusPublisher = gameStatusPublisher,
    )

    @Bean
    fun joinRoomUseCase(
        roomRepository: RoomRepository,
        userRepository: UserRepository,
        leaveRoomUseCase: LeaveRoomUseCase,
        gameStatusPublisher: GameStatusPublisher,
    ) = JoinRoomUseCase(
        roomRepository = roomRepository,
        userRepository = userRepository,
        leaveRoomUseCase = leaveRoomUseCase,
        gameStatusPublisher = gameStatusPublisher,
    )

    @Bean
    fun leaveRoomUseCase(
        roomRepository: RoomRepository,
        userRepository: UserRepository,
        gameStatusPublisher: GameStatusPublisher,
    ) = LeaveRoomUseCase(
        roomRepository = roomRepository,
        userRepository = userRepository,
        gameStatusPublisher = gameStatusPublisher,
    )

    @Bean
    fun startGameUseCase(
        roomRepository: RoomRepository,
        gameCreator: GameCreator,
        gameStatusPublisher: GameStatusPublisher,
    ) = StartGameUseCase(
        roomRepository = roomRepository,
        gameCreator = gameCreator,
        gameStatusPublisher = gameStatusPublisher,
    )

    @Bean
    fun challengeProvider() = ChallengeProvider()

    @Bean
    fun gameCreator(
        challengeProvider: ChallengeProvider,
    ) = GameCreator(
        challengeProvider = challengeProvider,
    )

    @Bean
    fun gameStatusPublisher(
        socketIoServer: SocketIoServer,
    ) = SocketIoGameStatusPublisher(
        socketIoServer = socketIoServer,
    )

    @Bean
    fun bindSessionToUserUseCase(
        userRepository: UserRepository,
        roomRepository: RoomRepository,
        userSessionBinder: UserSessionBinder,
        gameStatusPublisher: GameStatusPublisher,
    ) = BindSessionToUserUseCase(
        userRepository = userRepository,
        roomRepository = roomRepository,
        userSessionBinder = userSessionBinder,
        gameStatusPublisher = gameStatusPublisher,
    )
}
