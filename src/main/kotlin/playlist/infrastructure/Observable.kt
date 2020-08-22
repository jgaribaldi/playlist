package playlist.infrastructure

interface Observer<T> {
    fun update(newValue: T)
}

abstract class Observable<T> {
    private val observers: MutableList<Observer<T>> = mutableListOf()

    fun register(observer: Observer<T>) = observers.add(observer)

    fun updateObservers(newValue: T) = observers.forEach { it.update(newValue) }
}