package kotegames.engine.com;

import java.util.*
import kotlin.NoSuchElementException


/**
 * Entity класс для большей производительности является просто id,
 * который ссылается на индекс в коллекции Entity
 */
@JvmInline
value class Entity(val id: Int = 0) {
    val components: MutableMap<Int, IComponent>
        get() = EntityList.entities[this] ?: throw NoSuchElementException("Entity has been destroyed")
    val componentsEntry: Set<Int>
        get() = EntityList.components[this] ?: throw NoSuchElementException("Entity has been destroyed")
}

/**
 * Этот объект должен уметь работать сразу в нескольких потоках
 */
object EntityList {
    val entities: MutableMap<Entity, MutableMap<Int, IComponent>> = mutableMapOf()

    // Это конечно не быстро, но я не придумал, как нам взять информацию о количестве компонент по имеющимся аннотациям
    val components: MutableMap<Entity, Set<Int>> = mutableMapOf()

    @Synchronized
    fun createEntity(vararg data: IComponent): Entity {
        val entity = Entity(entities.size)
        val comps = data.map{Pair(it.id, it)}.toTypedArray()
        entities[entity] = mutableMapOf(*comps)
        components[entity] = data.getSet()
        return entity
    }

    @Synchronized
    fun destroyEntity(entity: Entity) {
        entities.remove(entity)
        components.remove(entity)
    }

    private fun Array<out IComponent>.getSet(): Set<Int>{
        val ids = this.map {component -> component.id}.toTypedArray()
        return setOf(*ids)
    }
}