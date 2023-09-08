package pl.przemyslawpitus.mamyklocga.api.rooms

import pl.przemyslawpitus.mamyklocga.domain.rooms.RoomStatusChangedEvent
import pl.przemyslawpitus.mamyklocga.domain.rooms.RoomStatusController
import java.util.concurrent.ConcurrentLinkedQueue

class DeferredResultRoomStatusController : RoomStatusController {
    private val handlers = ConcurrentLinkedQueue<(RoomStatusChangedEvent) -> Unit>()

    override fun subscribe(handler: (RoomStatusChangedEvent) -> Unit) {
        handlers.add(handler)
    }

    override fun publish(event: RoomStatusChangedEvent) {
        handlers.forEach {
            it(event)
            handlers.remove(it)
        }
    }

}