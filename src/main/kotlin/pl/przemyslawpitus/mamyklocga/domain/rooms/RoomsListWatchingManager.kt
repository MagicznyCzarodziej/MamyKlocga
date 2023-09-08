package pl.przemyslawpitus.mamyklocga.domain.rooms

import pl.przemyslawpitus.mamyklocga.domain.Observer
import pl.przemyslawpitus.mamyklocga.domain.WatchingManager

interface RoomsListWatchingManager: WatchingManager<RoomsListChangedEvent>

typealias RoomsListObserver = Observer<RoomsListChangedEvent>

data class RoomsListChangedEvent(
    val rooms: List<Room>,
)