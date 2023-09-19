package pl.przemyslawpitus.mamyklocga.domain.rooms.updateRoomsListUseCase

import pl.przemyslawpitus.mamyklocga.domain.Observer
import pl.przemyslawpitus.mamyklocga.domain.rooms.RoomChangedEvent
import pl.przemyslawpitus.mamyklocga.domain.rooms.RoomWatchingManager
import pl.przemyslawpitus.mamyklocga.domain.rooms.RoomsListChangedEvent
import pl.przemyslawpitus.mamyklocga.domain.rooms.RoomsListWatchingManager
import pl.przemyslawpitus.mamyklocga.domain.rooms.getRoomsUseCase.GetRoomsUseCase

class UpdateRoomsListUseCase(
    private val roomWatchingManager: RoomWatchingManager,
    private val roomsListWatchingManager: RoomsListWatchingManager,
    private val getRoomsUseCase: GetRoomsUseCase,
) {
    init {
        subscribeForRoomChanges()
    }

    private fun subscribeForRoomChanges() {
        val observer = RoomsListObserver()
        roomWatchingManager.subscribe(observer)
    }

    inner class RoomsListObserver : Observer<RoomChangedEvent> {
        override fun onChange(event: RoomChangedEvent) {
            val rooms = getRoomsUseCase.getPublicRooms()
            roomsListWatchingManager.publish(RoomsListChangedEvent(rooms))
            subscribeForRoomChanges()
        }
    }
}

