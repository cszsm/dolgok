package cszsm.dolgok.core.presentation.components.singlevaluelistitem

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

sealed interface SingleValueListItemShapes {
    val leadingShape: Shape
    val trailingShape: Shape

    data object Single : SingleValueListItemShapes {
        override val leadingShape = RoundedCornerShape(
            topStart = RADIUS_LARGE,
            bottomStart = RADIUS_LARGE,
            topEnd = RADIUS_SMALL,
            bottomEnd = RADIUS_SMALL,
        )
        override val trailingShape = RoundedCornerShape(
            topStart = RADIUS_SMALL,
            bottomStart = RADIUS_SMALL,
            topEnd = RADIUS_LARGE,
            bottomEnd = RADIUS_LARGE,
        )
    }

    data object Top : SingleValueListItemShapes {
        override val leadingShape = RoundedCornerShape(
            topStart = RADIUS_LARGE,
            bottomStart = RADIUS_SMALL,
            topEnd = RADIUS_SMALL,
            bottomEnd = RADIUS_SMALL,
        )
        override val trailingShape = RoundedCornerShape(
            topStart = RADIUS_SMALL,
            bottomStart = RADIUS_SMALL,
            topEnd = RADIUS_LARGE,
            bottomEnd = RADIUS_SMALL,
        )
    }

    data object Middle : SingleValueListItemShapes {
        override val leadingShape = RoundedCornerShape(
            size = RADIUS_SMALL,
        )
        override val trailingShape = RoundedCornerShape(
            size = RADIUS_SMALL,
        )
    }

    data object Bottom : SingleValueListItemShapes {
        override val leadingShape = RoundedCornerShape(
            topStart = RADIUS_SMALL,
            bottomStart = RADIUS_LARGE,
            topEnd = RADIUS_SMALL,
            bottomEnd = RADIUS_SMALL,
        )
        override val trailingShape = RoundedCornerShape(
            topStart = RADIUS_SMALL,
            bottomStart = RADIUS_SMALL,
            topEnd = RADIUS_SMALL,
            bottomEnd = RADIUS_LARGE,
        )
    }

    private companion object {
        val RADIUS_SMALL = 4.dp
        val RADIUS_LARGE = 16.dp
    }
}
