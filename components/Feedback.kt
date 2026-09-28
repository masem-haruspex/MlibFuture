package com.mlib.future.components

import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.dp
import com.mlib.future.modifiers.*
import com.mlib.future.FutureTheme
import com.mlib.future.components.FutureText

enum class ToastType { SUCCESS, ERROR, WARNING, INFO }

@Composable
fun FutureSkeleton(
	modifier: Modifier = Modifier
) {
	val config = FutureTheme.config
	val infiniteTransition = rememberInfiniteTransition(label = "shimmer")
	val shimmerTranslate by infiniteTransition.animateFloat(
		initialValue = 0f, targetValue = 1f,
		animationSpec = infiniteRepeatable(tween(1500, easing = LinearEasing), RepeatMode.Restart),
		label = "shimmer"
	)

	Box(
		modifier = modifier
		.clip(RoundedCornerShape(6.dp))
		.background(
			Brush.linearGradient(
				listOf(config.colors.componentBg, config.colors.componentBg.copy(alpha = 0.7f))
			)
		)
		.drawBehind {
			val w = size.width
			val h = size.height
			val diag = kotlin.math.sqrt(w * w + h * h)
			val x = -diag * 0.4f + (diag * 1.8f) * shimmerTranslate
			drawRect(
				brush = Brush.linearGradient(
					listOf(Color.Transparent, config.colors.primary.copy(alpha = 0.35f), Color.Transparent),
					start = Offset(x - diag * 0.2f, 0f),
					end = Offset(x, h)
				)
			)
		}
	)
}

@Composable
fun FutureToast(
	message: String,
	modifier: Modifier = Modifier,
	type: ToastType = ToastType.INFO,
	floating: Boolean = true,
	alignment: Alignment = Alignment.TopCenter,
	offset: DpOffset = DpOffset(0.dp, 72.dp)
) {
	val config = FutureTheme.config
	val color = when (type) {
		ToastType.SUCCESS -> config.colors.success
		ToastType.ERROR   -> config.colors.error
		ToastType.WARNING -> config.colors.warning
		ToastType.INFO    -> config.colors.primary
	}

	val content: @Composable () -> Unit = {
		Box(
			modifier = modifier
			.clip(RoundedCornerShape(8.dp))
			.background(config.colors.componentBg)
			.border(1.dp, color.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
			.futureGlow(
				color = color.copy(alpha = 0.3f),
				blurRadius = 16f,
				shape = RoundedCornerShape(8.dp),
				intensity = 0.6f
			)
			.futureCornerBrackets(shape = RoundedCornerShape(8.dp), length = 10.dp, width = 2.dp, color = color)
			.padding(horizontal = 20.dp, vertical = 14.dp)
		) {
			Row(
				horizontalArrangement = Arrangement.spacedBy(12.dp),
				verticalAlignment = Alignment.CenterVertically
			) {
				Box(
					modifier = Modifier
					.size(8.dp)
					.clip(RoundedCornerShape(4.dp))
					.background(
						Brush.radialGradient(
							listOf(Color.White, color),
							radius = 6.dp.value
						)
					)
					.drawBehind {
						drawCircle(color.copy(alpha = 0.3f), radius = 8.dp.toPx())
					}
				)
				FutureText(message)
			}
		}
	}

	if (floating) {
		val density = LocalDensity.current
		Popup(
			alignment = alignment,
			offset = with(density) { IntOffset(offset.x.roundToPx(), offset.y.roundToPx()) },
			properties = PopupProperties(focusable = false)
		) {
			content()
		}
	} else {
		content()
	}
}

@Composable
fun FutureProgress(
	progress: Float,
	modifier: Modifier = Modifier,
	color: Color? = null
) {
	val config = FutureTheme.config
	val c = color ?: config.colors.primary
	val accent = config.colors.accent ?: c
	val animatedProgress by animateFloatAsState(
		targetValue = progress.coerceIn(0f, 1f),
		animationSpec = tween(config.animation.defaultMs, easing = EaseOutQuart)
	)

	Box(
		modifier = modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp))
		.background(config.colors.componentBg)
	) {
		Box(
			modifier = Modifier
			.fillMaxHeight()
			.fillMaxWidth(animatedProgress)
			.clip(RoundedCornerShape(3.dp))
			.background(Brush.horizontalGradient(listOf(c.copy(alpha = 0.6f), c, accent)))
			.drawBehind {
				drawCircle(
					color = c.copy(alpha = 0.8f),
					radius = 8.dp.toPx(),
					center = Offset(size.width, size.height / 2)
				)
				drawCircle(
					Color.White.copy(alpha = 0.9f),
					radius = 2.dp.toPx(),
					center = Offset(size.width, size.height / 2)
				)
			}
		)
	}
}

