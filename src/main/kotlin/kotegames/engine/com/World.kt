package kotegames.engine.com

import org.jetbrains.annotations.TestOnly

open class World {
    private val entities = mutableSetOf<Entity>()
    private val systems = mutableListOf<System>()

    // start -> update (init, destroy) -> stop | -> start -> ...

    fun start() {
        systems.forEach{ it.start() }
    }

    fun update() {
        systems.forEach{ it.run() }
    }

    fun pause() {
        systems.forEach{ it.pause() }
    }

    fun stop() {
        entities.forEach { Entities.destroyEntity(it) }
        entities.clear()

        systems.forEach{ it.stop() }
    }

    fun entity(vararg components: Component) {
        entities.add(Entities.createEntity(components))
    }

    fun addSystem(system: System) {
        systems.add(system)
    }

    @TestOnly
    fun getEntities() = entities
}