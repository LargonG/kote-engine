package kotegames.engine.com

@JvmInline
value class Entity(internal val id: Int = 0) {
    val components: Array<Component?>
        get() = Entities.alive[id] ?: throw NoSuchElementException("Entity does not exist")
    val types: Set<Int>
        get() = mutableTypes

    private val mutableTypes: MutableSet<Int>
        get() = Entities.aliveTypes[id]

    // Работает слишком долго, нужно что-то с этим делать
    inline fun <reified T: Component> getComponent(type: ComponentType) = components[type.id] as T

    fun setComponent(component: Component) {
        components[component.type.id] = component
        mutableTypes.add(component.type.id)
    }

    fun removeComponent(type: ComponentType): Component {
        val res = components[type.id] ?: throw NoSuchElementException("Component does not exist")
        components[type.id] = null
        mutableTypes.remove(type.id)
        return res
    }
}


// Тонкое место в производительности
internal object Entities {
    // Alive components can be null, such as dead (problem: allocation)
    internal val alive = mutableListOf<Array<Component?>?>()
    internal val aliveTypes = mutableListOf<MutableSet<Int>>()
    private val dead = mutableListOf<Entity>()
    private var nextId = 0

    val onCreate = Event<Entity>()
    val onDestroy = Event<Entity>()

    @Synchronized
    private fun pullEntity(components: Array<out Component>): Entity {
        val entity = if (dead.isNotEmpty()) dead.removeLast() else Entity(nextId++)
        alive.add(arrayOfNulls(ComponentType.count))
        aliveTypes.add(mutableSetOf())
        components.forEach { entity.setComponent(it) }
        return entity
    }

    @Synchronized
    private fun pushEntity(entity: Entity) {
        alive[entity.id] = null
        aliveTypes[entity.id].clear()
        dead.add(entity)
    }

    internal fun createEntity(components: Array<out Component>): Entity {
        val entity = pullEntity(components)
        onCreate(entity)
        return entity
    }

    internal fun destroyEntity(entity: Entity): Unit {
        onDestroy(entity)
        pushEntity(entity)
    }
}