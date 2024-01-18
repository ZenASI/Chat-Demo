package com.chat.joycom.ui.commom

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection

class OtherBubbleShape(private val cornerRadius: Float) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        return Outline.Generic(path = drawBubblePath(size, cornerRadius = cornerRadius))
    }

    private fun drawBubblePath(size: Size, cornerRadius: Float): Path {
        val defaultValue = 30f
        return Path().apply {
            reset()
            lineTo(x = size.width - cornerRadius, y = 0f)
            arcTo(
                rect = Rect(Offset(size.width - cornerRadius, cornerRadius), cornerRadius),
                startAngleDegrees = -90f,
                sweepAngleDegrees = 90f,
                forceMoveTo = false
            )
            lineTo(size.width, size.height - cornerRadius)
            arcTo(
                rect = Rect(
                    Offset(size.width - cornerRadius, size.height - cornerRadius),
                    cornerRadius
                ),
                startAngleDegrees = 0f,
                sweepAngleDegrees = 90f,
                forceMoveTo = false
            )
            lineTo((cornerRadius + defaultValue), size.height)
            arcTo(
                rect = Rect(
                    Offset(cornerRadius + defaultValue, size.height - cornerRadius),
                    cornerRadius
                ),
                startAngleDegrees = -270f,
                sweepAngleDegrees = 90f,
                forceMoveTo = false
            )
            lineTo(defaultValue, defaultValue)
            lineTo(0f, 0f)
            close()
        }
    }
}

class SelfBubbleShape(private val cornerRadius: Float) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        return Outline.Generic(path = drawBubblePath(size, cornerRadius = cornerRadius))
    }

    private fun drawBubblePath(size: Size, cornerRadius: Float): Path {
        val defaultValue = 30f
        return Path().apply {
            reset()
            moveTo(cornerRadius, 0f)
            lineTo(size.width, 0f)
            lineTo(size.width - defaultValue, defaultValue)
            lineTo(size.width - defaultValue, size.height - cornerRadius)
            arcTo(
                rect = Rect(
                    Offset(
                        size.width - (cornerRadius + defaultValue),
                        size.height - cornerRadius
                    ), cornerRadius
                ),
                startAngleDegrees = 0f,
                sweepAngleDegrees = 90f,
                forceMoveTo = false
            )
            lineTo(cornerRadius, size.height)
            arcTo(
                rect = Rect(Offset(cornerRadius, size.height - cornerRadius), cornerRadius),
                startAngleDegrees = -270f,
                sweepAngleDegrees = 90f,
                forceMoveTo = false
            )

            lineTo(0f, cornerRadius)

            arcTo(
                rect = Rect(Offset(cornerRadius, cornerRadius), cornerRadius),
                startAngleDegrees = -180f,
                sweepAngleDegrees = 90f,
                forceMoveTo = false
            )

            close()
        }
    }
}