package com.mlib.future.modifiers

import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.animation.core.EaseInOutSine
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.mlib.future.BackgroundAnimationType
import com.mlib.future.FutureTheme
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.PI

@Composable
fun FutureScreenBackground(
	modifier: Modifier = Modifier,
	type: BackgroundAnimationType = FutureTheme.config.backgroundAnimation
) {
	when (type) {
		BackgroundAnimationType.NONE -> Unit
		BackgroundAnimationType.RADIAL_PULSE -> AnimatedRadialGlow(modifier)
		BackgroundAnimationType.CYBER_GRID -> AnimatedCyberGrid(modifier)
		BackgroundAnimationType.SCANLINE_FLOW -> AnimatedScanlines(modifier)
		BackgroundAnimationType.HEX_PATTERN -> AnimatedHexPattern(modifier)
	}
}

@Composable
private fun AnimatedRadialGlow(modifier: Modifier = Modifier) {
	val config = FutureTheme.config
	val accent = config.colors.accent ?: config.colors.primary
	val transition = rememberInfiniteTransition(label = "radial_pulse")
	val scale by transition.animateFloat(
		initialValue = 0.8f,
		targetValue = 1.25f,
		animationSpec = infiniteRepeatable(
			animation = tween(4000, easing = EaseInOutSine),
			repeatMode = RepeatMode.Reverse
		),
		label = "scale"
	)
	val scale2 by transition.animateFloat(
		initialValue = 1.2f,
		targetValue = 0.75f,
		animationSpec = infiniteRepeatable(
			animation = tween(5200, easing = EaseInOutSine),
			repeatMode = RepeatMode.Reverse
		),
		label = "scale2"
	)

	Canvas(modifier = modifier.fillMaxSize()) {
		val radius = size.minDimension * scale * 0.6f
		drawRect(
			brush = Brush.radialGradient(
				colors = listOf(
					config.colors.primary.copy(alpha = 0.14f),
					Color.Transparent
				),
				center = Offset(size.width / 2, size.height / 2),
				radius = radius
			)
		)
		drawRect(
			brush = Brush.radialGradient(
				colors = listOf(
					accent.copy(alpha = 0.10f),
					Color.Transparent
				),
				center = Offset(size.width / 2, size.height * 0.28f),
				radius = size.minDimension * scale2 * 0.55f
			)
		)
		drawRect(
			brush = Brush.radialGradient(
				colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.35f)),
				center = Offset(size.width / 2, size.height / 2),
				radius = size.maxDimension * 0.75f
			)
		)
	}
}

@Composable
private fun AnimatedCyberGrid(modifier: Modifier = Modifier) {
	val config = FutureTheme.config
	val transition = rememberInfiniteTransition(label = "cyber_grid")
	val offset by transition.animateFloat(
		initialValue = 0f,
		targetValue = 1f,
		animationSpec = infiniteRepeatable(
			animation = tween(8000, easing = LinearEasing),
			repeatMode = RepeatMode.Restart
		),
		label = "grid_offset"
	)
	val offset2 by transition.animateFloat(
		initialValue = 1f,
		targetValue = 0f,
		animationSpec = infiniteRepeatable(
			animation = tween(12000, easing = LinearEasing),
			repeatMode = RepeatMode.Restart
		),
		label = "grid_offset2"
	)

	Canvas(modifier = modifier.fillMaxSize()) {
		val spacing = 40.dp.toPx()

		val startX = -spacing + (spacing * 2) * offset
		var x = startX
		while (x < size.width) {
			drawLine(Color(0x0AFFFFFF), Offset(x, 0f), Offset(x, size.height), 1.dp.toPx())
			x += spacing
		}
		var y = 0f
		while (y < size.height) {
			drawLine(Color(0x0AFFFFFF), Offset(0f, y), Offset(size.width, y), 1.dp.toPx())
			y += spacing
		}

		val spacing2 = spacing * 4
		val startX2 = -spacing2 + (spacing2 * 2) * offset2
		var x2 = startX2
		while (x2 < size.width) {
			drawLine(Color(0x06FFFFFF), Offset(x2, 0f), Offset(x2, size.height), 1.dp.toPx())
			x2 += spacing2
		}

		drawRect(
			brush = Brush.radialGradient(
				colors = listOf(config.colors.primary.copy(alpha = 0.07f), Color.Transparent),
				center = Offset(size.width / 2, size.height / 2),
				radius = size.minDimension * 0.55f
			)
		)
		drawRect(
			brush = Brush.radialGradient(
				colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.3f)),
				center = Offset(size.width / 2, size.height / 2),
				radius = size.maxDimension * 0.75f
			)
		)
	}
}

