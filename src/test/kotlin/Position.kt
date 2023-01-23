import kotegames.engine.com.*
import org.junit.jupiter.api.Test
import kotlin.math.sqrt

data class Vector2(val x: Float, val y: Float) {
    val magnitude by lazy { sqrt(x * x + y * y) }
    val normalize by lazy { Vector2(x / magnitude, y / magnitude) }

    operator fun plus(vector: Vector2) = Vector2(x + vector.x, y + vector.y)
    operator fun times(k: Float) = Vector2(x * k, y * k)
}

const val POSITION = 0
const val SPEED = 1

@Component(POSITION)
data class Position(val x: Float, val y: Float): IComponent() {
    operator fun plus(vector: Vector2) = Position(x + vector.x, y + vector.y)
}

@Component(SPEED)
data class Speed(val direction: Vector2, val amplitude: Float): IComponent()

@UseComponents(POSITION, SPEED)
object MovementSystem: System() {
    override fun update(entities: List<Entity>) {
        entities.forEach { entity ->
            val position = entity.components[POSITION] as Position
            val speed = entity.components[SPEED] as Speed
            val newPosition = position + speed.direction * speed.amplitude
            entity.components[POSITION] = newPosition
            println("$entity $newPosition")
        }
    }

}

object Game {
    @Test
    fun run() {
        EntityList.createEntity(Position(10f, 0f))
        EntityList.createEntity(Position(1f, 1f), Speed(Vector2(1f, 0f).normalize, 5f))
        EntityList.createEntity(Position(0f, 0f), Speed(Vector2(0f, 1f).normalize, 2f))
        repeat(5) {
            MovementSystem.run()
        }
    }
}