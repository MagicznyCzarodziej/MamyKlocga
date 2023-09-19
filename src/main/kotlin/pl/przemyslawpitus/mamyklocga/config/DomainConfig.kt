package pl.przemyslawpitus.mamyklocga.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import pl.przemyslawpitus.mamyklocga.api.rooms.DeferredResultRoomStatusController
import pl.przemyslawpitus.mamyklocga.domain.user.UserRepository
import pl.przemyslawpitus.mamyklocga.domain.rooms.RoomRepository
import pl.przemyslawpitus.mamyklocga.domain.rooms.RoomStatusController
import pl.przemyslawpitus.mamyklocga.domain.game.endRoundsUseCase.EndRoundsUseCase
import pl.przemyslawpitus.mamyklocga.domain.game.ChallengeProvider
import pl.przemyslawpitus.mamyklocga.domain.game.PointsCounter
import pl.przemyslawpitus.mamyklocga.domain.game.RoundCreator
import pl.przemyslawpitus.mamyklocga.domain.game.endGameUseCase.EndGameUseCase
import pl.przemyslawpitus.mamyklocga.domain.game.getPointsUseCase.GetPointsUseCase
import pl.przemyslawpitus.mamyklocga.domain.game.nextRoundUseCase.NextRoundUseCase
import pl.przemyslawpitus.mamyklocga.domain.game.rateGuessUseCase.RateGuessUseCase
import pl.przemyslawpitus.mamyklocga.domain.rooms.getRoomUseCase.GetRoomUseCase
import pl.przemyslawpitus.mamyklocga.domain.user.helloUseCase.HelloUseCase
import pl.przemyslawpitus.mamyklocga.domain.rooms.joinRoomUseCase.JoinRoomUseCase
import pl.przemyslawpitus.mamyklocga.domain.rooms.leaveRoomUseCase.LeaveRoomUseCase
import pl.przemyslawpitus.mamyklocga.domain.rooms.RoomsListWatchingManager
import pl.przemyslawpitus.mamyklocga.domain.user.setUsernameUseCase.SetUsernameUseCase
import pl.przemyslawpitus.mamyklocga.domain.game.startGameUseCase.GameCreator
import pl.przemyslawpitus.mamyklocga.domain.game.startGameUseCase.StartGameUseCase
import pl.przemyslawpitus.mamyklocga.domain.game.startGameUseCase.WordsProvider
import pl.przemyslawpitus.mamyklocga.domain.game.startRoundUseCase.StartRoundUseCase
import pl.przemyslawpitus.mamyklocga.domain.rooms.RoomWatchingManager
import pl.przemyslawpitus.mamyklocga.domain.rooms.createRoomUseCase.CreateRoomUseCase
import pl.przemyslawpitus.mamyklocga.domain.rooms.getRoomUseCase.RoomToUserRoomMapper
import pl.przemyslawpitus.mamyklocga.domain.rooms.getRoomsUseCase.GetRoomsUseCase
import pl.przemyslawpitus.mamyklocga.domain.rooms.updateRoomsListUseCase.UpdateRoomsListUseCase
import pl.przemyslawpitus.mamyklocga.infrastructure.InMemoryRoomRepository
import pl.przemyslawpitus.mamyklocga.infrastructure.InMemoryRoomWatchingManager
import pl.przemyslawpitus.mamyklocga.infrastructure.InMemoryRoomsWatchingManager
import pl.przemyslawpitus.mamyklocga.infrastructure.InMemoryUserRepository

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
    fun pointsCounter() = PointsCounter()

    @Bean
    fun getRoomUseCase(
        roomRoomRepository: RoomRepository,
        userRepository: UserRepository,
        pointsCounter: PointsCounter,
        roomToUserRoomMapper: RoomToUserRoomMapper
    ) = GetRoomUseCase(
        roomRepository = roomRoomRepository,
        userRepository = userRepository,
        roomToUserRoomMapper = roomToUserRoomMapper,
    )

    @Bean
    fun createRoomUseCase(
        roomRepository: RoomRepository,
        userRepository: UserRepository,
        leaveRoomUseCase: LeaveRoomUseCase,
        roomWatchingManager: RoomWatchingManager,
    ) = CreateRoomUseCase(
        roomRepository = roomRepository,
        userRepository = userRepository,
        leaveRoomUseCase = leaveRoomUseCase,
        roomWatchingManager = roomWatchingManager,
    )

    @Bean
    fun joinRoomUseCase(
        roomRepository: RoomRepository,
        userRepository: UserRepository,
        leaveRoomUseCase: LeaveRoomUseCase,
        roomWatchingManager: RoomWatchingManager,
    ) = JoinRoomUseCase(
        roomRepository = roomRepository,
        userRepository = userRepository,
        leaveRoomUseCase = leaveRoomUseCase,
        roomWatchingManager = roomWatchingManager,
    )

    @Bean
    fun leaveRoomUseCase(
        roomRepository: RoomRepository,
        userRepository: UserRepository,
        roomWatchingManager: RoomWatchingManager,
    ) = LeaveRoomUseCase(
        roomRepository = roomRepository,
        userRepository = userRepository,
        roomWatchingManager = roomWatchingManager,
    )

    @Bean
    fun startGameUseCase(
        roomRepository: RoomRepository,
        gameCreator: GameCreator,
        roomWatchingManager: RoomWatchingManager,
    ) = StartGameUseCase(
        roomRepository = roomRepository,
        gameCreator = gameCreator,
        roomWatchingManager = roomWatchingManager,
    )

    @Bean
    fun updateRoomsListUseCase(
        roomWatchingManager: RoomWatchingManager,
        roomsListWatchingManager: RoomsListWatchingManager,
        getRoomsUseCase: GetRoomsUseCase,
    ) = UpdateRoomsListUseCase(
        roomWatchingManager = roomWatchingManager,
        roomsListWatchingManager = roomsListWatchingManager,
        getRoomsUseCase = getRoomsUseCase,
    )

    @Bean
    fun challengeProvider() = ChallengeProvider()

    @Bean
    fun wordsProvider() = WordsProvider()

    @Bean
    fun gameCreator(
        wordsProvider: WordsProvider,
        roundCreator: RoundCreator,
    ) = GameCreator(
        wordsProvider = wordsProvider,
        roundCreator = roundCreator,
    )

    @Bean
    fun roundCreator(
        challengeProvider: ChallengeProvider
    ) = RoundCreator(
        challengeProvider = challengeProvider
    )

    @Bean
    fun endRoundsUseCase(
        roomRepository: RoomRepository,
        roomWatchingManager: RoomWatchingManager,
    ) = EndRoundsUseCase(
        roomRepository = roomRepository,
        roomWatchingManager = roomWatchingManager,
    )

    @Bean
    fun startRoundUseCase(
        roomRepository: RoomRepository,
        roomWatchingManager: RoomWatchingManager,
    ) = StartRoundUseCase(
        roomRepository = roomRepository,
        roomWatchingManager = roomWatchingManager,
    )

    @Bean
    fun rateGuessUseCase(
        roomRepository: RoomRepository,
        userRepository: UserRepository,
        roomToUserRoomMapper: RoomToUserRoomMapper,
        roomWatchingManager: RoomWatchingManager,
    ) = RateGuessUseCase(
        roomRepository = roomRepository,
        userRepository = userRepository,
        roomToUserRoomMapper = roomToUserRoomMapper,
        roomWatchingManager = roomWatchingManager,
    )

    @Bean
    fun nextRoundUseCase(
        roomRepository: RoomRepository,
        roomWatchingManager: RoomWatchingManager,
        roundCreator: RoundCreator,
    ) = NextRoundUseCase(
        roomRepository = roomRepository,
        roomWatchingManager = roomWatchingManager,
        roundCreator = roundCreator,
    )

    @Bean
    fun getPointsUseCase(
        roomRepository: RoomRepository,
        pointsCounter: PointsCounter,
    ) = GetPointsUseCase(
        roomRepository,
        pointsCounter = pointsCounter,
    )

    @Bean
    fun endGameUseCase(
        roomRepository: RoomRepository,
        roomWatchingManager: RoomWatchingManager,
    ) = EndGameUseCase(
        roomRepository = roomRepository,
        roomWatchingManager = roomWatchingManager,
    )

    @Bean
    fun roomToUserRoomMapper(
        pointsCounter: PointsCounter,
    ) = RoomToUserRoomMapper(
        pointsCounter = pointsCounter,
    )

    @Bean
    fun roomStatusController(): RoomStatusController =
        DeferredResultRoomStatusController()

    @Bean
    fun roomsListWatchingManager(): RoomsListWatchingManager =
        InMemoryRoomsWatchingManager()

    @Bean
    fun roomWatchingManager(): RoomWatchingManager =
        InMemoryRoomWatchingManager()
}
