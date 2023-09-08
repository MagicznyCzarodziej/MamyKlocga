package pl.przemyslawpitus.mamyklocga.infrastructure

import pl.przemyslawpitus.mamyklocga.domain.rooms.RoomChangedEvent
import pl.przemyslawpitus.mamyklocga.domain.rooms.RoomObserver
import pl.przemyslawpitus.mamyklocga.domain.rooms.RoomWatchingManager
import java.util.concurrent.ConcurrentLinkedQueue

class InMemoryRoomWatchingManager : RoomWatchingManager {
    private val observers = ConcurrentLinkedQueue<RoomObserver>()

    override fun subscribe(observer: RoomObserver) {
        observers.add(observer)
    }

    override fun publish(event: RoomChangedEvent) {
        observers.forEach {
            it.onChange(event)
            observers.remove(it)
        }
    }
}