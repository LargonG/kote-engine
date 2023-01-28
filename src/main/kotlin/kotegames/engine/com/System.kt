package kotegames.engine.com

abstract class System(internal val group: Group) {

    init {
        group.onCreateEvent += { init(it) }
        group.onDestroyEvent += { init(it) }
    }

    open fun start() {}

    open fun init(entity: Entity) {}

    internal fun run() {
        val iter = group.iterator()
        while (iter.hasNext()) {
            update(iter.next())
        }
    }

    open fun update(entity: Entity) {}
    open fun destroy(entity: Entity) {}

    open fun stop() {}
}