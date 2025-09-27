package cszsm.dolgok.core.presentation.components.singlevaluelistitem

data class SingleValueListItemShapeParams(
    val index: Int,
    val size: Int,
    val forcedTop: Boolean = false,
    val forcedBottom: Boolean = false,
) {
    fun toItemShapes() = when {
        size == 1 -> SingleValueListItemShapes.Single
        forcedTop || index == 0 -> SingleValueListItemShapes.Top
        forcedBottom || index == size - 1 -> SingleValueListItemShapes.Bottom
        else -> SingleValueListItemShapes.Middle
    }
}