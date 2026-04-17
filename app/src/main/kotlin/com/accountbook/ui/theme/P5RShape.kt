package com.accountbook.ui.theme

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

class QuadShape(
    private val topLeftOffset: Dp = 0.dp,
    private val topRightOffset: Dp = 0.dp,
    private val bottomRightOffset: Dp = 0.dp,
    private val bottomLeftOffset: Dp = 0.dp,
) : Shape {
    override fun createOutline(size: Size, layoutDirection: LayoutDirection, density: Density): Outline {
        val tl = with(density) { topLeftOffset.toPx() }
        val tr = with(density) { topRightOffset.toPx() }
        val br = with(density) { bottomRightOffset.toPx() }
        val bl = with(density) { bottomLeftOffset.toPx() }
        val w = size.width
        val h = size.height
        val path = Path().apply {
            moveTo(tl, 0f)
            lineTo(w - tr, 0f)
            lineTo(w - br, h)
            lineTo(bl, h)
            close()
        }
        return Outline.Generic(path)
    }
}

class BorderedQuadShape(
    private val topLeftOffset: Dp = 0.dp,
    private val topRightOffset: Dp = 0.dp,
    private val bottomRightOffset: Dp = 0.dp,
    private val bottomLeftOffset: Dp = 0.dp,
    private val borderWidth: Dp = 4.dp,
) : Shape {
    override fun createOutline(size: Size, layoutDirection: LayoutDirection, density: Density): Outline {
        val tl = with(density) { topLeftOffset.toPx() }
        val tr = with(density) { topRightOffset.toPx() }
        val br = with(density) { bottomRightOffset.toPx() }
        val bl = with(density) { bottomLeftOffset.toPx() }
        val bw = with(density) { borderWidth.toPx() }
        val w = size.width
        val h = size.height
        val path = Path().apply {
            moveTo(tl + bw, bw)
            lineTo(w - tr - bw, bw)
            lineTo(w - br - bw, h - bw)
            lineTo(bl + bw, h - bw)
            close()
        }
        return Outline.Generic(path)
    }
}

class CornerAccentQuadShape(
    private val topLeftOffset: Dp = 0.dp,
    private val topRightOffset: Dp = 0.dp,
    private val bottomRightOffset: Dp = 0.dp,
    private val bottomLeftOffset: Dp = 0.dp,
    private val borderWidth: Dp = 4.dp,
    private val accentTopLeft: Dp = 0.dp,
    private val accentTopRight: Dp = 0.dp,
    private val accentBottomRight: Dp = 0.dp,
    private val accentBottomLeft: Dp = 0.dp,
) : Shape {
    override fun createOutline(size: Size, layoutDirection: LayoutDirection, density: Density): Outline {
        val tl = with(density) { topLeftOffset.toPx() }
        val tr = with(density) { topRightOffset.toPx() }
        val br = with(density) { bottomRightOffset.toPx() }
        val bl = with(density) { bottomLeftOffset.toPx() }
        val bw = with(density) { borderWidth.toPx() }
        val aTL = with(density) { accentTopLeft.toPx() }
        val aTR = with(density) { accentTopRight.toPx() }
        val aBR = with(density) { accentBottomRight.toPx() }
        val aBL = with(density) { accentBottomLeft.toPx() }
        val w = size.width
        val h = size.height
        val path = Path().apply {
            moveTo(tl - aTL, -aTL)
            lineTo(tl + bw, bw)
            lineTo(w - tr - bw, bw)
            lineTo(w - tr + aTR, -aTR)
            lineTo(w - br + aBR, -aBR)
            lineTo(w - br - bw, h - bw)
            lineTo(bl + bw, h - bw)
            lineTo(bl - aBL, h + aBL)
            close()
        }
        return Outline.Generic(path)
    }
}

val QuadShapeA = QuadShape(topLeftOffset = 8.dp, topRightOffset = 2.dp, bottomRightOffset = 4.dp, bottomLeftOffset = 10.dp)
val QuadShapeNarrow = QuadShape(topLeftOffset = 4.dp, topRightOffset = 4.dp, bottomRightOffset = 4.dp, bottomLeftOffset = 4.dp)
val QuadShapeRecord = QuadShape(topLeftOffset = 6.dp, topRightOffset = 2.dp, bottomRightOffset = 4.dp, bottomLeftOffset = 8.dp)

val AccentQuadShapeA = CornerAccentQuadShape(topLeftOffset = 8.dp, topRightOffset = 2.dp, bottomRightOffset = 4.dp, bottomLeftOffset = 10.dp, borderWidth = 4.dp, accentTopLeft = 6.dp, accentBottomRight = 6.dp)
val AccentQuadShapeRecord = CornerAccentQuadShape(topLeftOffset = 6.dp, topRightOffset = 2.dp, bottomRightOffset = 4.dp, bottomLeftOffset = 8.dp, borderWidth = 4.dp, accentTopLeft = 5.dp, accentBottomRight = 5.dp)

class PerspectiveShape : Shape {
    override fun createOutline(size: Size, layoutDirection: LayoutDirection, density: Density): Outline {
        val w = size.width
        val h = size.height
        val path = Path().apply {
            moveTo(0f, 0f)
            lineTo(w, h * 0.08f)
            lineTo(w, h * 0.92f)
            lineTo(0f, h)
            close()
        }
        return Outline.Generic(path)
    }
}

val QuadPerspective = PerspectiveShape()

class SlantRightShape : Shape {
    override fun createOutline(size: Size, layoutDirection: LayoutDirection, density: Density): Outline {
        val w = size.width
        val h = size.height
        val path = Path().apply {
            moveTo(w * 0.1f, 0f)
            lineTo(w, 0f)
            lineTo(w * 0.9f, h)
            lineTo(0f, h)
            close()
        }
        return Outline.Generic(path)
    }
}

class SlantLeftShape : Shape {
    override fun createOutline(size: Size, layoutDirection: LayoutDirection, density: Density): Outline {
        val w = size.width
        val h = size.height
        val path = Path().apply {
            moveTo(0f, 0f)
            lineTo(w * 0.9f, 0f)
            lineTo(w, h)
            lineTo(w * 0.1f, h)
            close()
        }
        return Outline.Generic(path)
    }
}

class FabShape : Shape {
    override fun createOutline(size: Size, layoutDirection: LayoutDirection, density: Density): Outline {
        val w = size.width
        val h = size.height
        val path = Path().apply {
            moveTo(w * 0.2f, 0f)
            lineTo(w, 0f)
            lineTo(w * 0.8f, h)
            lineTo(0f, h)
            close()
        }
        return Outline.Generic(path)
    }
}

val SlantRight = SlantRightShape()
val SlantLeft = SlantLeftShape()
val QuadFab = FabShape()

class TopBarSlantShape : Shape {
    override fun createOutline(size: Size, layoutDirection: LayoutDirection, density: Density): Outline {
        val w = size.width
        val h = size.height
        val path = Path().apply {
            moveTo(0f, 0f)
            lineTo(w, 0f)
            lineTo(w, h * 0.7f)
            lineTo(w * 0.92f, h)
            lineTo(0f, h)
            close()
        }
        return Outline.Generic(path)
    }
}

val TopBarSlant = TopBarSlantShape()
