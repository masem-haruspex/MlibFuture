package com.mlib.future.modifiers

import androidx.compose.runtime.getValue
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalDensity
import com.mlib.future.FutureTheme

fun Modifier.futureSeparator(
	coreColor: Color? = null,
	glowColor: Color? = null,
	thickness: Dp = 0.5.dp,
	glowIntensity: Float = 1.0f,
	gradientEdgeFade: Boolean = true
) = composed {
	val config = FutureTheme.config
	val core = coreColor ?: config.colors.primary
	val glow = glowColor ?: core.copy(alpha = 0.6f)
	val density = LocalDensity.current
	val strokePx = with(density) { thickness.toPx() }
	val glowStrokePx = strokePx * 5f

	val transition = rememberInfiniteTransition(label = "separator_flow")
	val t by transition.animateFloat(
		initialValue = 0f,
		targetValue = 1f,
		animationSpec = infiniteRepeatable(
			animation = tween(3200, easing = LinearEasing),
			repeatMode = RepeatMode.Restart
		),
		label = "flow"
	)

	drawBehind {
		val centerY = size.height / 2
		val insetX = if (gradientEdgeFade) strokePx * 2 else 0f

		drawLine(
			color = glow.copy(alpha = glowIntensity * 0.25f),
			start = Offset(insetX, centerY),
			end = Offset(size.width - insetX, centerY),
			strokeWidth = glowStrokePx,
			cap = StrokeCap.Round
		)
		drawLine(
			color = glow.copy(alpha = glowIntensity * 0.5f),
			start = Offset(insetX, centerY),
			end = Offset(size.width - insetX, centerY),
			strokeWidth = strokePx * 3f,
			cap = StrokeCap.Round
		)

		val seg = size.width * 0.22f
		val sx = -seg + (size.width + seg) * t
		val flowBrush = Brush.linearGradient(
			colors = listOf(
				Color.Transparent,
				glow.copy(alpha = 0.8f),
				core.copy(alpha = 0.95f),
				glow.copy(alpha = 0.8f),
				Color.Transparent
			),
			start = Offset(sx, centerY),
			end = Offset(sx + seg, centerY)
		)
		drawLine(
			brush = flowBrush,
			start = Offset(0f, centerY),
			end = Offset(size.width, centerY),
			strokeWidth = strokePx * 3f,
			cap = StrokeCap.Round
		)
		drawLine(
			brush = flowBrush,
			start = Offset(0f, centerY),
			end = Offset(size.width, centerY),
			strokeWidth = strokePx,
			cap = StrokeCap.Round
		)
	}
}

fun Modifier.futureSeparatorVertical(
	color: Color? = null,
	thickness: Dp = 1.dp,
	glowColor: Color? = null
) = composed {
	val config = FutureTheme.config
	val core = color ?: config.colors.primary
	val glow = glowColor ?: config.colors.primary
	val density = LocalDensity.current
	val strokePx = with(density) { thickness.toPx() }

	val transition = rememberInfiniteTransition(label = "separator_v_flow")
	val t by transition.animateFloat(
		initialValue = 0f,
		targetValue = 1f,
		animationSpec = infiniteRepeatable(
			animation = tween(3200, easing = LinearEasing),
			repeatMode = RepeatMode.Restart
		),
		label = "flow"
	)

	drawBehind {
		val centerX = size.width / 2
		drawLine(
			color = glow.copy(alpha = 0.15f),
			start = Offset(centerX, 0f),
			end = Offset(centerX, size.height),
			strokeWidth = strokePx * 4
		)
		drawLine(
			color = core.copy(alpha = 0.5f),
			start = Offset(centerX, 0f),
			end = Offset(centerX, size.height),
			strokeWidth = strokePx
		)

		val seg = size.height * 0.22f
		val sy = -seg + (size.height + seg) * t
		val flowBrush = Brush.verticalGradient(
			colors = listOf(Color.Transparent, core.copy(alpha = 0.9f), Color.White, core.copy(alpha = 0.9f), Color.Transparent),
			startY = sy,
			endY = sy + seg
		)
		drawLine(flowBrush, Offset(centerX, 0f), Offset(centerX, size.height), strokePx * 2f)
	}
}
