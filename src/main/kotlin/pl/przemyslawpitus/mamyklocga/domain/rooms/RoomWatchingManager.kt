package pl.przemyslawpitus.mamyklocga.domain.rooms

import pl.przemyslawpitus.mamyklocga.domain.Observer
import pl.przemyslawpitus.mamyklocga.domain.WatchingManager

interface RoomWatchingManager: WatchingManager<RoomChangedEvent>

typealias RoomObserver = Observer<RoomChangedEvent>

data class RoomChangedEvent(
    val room: Room,
)