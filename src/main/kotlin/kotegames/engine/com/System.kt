package kotegames.engine.com

abstract class System(private val group: Group) {

    init {
        group.onCreateEvent += { init(it) }
        group.onDestroyEvent += { destroy(it) }
    }

    open fun start() {}

    open fun resume() {}

    open fun init(entity: Entity) {}

    internal fun run() {
        group.iterator().forEach { update(it) }
    }

    open fun update(entity: Entity) {}
    open fun destroy(entity: Entity) {}

    open fun pause() {}

    open fun stop() {}
}