@Composable
fun FutureRingProgress(
	progress: Float,
	modifier: Modifier = Modifier,
	color: Color? = null,
	strokeWidth: Float = 6f
) {
	val config = FutureTheme.config
	val c = color ?: config.colors.primary
	val accent = config.colors.accent ?: c
	val animatedProgress by animateFloatAsState(
		targetValue = progress.coerceIn(0f, 1f),
		animationSpec = tween(config.animation.slowMs, easing = EaseOutQuart)
	)

	val infiniteTransition = rememberInfiniteTransition(label = "pulse")
	val pulse by infiniteTransition.animateFloat(
		initialValue = 0.8f, targetValue = 1.2f,
		animationSpec = infiniteRepeatable(tween(2000, easing = EaseInOutSine), RepeatMode.Reverse),
		label = "pulse"
	)

	Box(modifier = modifier, contentAlignment = Alignment.Center) {
		Box(
			modifier = Modifier.fillMaxSize().drawBehind {
				val radius = (size.minDimension / 2) - strokeWidth
				drawCircle(
					color = config.colors.componentBg,
					radius = radius,
					style = androidx.compose.ui.graphics.drawscope.Stroke(width = strokeWidth)
				)
			}
		)
		Box(
			modifier = Modifier.fillMaxSize().drawBehind {
				val radius = (size.minDimension / 2) - strokeWidth
				val sweepAngle = 360f * animatedProgress
				drawArc(
					brush = Brush.sweepGradient(
						listOf(c.copy(alpha = 0.5f), accent, c),
						center = center
					),
					startAngle = -90f,
					sweepAngle = sweepAngle,
					useCenter = false,
					style = androidx.compose.ui.graphics.drawscope.Stroke(
						width = strokeWidth * pulse,
						cap = androidx.compose.ui.graphics.StrokeCap.Round
					)
				)
				val angleRad = kotlin.math.PI * (-90f + sweepAngle) / 180f
				val tipX = size.width / 2 + radius * kotlin.math.cos(angleRad).toFloat()
				val tipY = size.height / 2 + radius * kotlin.math.sin(angleRad).toFloat()
				drawCircle(c.copy(alpha = 0.45f), radius = 12f, center = Offset(tipX, tipY))
				drawCircle(Color.White.copy(alpha = 0.9f), radius = 4f, center = Offset(tipX, tipY))
			}
		)
		FutureTextHeading("${(animatedProgress * 100).toInt()}%", color = c)
	}
}

@Composable
fun FutureLoading(
	text: String = "LOADING",
	modifier: Modifier = Modifier
) {
	val config = FutureTheme.config
	val infiniteTransition = rememberInfiniteTransition(label = "loading")
	val dotOffset by infiniteTransition.animateFloat(
		initialValue = 0f, targetValue = 3f,
		animationSpec = infiniteRepeatable(tween(config.animation.flowMs * 2, easing = LinearEasing), RepeatMode.Restart),
		label = "dots"
	)
	val pulse by infiniteTransition.animateFloat(
		initialValue = 0.6f, targetValue = 1.3f,
		animationSpec = infiniteRepeatable(tween(1200, easing = EaseInOutSine), RepeatMode.Reverse),
		label = "dot_pulse"
	)
	val dots = ".".repeat(((dotOffset % 3) + 1).toInt())

	Row(
		modifier = modifier,
		horizontalArrangement = Arrangement.spacedBy(8.dp),
		verticalAlignment = Alignment.CenterVertically
	) {
		Box(
			modifier = Modifier.size(16.dp).drawBehind {
				drawCircle(
					color = config.colors.primary.copy(alpha = 0.35f),
					radius = 8.dp.toPx() * pulse,
					center = center
				)
				drawCircle(
					color = config.colors.primary,
					radius = 4.dp.toPx(),
					center = center
				)
				drawCircle(
					Color.White.copy(alpha = 0.9f),
					radius = 1.5.dp.toPx(),
					center = center
				)
			}
		)
		FutureText(
			text = text + dots,
			variant = TextVariant.Glow,
			style = FutureTheme.typography.labelLarge.copy(
				fontWeight = FontWeight.SemiBold,
				letterSpacing = 2.sp
			)
		)
	}
}
