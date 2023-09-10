package pl.przemyslawpitus.mamyklocga.api.rooms

import org.springframework.web.bind.annotation.CookieValue
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.context.request.async.DeferredResult
import pl.przemyslawpitus.mamyklocga.WithLogger
import pl.przemyslawpitus.mamyklocga.api.LongPollingObserver
import pl.przemyslawpitus.mamyklocga.domain.rooms.RoomChangedEvent
import pl.przemyslawpitus.mamyklocga.domain.rooms.RoomWatchingManager
import pl.przemyslawpitus.mamyklocga.domain.rooms.RoomsListChangedEvent
import pl.przemyslawpitus.mamyklocga.domain.rooms.RoomsListWatchingManager
import pl.przemyslawpitus.mamyklocga.domain.rooms.getRoomUseCase.GetRoomUseCase
import pl.przemyslawpitus.mamyklocga.domain.user.UserId

private const val LONG_POLLING_TIMEOUT_MILLIS = 600_000L

@RestController
@RequestMapping("/rooms")
class LongPollingRoomStatusEndpoint(
    private val roomWatchingManager: RoomWatchingManager,
    private val roomsListWatchingManager: RoomsListWatchingManager,
    private val getRoomUseCase: GetRoomUseCase,
) {

    @GetMapping("/{roomCode}/watch")
    fun getRoomStatusLongPolling(
        @CookieValue userId: String,
        @PathVariable roomCode: String,
    ): DeferredResult<GetRoomResponse> {
        logger.info("Watch room, userId: $userId, roomCode: $roomCode")

        val deferredResult = DeferredResult<GetRoomResponse>(LONG_POLLING_TIMEOUT_MILLIS)
        val observer = LongPollingRoomObserver(
            deferredResult = deferredResult,
            userId = UserId(userId)
        )
        roomWatchingManager.subscribe(observer)

        return deferredResult
    }

    @GetMapping("/watch")
    fun watchRoomsList(
        @CookieValue userId: String,
    ): DeferredResult<RoomsResponse> {
        logger.info("Watch rooms list, userId: $userId")

        val deferredResult = DeferredResult<RoomsResponse>(LONG_POLLING_TIMEOUT_MILLIS)
        val observer = LongPollingRoomsListObserver(deferredResult = deferredResult)
        roomsListWatchingManager.subscribe(observer)

        return deferredResult
    }

    inner class LongPollingRoomObserver(
        deferredResult: DeferredResult<GetRoomResponse>,
        private val userId: UserId,
    ) : LongPollingObserver<RoomChangedEvent, GetRoomResponse>(deferredResult) {

        override fun onChange(event: RoomChangedEvent) {
            try {
                val userRoom = getRoomUseCase.getRoom(
                    roomCode = event.room.code,
                    userId = userId,
                )
                deferredResult.setResult(userRoom.toGetRoomResponse())
            } catch (exception: Exception) {
                logger.error("Error while handling room changed event", exception)
                deferredResult.setErrorResult(exception)
            }
        }
    }

    inner class LongPollingRoomsListObserver(
        deferredResult: DeferredResult<RoomsResponse>,
    ) : LongPollingObserver<RoomsListChangedEvent, RoomsResponse>(deferredResult) {

        override fun onChange(event: RoomsListChangedEvent) {
            deferredResult.setResult(event.toDto())
        }
    }

    private companion object : WithLogger()
}


private fun RoomsListChangedEvent.toDto() = RoomsResponse(
    rooms = this.rooms.map {
        RoomResponse(
            code = it.code,
            name = it.name,
            usersCount = it.users.size,
        )
    }
)
