package pl.przemyslawpitus.mamyklocga.infrastructure

import pl.przemyslawpitus.mamyklocga.domain.rooms.RoomsListChangedEvent
import pl.przemyslawpitus.mamyklocga.domain.rooms.RoomsListObserver
import pl.przemyslawpitus.mamyklocga.domain.rooms.RoomsListWatchingManager
import java.util.concurrent.ConcurrentLinkedQueue

class InMemoryRoomsWatchingManager : RoomsListWatchingManager {
    private val observers = ConcurrentLinkedQueue<RoomsListObserver>()

     override fun subscribe(observer: RoomsListObserver) {
        observers.add(observer)
    }

    override fun publish(event: RoomsListChangedEvent) {
        observers.forEach {
            it.onChange(event)
            observers.remove(it)
        }
    }
}