@Composable
private fun AnimatedScanlines(modifier: Modifier = Modifier) {
	val config = FutureTheme.config
	val transition = rememberInfiniteTransition(label = "scanlines")
	val y by transition.animateFloat(
		initialValue = 0f,
		targetValue = 1f,
		animationSpec = infiniteRepeatable(
			animation = tween(2000, easing = LinearEasing),
			repeatMode = RepeatMode.Restart
		),
		label = "scan_y"
	)
	val bandY by transition.animateFloat(
		initialValue = 0f,
		targetValue = 1f,
		animationSpec = infiniteRepeatable(
			animation = tween(6000, easing = LinearEasing),
			repeatMode = RepeatMode.Restart
		),
		label = "band_y"
	)

	Canvas(modifier = modifier.fillMaxSize()) {
		val lineHeight = 2.dp.toPx()
		val spacing = 20.dp.toPx()
		var currentY = -lineHeight + (size.height + spacing) * y

		while (currentY < size.height) {
			drawRect(
				color = Color(0x08FFFFFF),
				topLeft = Offset(0f, currentY),
				size = Size(size.width, lineHeight)
			)
			currentY += spacing
		}

		val bandPos = -size.height * 0.25f + size.height * 1.5f * bandY
		drawRect(
			brush = Brush.verticalGradient(
				colors = listOf(Color.Transparent, config.colors.primary.copy(alpha = 0.05f), Color.Transparent),
				startY = bandPos,
				endY = bandPos + size.height * 0.25f
			),
			topLeft = Offset(0f, 0f),
			size = size
		)
		drawRect(
			brush = Brush.radialGradient(
				colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.3f)),
				center = Offset(size.width / 2, size.height / 2),
				radius = size.maxDimension * 0.75f
			)
		)
	}
}

@Composable
private fun AnimatedHexPattern(modifier: Modifier = Modifier) {
	val config = FutureTheme.config
	val transition = rememberInfiniteTransition(label = "hex_pulse")
	val pulse by transition.animateFloat(
		initialValue = 0.04f,
		targetValue = 0.12f,
		animationSpec = infiniteRepeatable(
			animation = tween(3000, easing = EaseInOutSine),
			repeatMode = RepeatMode.Reverse
		),
		label = "hex_alpha"
	)

	Canvas(modifier = modifier.fillMaxSize()) {
		val hexRadius = 20.dp.toPx()
		val hexHeight = hexRadius * 2
		val hexWidth = kotlin.math.sqrt(3f) * hexRadius
		val patternColor = config.colors.primary.copy(alpha = pulse)
		val stroke = 1.dp.toPx()

		val hexPath = Path().apply {
			for (i in 0 until 6) {
				val angle = PI / 6.0 + i * PI / 3.0
				val px = (cos(angle) * hexRadius).toFloat()
				val py = (sin(angle) * hexRadius).toFloat()
				if (i == 0) moveTo(px, py) else lineTo(px, py)
			}
			close()
		}

		val rows = (size.height / (hexHeight * 0.75f)).toInt() + 1
		val cols = (size.width / hexWidth).toInt() + 1

		for (row in 0..rows) {
			for (col in 0..cols) {
				val xOffset = if (row % 2 == 0) 0f else hexWidth / 2
				val cx = col * hexWidth + xOffset
				val cy = row * (hexHeight * 0.75f)

				translate(left = cx, top = cy) {
					drawPath(
						path  = hexPath,
						color = patternColor,
						style = Stroke(stroke)
					)
				}
			}
		}

		drawRect(
			brush = Brush.radialGradient(
				colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.3f)),
				center = Offset(size.width / 2, size.height / 2),
				radius = size.maxDimension * 0.75f
			)
		)
	}
}
