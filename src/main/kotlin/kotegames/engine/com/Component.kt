package kotegames.engine.com

open class ComponentType {
    val id = count++

    companion object {
        var count = 0
    }
}

interface Component {
    val type: ComponentType
}