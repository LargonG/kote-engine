package kotegames.engine.com

open class Event<T> {
    private val observers = mutableSetOf<(T) -> Unit>()

    @Synchronized
    operator fun plusAssign(observer: (T) -> Unit) {
        observers.add(observer)
    }

    @Synchronized
    operator fun minusAssign(observer: (T) -> Unit) {
        observers.remove(observer)
    }

    // Not thread safety: at the same time element can be deleted & iterator changes value to this removed element
    operator fun invoke(value: T) {
        observers.forEach { it(value) }
    }
}