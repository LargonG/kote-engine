package kotegames.engine.com

abstract class System {
    /**
     * Принимает в себя коллекцию из всех игровых объектов
     */

    // Available components on entity that system uses
    val components by lazy { initializeAnnotations() }

    private fun initializeAnnotations(): Set<Int> =
        this::class.annotations.filterIsInstance<UseComponents>().first().ids.toSet()

    private fun filterEntity(entity: Entity): Boolean = entity.componentsEntry.containsAll(components)

    fun run() {
        update(EntityList.entities.filter { filterEntity(it.key) }.keys.toList())
    }

    // Получает только объекты с нужными компонентами
    protected abstract fun update(entities: List<Entity>)
}

@Target(AnnotationTarget.CLASS)
@MustBeDocumented
annotation class UseComponents(vararg val ids: Int)