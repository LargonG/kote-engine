package kotegames.engine.com

class Group private constructor() {
    private val entities = mutableSetOf<Entity>()
    private val allTypes = mutableListOf<Int>()
    private val anyTypes = mutableSetOf<Int>()
    private val noneTypes = mutableSetOf<Int>()

    companion object {
        fun nil() = Group()

        fun Group.all(vararg params: ComponentType): Group {
            this.allTypes.addAll(params.map {it.id})
            return this
        }

        fun Group.any(vararg params: ComponentType): Group {
            this.anyTypes.addAll(params.map {it.id})
            return this
        }

        fun Group.none(vararg params: ComponentType): Group {
            this.noneTypes.addAll(params.map {it.id})
            return this
        }
    }


    val onCreateEvent = Event<Entity>()
    val onDestroyEvent = Event<Entity>()

    private fun filter(entity: Entity): Boolean {
        val res1 = allTypes.isEmpty() || entity.types.containsAll(allTypes)
        val res2 = anyTypes.isEmpty() || entity.types.any {it in anyTypes}
        val res3 = noneTypes.isEmpty() || entity.types.none {it in noneTypes}
        return res1 && res2 && res3
    }

    fun iterator(): Iterator<Entity> = entities.iterator()

    private fun onCreate(entity: Entity) {
        if (filter(entity)) {
            entities.add(entity)
            onCreateEvent(entity)
        }
    }

    private fun onDestroy(entity: Entity) {
        if (filter(entity)) {
            onDestroyEvent(entity)
            entities.remove(entity)
        }
    }

    init {
        Entities.onCreate += { onCreate(it) }
        Entities.onDestroy += { onDestroy(it) }
    }
}