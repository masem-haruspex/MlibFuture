package com.mlib.future.modifiers

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.animateColorAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mlib.future.FutureTheme
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.drawscope.withTransform

private fun buildSparkBrush(
	color: Color,
	centerX: Float,
	halfWidth: Float,
	samples: Int = 121
): Brush {
	val peak = lerp(color, Color.White, 0.85f)
	val colors = List(samples) { i ->
		val t = i.toFloat() / (samples - 1)
		val s = kotlin.math.sin(kotlin.math.PI.toFloat() * t)
		val env = s * s
		lerp(color, peak, env).copy(alpha = env)
	}
	return Brush.horizontalGradient(
		colors = colors,
		startX = centerX - halfWidth,
		endX = centerX + halfWidth
	)
}

fun Modifier.futureUnderline(
	color: Color? = null,
	height: Dp = 2.dp,
	widthPercent: Float = 1f,
	glowIntensity: Float = 0.7f,
	top: Boolean = false,
	showSpark: Boolean = true,
	strength: Float = 0.7f          
) = composed {
	val config = FutureTheme.config
	val lineColor = color ?: config.colors.primary
	val density = LocalDensity.current
	val strokePx = with(density) { height.toPx() }

	val transition = rememberInfiniteTransition(label = "underline_flow")
	val tState = transition.animateFloat(
		initialValue = 0f,
		targetValue = 1f,
		animationSpec = infiniteRepeatable(
			animation = tween(2200, easing = LinearEasing),
			repeatMode = RepeatMode.Restart
		),
		label = "flow"
	)


	drawBehind {
		val lineWidth = size.width * widthPercent
		val startX = (size.width - lineWidth) / 2
		val y = if (top) strokePx / 2 else size.height - strokePx / 2

		drawLine(
			color = lineColor.copy(alpha = glowIntensity * 0.15f * strength),
			start = Offset(startX, y),
			end = Offset(startX + lineWidth, y),
			strokeWidth = strokePx * 3f * strength
		)
		drawLine(
			color = lineColor.copy(alpha = 0.35f * strength),
			start = Offset(startX, y),
			end = Offset(startX + lineWidth, y),
			strokeWidth = strokePx
		)

		if (!showSpark) return@drawBehind

		val t = tState.value
		val sparkX = startX - lineWidth * 0.05f + (lineWidth * 1.10f) * t

		val hRadius = lineWidth * 0.08f
		val vRadius = strokePx * 3f * strength

		val peak = lerp(lineColor, Color.White, 0.85f * strength)

		withTransform({
			translate(left = sparkX, top = y)
			scale(scaleX = 1f, scaleY = vRadius / hRadius, pivot = Offset.Zero)
		}) {
			drawCircle(
				brush = Brush.radialGradient(
					colors = listOf(
						peak,
						lerp(lineColor, peak, 0.5f + 0.5f * (1f - strength)),
						lineColor,
						lineColor.copy(alpha = 0f)
					),
					center = Offset.Zero,
					radius = hRadius
				),
				radius = hRadius,
				center = Offset.Zero,
				blendMode = BlendMode.Plus
			)
		}
	}
}

fun Modifier.futureUnderlineAnimated(
	color: Color? = null,
	height: Dp = 2.dp,
	progress: Float = 1f
) = composed {
	val config = FutureTheme.config
	val lineColor = color ?: config.colors.primary
	val density = LocalDensity.current
	val strokePx = with(density) { height.toPx() }

	drawBehind {
		val lineWidth = size.width * progress
		val startX = (size.width - lineWidth) / 2
		val y = size.height - strokePx / 2

		drawLine(
			color = lineColor.copy(alpha = 0.3f),
			start = Offset(startX, y),
			end = Offset(startX + lineWidth, y),
			strokeWidth = strokePx * 3
		)
		drawLine(
			brush = Brush.linearGradient(
				listOf(lineColor.copy(alpha = 0.4f), lineColor, Color.White),
				start = Offset(startX, y),
				end = Offset(startX + lineWidth, y)
			),
			start = Offset(startX, y),
			end = Offset(startX + lineWidth, y),
			strokeWidth = strokePx
		)
	}
}

fun Modifier.futureInputUnderline(
	focused: Boolean,
	height: Dp = 2.dp
) = composed {
	val config = FutureTheme.config
	val glowAlpha by animateFloatAsState(
		targetValue = if (focused) 0.8f else 0.2f,
		animationSpec = tween(config.animation.defaultMs)
	)
	val underlineColor by animateColorAsState(
		targetValue = config.colors.primary,
		animationSpec = tween(config.animation.defaultMs)
	)
	val density = LocalDensity.current
	val strokePx = with(density) { height.toPx() }

	drawBehind {
		val centerY = size.height / 2

		drawLine(
			color = underlineColor.copy(alpha = glowAlpha * 0.4f),
			start = Offset(0f, centerY),
			end = Offset(size.width, centerY),
			strokeWidth = strokePx * 3
		)
		drawLine(
			color = underlineColor.copy(alpha = 0.5f + glowAlpha * 0.5f),
			start = Offset(0f, centerY),
			end = Offset(size.width, centerY),
			strokeWidth = strokePx
		)
	}
}
