package com.mlib.future.modifiers

import androidx.compose.animation.core.EaseInOutSine
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.background
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mlib.future.FutureTheme

private const val SWEEP_DURATION = 6400
private const val SWEEP_SAMPLES = 72

private fun DrawScope.averageCornerRadius(shape: Shape): CornerRadius {
	val outline = shape.createOutline(size, layoutDirection, this)
	return when (outline) {
		is Outline.Rounded -> {
			val rr = outline.roundRect
			CornerRadius(
				x = (rr.topLeftCornerRadius.x + rr.topRightCornerRadius.x) / 2f,
				y = (rr.bottomLeftCornerRadius.y + rr.bottomRightCornerRadius.y) / 2f
			)
		}
		else -> CornerRadius(4.dp.toPx())
	}
}

private fun buildDenseSweepPalette(base: Color, highlight: Color, n: Int): List<Color> {
	val low = base.copy(alpha = base.alpha * 0.15f)
	return List(n) { i ->
		val t = i.toFloat() / n                        
		val d = kotlin.math.abs(t - 0.5f) * 2f         
		if (d < 0.5f) {
			lerp(highlight, base, d * 1.2f)              
		} else {
			lerp(base, low, (d - 0.5f) * 1.2f)           
		}
	}
}

private fun rotateSweepColors(colors: List<Color>, angleDegrees: Float): List<Color> {
	val n = colors.size
	if (n <= 1) return colors
	val shift = angleDegrees / 360f * n
	return List(n) { i ->
		val pos = ((i - shift) % n + n) % n
		val lo = pos.toInt() % n
		val hi = (lo + 1) % n
		lerp(colors[lo], colors[hi], pos - pos.toInt())
	}
}

private fun DrawScope.drawSweepFrame(
	shape: Shape,
	strokePx: Float,
	base: Color,
	angle: Float,
	highlight: Color,
	outerStrokePx: Float = 0f
) {
	val radius = averageCornerRadius(shape)

	if (outerStrokePx > 0f) {
		drawRoundRect(
			color = base.copy(alpha = base.alpha * 0.25f),
			topLeft = Offset(outerStrokePx / 2, outerStrokePx / 2),
			size = Size(size.width - outerStrokePx, size.height - outerStrokePx),
			cornerRadius = radius,
			style = Stroke(outerStrokePx)
		)
	}

	val densePalette = buildDenseSweepPalette(base, highlight, SWEEP_SAMPLES)
	val rotatedPalette = rotateSweepColors(densePalette, angle)

	drawRoundRect(
		brush = Brush.sweepGradient(colors = rotatedPalette, center = center),
		topLeft = Offset(strokePx / 2, strokePx / 2),
		size = Size(size.width - strokePx, size.height - strokePx),
		cornerRadius = radius,
		style = Stroke(strokePx)
	)
}

@Composable
private fun rememberSweepAngle(duration: Int = SWEEP_DURATION): Float {
	val transition = rememberInfiniteTransition(label = "future_sweep")
	val angle by transition.animateFloat(
		initialValue = 0f,
		targetValue = 360f,
		animationSpec = infiniteRepeatable(
			animation = tween(duration, easing = LinearEasing),
			repeatMode = RepeatMode.Restart
		),
		label = "angle"
	)
	return angle
}

fun Modifier.futureGlow(
	color: Color? = null,
	blurRadius: Float = 16f,
	shape: RoundedCornerShape = RoundedCornerShape(8.dp),
	intensity: Float = 0.6f
) = composed {
	val config = FutureTheme.config
	val glowColor = color ?: config.colors.primary
	val transition = rememberInfiniteTransition(label = "future_glow")
	val pulse by transition.animateFloat(
		initialValue = 0.8f, targetValue = 1.2f,
		animationSpec = infiniteRepeatable(
			tween(1800, easing = EaseInOutSine),
			RepeatMode.Reverse
		),
		label = "pulse"
	)
	val sweepAngle = rememberSweepAngle()
	val haloAlpha = (intensity * 0.5f * pulse).coerceIn(0f, 1f)
	val borderBase = glowColor.copy(alpha = (0.25f + 0.6f * intensity).coerceIn(0f, 1f))
	val highlight = lerp(borderBase, Color.White, 0.65f)

	this
	.shadow(
		elevation = (blurRadius / 3f).dp.coerceIn(0.dp, 28.dp),
		shape = shape,
		clip = false,
		ambientColor = glowColor.copy(alpha = haloAlpha),
		spotColor = glowColor.copy(alpha = haloAlpha * 0.6f)
	)
	.clip(shape)
	.drawWithContent {
		drawContent()
		drawSweepFrame(shape, 1.dp.toPx(), borderBase, sweepAngle, highlight)
	}
}

fun Modifier.futureGlowStrong(
	color: Color? = null,
	shape: RoundedCornerShape = RoundedCornerShape(8.dp)
) = composed {
	val config = FutureTheme.config
	val glowColor = color ?: config.colors.primary
	val transition = rememberInfiniteTransition(label = "future_glow_strong")
	val pulse by transition.animateFloat(
		initialValue = 0.85f, targetValue = 1.25f,
		animationSpec = infiniteRepeatable(
			tween(1600, easing = EaseInOutSine),
			RepeatMode.Reverse
		),
		label = "pulse"
	)
	val sweepAngle = rememberSweepAngle()
	val base = glowColor.copy(alpha = 0.85f)

	this
	.shadow(
		elevation = 16.dp * pulse,
		shape = shape,
		clip = false,
		ambientColor = glowColor.copy(alpha = 0.4f * pulse),
		spotColor = glowColor.copy(alpha = 0.25f * pulse)
	)
	.clip(shape)
	.drawWithContent {
		drawContent()
		drawSweepFrame(shape, 1.5.dp.toPx(), base, sweepAngle, Color.White)
	}
}

