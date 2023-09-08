package pl.przemyslawpitus.mamyklocga.domain.rooms

interface RoomStatusController {
    fun subscribe(handler: (RoomStatusChangedEvent) -> Unit)
    fun publish(event: RoomStatusChangedEvent)
}

data class RoomStatusChangedEvent(
    val type: String,
    val payload: Any,
)