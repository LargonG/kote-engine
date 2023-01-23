package kotegames.engine.com

/**
 * От данного интерфейса должны быть унаследованы все коллекции данных entities
 */
abstract class IComponent {
    val id by lazy { this::class.annotations.filterIsInstance<Component>().first().id }
}


/**
 * Для каждой компоненты, унаследованной от IComponent должен иметься независимый id,
 * чтобы библиотека могла правильно отфильтровать нужные для системы entity
 */
@Target(AnnotationTarget.CLASS)
@MustBeDocumented
annotation class Component(val id: Int = 0)