fun Modifier.futureGlowVeryStrong(
	color: Color? = null,
	shape: RoundedCornerShape = RoundedCornerShape(8.dp)
) = composed {
	val config = FutureTheme.config
	val glowColor = color ?: config.colors.primary
	val transition = rememberInfiniteTransition(label = "future_glow_vstrong")
	val pulse by transition.animateFloat(
		initialValue = 0.9f, targetValue = 1.3f,
		animationSpec = infiniteRepeatable(
			tween(1400, easing = EaseInOutSine),
			RepeatMode.Reverse
		),
		label = "pulse"
	)
	val sweepAngle = rememberSweepAngle()
	val base = glowColor.copy(alpha = 0.95f)

	this
	.shadow(
		elevation = 22.dp * pulse,
		shape = shape,
		clip = false,
		ambientColor = glowColor.copy(alpha = 0.5f * pulse),
		spotColor = glowColor.copy(alpha = 0.3f * pulse)
	)
	.clip(shape)
	.drawWithContent {
		drawContent()
		drawSweepFrame(shape, 2.dp.toPx(), base, sweepAngle, Color.White, outerStrokePx = 5.dp.toPx())
	}
}

fun Modifier.futureGlowSubtle(
	color: Color? = null,
	shape: RoundedCornerShape = RoundedCornerShape(8.dp)
) = composed {
	val config = FutureTheme.config
	val glowColor = color ?: config.colors.primary
	this
	.shadow(
		elevation = 6.dp,
		shape = shape,
		clip = false,
		ambientColor = glowColor.copy(alpha = 0.15f),
		spotColor = glowColor.copy(alpha = 0.08f)
	)
	.border(0.5.dp, glowColor.copy(alpha = 0.3f), shape)
}

fun Modifier.futureHoverGlow(
	interactionSource: MutableInteractionSource,
	color: Color? = null,
	shape: RoundedCornerShape = RoundedCornerShape(8.dp),
	hoverIntensity: Float = 0.4f,
	idleIntensity: Float = 0.2f
) = composed {
	val config = FutureTheme.config
	val isHovered by interactionSource.collectIsHoveredAsState()
	val glowAlpha by animateFloatAsState(
		targetValue = if (isHovered) hoverIntensity else idleIntensity,
		animationSpec = tween(config.animation.defaultMs)
	)
	val glowColor = color ?: config.colors.primary

	this
	.clip(shape)
	.background(glowColor.copy(alpha = glowAlpha * 0.2f))
	.futureGlow(
		color = glowColor.copy(alpha = glowAlpha * 0.3f),
		blurRadius = 12f,
		shape = shape,
		intensity = glowAlpha
	)
}

fun Modifier.futureAnimatedBorder(
	color: Color? = null,
	shape: RoundedCornerShape = RoundedCornerShape(8.dp),
	width: Dp = 1.dp,
	@Suppress("UNUSED_PARAMETER")
	halo: Boolean = false        
) = composed {
	val config = FutureTheme.config
	val glowColor = color ?: config.colors.primary
	val sweepAngle = rememberSweepAngle()
	val base = glowColor.copy(alpha = 0.75f)

	this.drawWithContent {
		drawContent()
		drawSweepFrame(shape, width.toPx(), base, sweepAngle, Color.White)
	}
}

fun Modifier.futureCornerBrackets(
	color: Color? = null,
	shape: RoundedCornerShape = RoundedCornerShape(8.dp),
	length: Dp = 14.dp,
	width: Dp = 2.dp,
	inset: Dp = 1.dp
) = composed {
	val config = FutureTheme.config
	val c = color ?: config.colors.primary
	this.drawWithContent {
		drawContent()
		val len = length.toPx()
		val sw = width.toPx()
		val ins = inset.toPx()
		val l = ins
		val t = ins
		val rgt = size.width - ins
		val btm = size.height - ins
		val tl = averageCornerRadius(shape).x
		val tr = averageCornerRadius(shape).x
		val bl = averageCornerRadius(shape).x
		val br = averageCornerRadius(shape).x

		fun tick(s: Offset, e: Offset) = drawLine(c, s, e, sw, cap = androidx.compose.ui.graphics.StrokeCap.Round)

		tick(Offset(l, t + tl), Offset(l, t))
		tick(Offset(l, t), Offset(l + tl, t))
		tick(Offset(rgt - tr, t), Offset(rgt, t))
		tick(Offset(rgt, t), Offset(rgt, t + tr))
		tick(Offset(rgt, btm - br), Offset(rgt, btm))
		tick(Offset(rgt, btm), Offset(rgt - br, btm))
		tick(Offset(l + bl, btm), Offset(l, btm))
		tick(Offset(l, btm), Offset(l, btm - bl))
	}
}
