package cszsm.dolgok.core.presentation.components.singlevaluelistitem

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.FieldSource

internal class SingleValueListItemShapeParamsTest {

    @ParameterizedTest
    @FieldSource
    fun toItemShapes(
        data: Pair<SingleValueListItemShapeParams, SingleValueListItemShapes>,
    ) {
        // Given
        val params = data.first

        // When
        val actual = params.toItemShapes()

        // Then
        val expected = data.second
        assertEquals(expected, actual)
    }

    private companion object {
        @Suppress("unused")
        val toItemShapes = listOf(
            // TODO: check this case
            Pair(
                SingleValueListItemShapeParams(index = 0, size = 1),
                SingleValueListItemShapes.Single,
            ),
            Pair(
                SingleValueListItemShapeParams(index = 0, size = 3),
                SingleValueListItemShapes.Top,
            ),
            Pair(
                SingleValueListItemShapeParams(index = 1, size = 3),
                SingleValueListItemShapes.Middle,
            ),
            Pair(
                SingleValueListItemShapeParams(index = 2, size = 3),
                SingleValueListItemShapes.Bottom,
            ),
            Pair(
                SingleValueListItemShapeParams(index = 1, size = 3, forcedTop = true),
                SingleValueListItemShapes.Top,
            ),
            Pair(
                SingleValueListItemShapeParams(index = 1, size = 3, forcedBottom = true),
                SingleValueListItemShapes.Bottom,
            ),
        )
    }
}