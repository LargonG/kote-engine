import kotegames.engine.com.*
import kotegames.engine.com.Group.Companion.all
import org.junit.jupiter.api.Test
import kotlin.math.sqrt
import kotlin.test.assertEquals

data class Vector2(val x: Float, val y: Float) {
    val magnitude by lazy { sqrt(x * x + y * y) }
    val normalize by lazy { Vector2(x / magnitude, y / magnitude) }

    operator fun plus(vector: Vector2) = Vector2(x + vector.x, y + vector.y)
    operator fun times(k: Float) = Vector2(x * k, y * k)
}

val positionType = ComponentType()
val speedType = ComponentType()

@JvmInline
value class Position(private val value: Vector2): Component {
    operator fun plus(vector: Vector2) = Position(value + vector)

    constructor(x: Float, y: Float) : this(Vector2(x, y))

    override val type: ComponentType
        get() = positionType
}

data class Speed(val direction: Vector2, val amplitude: Float): Component {
    override val type: ComponentType
        get() = speedType
}

object MovementSystem: System(Group.nil().all(positionType, speedType)) {
    override fun update(entity: Entity) {
        val position = entity.getComponent<Position>(positionType)
        val speed = entity.getComponent<Speed>(speedType)
        val newPosition = position + speed.direction * speed.amplitude
        entity.setComponent(newPosition);
        println("$entity $newPosition")
    }
}

object Game {
    @Test
    fun simpleWorld() {
        val world = World()
        world.addSystem(MovementSystem)
        val pos = Position(10f, 0f)
        world.entity(pos)
        world.entity(Position(1f, 1f), Speed(Vector2(1f, 0f).normalize, 5f))
        world.entity(Position(0f, 0f), Speed(Vector2(0f, 1f).normalize, 2f))

        world.start()
        repeat(5) {
            world.update()
        }

        val entities = world.getEntities().toList()
        assertEquals(entities[0].components[positionType.id], pos)
        world.stop()
    }
}