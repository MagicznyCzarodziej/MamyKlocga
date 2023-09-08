package pl.przemyslawpitus.mamyklocga.domain

interface WatchingManager<T> {
    fun subscribe(observer: Observer<T>)
    fun publish(event: T)
}

interface Observer<T> {
    fun onChange(event: T)
}