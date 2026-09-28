package com.mlib.future.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mlib.future.modifiers.*
import com.mlib.future.FutureTheme
import androidx.compose.ui.graphics.Brush
import com.mlib.future.components.FutureText
import com.mlib.future.modifiers.futureSeparator

@Composable
fun FutureToggle(
	checked: Boolean,
	onCheckedChange: (Boolean) -> Unit,
	modifier: Modifier = Modifier,
	enabled: Boolean = true
) {
	val config = FutureTheme.config
	val interactionSource = remember { MutableInteractionSource() }
	val isHovered by interactionSource.collectIsHoveredAsState()

	val trackColor by animateColorAsState(
		targetValue = if (checked) config.colors.primary.copy(alpha = 0.3f) else config.colors.componentBg,
		animationSpec = tween(config.animation.defaultMs)
	)
	val thumbColor by animateColorAsState(
		targetValue = if (checked) config.colors.primary else config.colors.textMuted,
		animationSpec = tween(config.animation.defaultMs)
	)
	val thumbOffset by animateFloatAsState(
		targetValue = if (checked) 1f else 0f,
		animationSpec = tween(config.animation.defaultMs, easing = EaseOutQuart)
	)
	val glowAlpha by animateFloatAsState(
		targetValue = if (checked) 0.6f else if (isHovered) 0.3f else 0f,
		animationSpec = tween(config.animation.defaultMs)
	)

	Box(
		modifier = modifier
		.width(48.dp)
		.height(28.dp)
		.clip(RoundedCornerShape(14.dp))
		.background(trackColor)
		.drawBehind {
			if (checked) {
				drawRect(
					Brush.horizontalGradient(
						listOf(
							config.colors.primary.copy(alpha = 0.25f),
							(config.colors.accent ?: config.colors.primary).copy(alpha = 0.35f)
						)
					)
				)
			}
		}
		.border(
			1.dp,
			if (checked) config.colors.primary else config.colors.textMuted,
			RoundedCornerShape(14.dp)
		)
		.futureGlow(
			color = config.colors.primary.copy(alpha = glowAlpha * 0.3f),
			blurRadius = 12f,
			shape = RoundedCornerShape(14.dp),
			intensity = glowAlpha
		)
		.clickable(
			interactionSource = interactionSource,
			indication = null,
			enabled = enabled,
			onClick = { onCheckedChange(!checked) }
		)
		.padding(horizontal = 4.dp),
		contentAlignment = Alignment.CenterStart
	) {
		val thumbSize = 20.dp
		val trackWidth = 48.dp - 8.dp
		val offsetX = (trackWidth - thumbSize) * thumbOffset

		Box(
			modifier = Modifier
			.size(thumbSize)
			.offset(x = offsetX)
			.clip(RoundedCornerShape(10.dp))
			.background(thumbColor)
			.drawBehind {
				if (checked) {
					drawCircle(
						Color.White.copy(alpha = 0.85f),
						radius = 3.dp.toPx(),
						center = center
					)
				}
			},
			contentAlignment = Alignment.Center
		){}
	}
}

@Composable
fun FutureCheckbox(
	checked: Boolean,
	onCheckedChange: (Boolean) -> Unit,
	modifier: Modifier = Modifier,
	enabled: Boolean = true
) {
	val config = FutureTheme.config
	val interactionSource = remember { MutableInteractionSource() }
	val isHovered by interactionSource.collectIsHoveredAsState()

	val glowAlpha by animateFloatAsState(
		targetValue = if (checked) 0.6f else if (isHovered) 0.3f else 0.1f,
		animationSpec = tween(config.animation.defaultMs)
	)
	val checkScale by animateFloatAsState(
		targetValue = if (checked) 1f else 0f,
		animationSpec = tween(config.animation.defaultMs, easing = EaseOutQuart)
	)

	Box(
		modifier = modifier
		.size(24.dp)
		.clip(RoundedCornerShape(6.dp))
		.background(if (checked) config.colors.primary.copy(alpha = 0.15f) else config.colors.componentBg)
		.border(
			if (checked) 2.dp else 1.5.dp,
			if (checked) config.colors.primary else config.colors.textMuted,
			RoundedCornerShape(6.dp)
		)
		.futureGlow(
			color = config.colors.primary.copy(alpha = glowAlpha * 0.3f),
			blurRadius = 8f,
			shape = RoundedCornerShape(6.dp),
			intensity = glowAlpha
		)
		.clickable(
			interactionSource = interactionSource,
			indication = null,
			enabled = enabled,
			onClick = { onCheckedChange(!checked) }
		),
		contentAlignment = Alignment.Center
	) {
		if (checkScale > 0.01f) {
			Box(
				modifier = Modifier
				.size(14.dp * checkScale)
				.clip(RoundedCornerShape(3.dp))
				.background(
					Brush.linearGradient(
						listOf(
							config.colors.primary,
							config.colors.accent ?: config.colors.primary
						)
					)
				)
				.drawBehind {
					val u = size.minDimension
					drawLine(
						Color.White.copy(alpha = 0.9f),
						Offset(u * 0.25f, u * 0.5f),
						Offset(u * 0.42f, u * 0.68f),
						strokeWidth = u * 0.12f,
						cap = androidx.compose.ui.graphics.StrokeCap.Round
					)
					drawLine(
						Color.White.copy(alpha = 0.9f),
						Offset(u * 0.42f, u * 0.68f),
						Offset(u * 0.75f, u * 0.3f),
						strokeWidth = u * 0.12f,
						cap = androidx.compose.ui.graphics.StrokeCap.Round
					)
				}
			)
		}
	}
}

@Composable
fun FutureRadio(
	selected: Boolean,
	onClick: () -> Unit,
	modifier: Modifier = Modifier,
	enabled: Boolean = true
) {
	val config = FutureTheme.config
	val interactionSource = remember { MutableInteractionSource() }
	val isHovered by interactionSource.collectIsHoveredAsState()

	val glowAlpha by animateFloatAsState(
		targetValue = if (selected) 0.6f else if (isHovered) 0.3f else 0.1f,
		animationSpec = tween(config.animation.defaultMs)
	)
	val innerScale by animateFloatAsState(
		targetValue = if (selected) 1f else 0f,
		animationSpec = tween(config.animation.defaultMs, easing = EaseOutQuart)
	)

	Box(
		modifier = modifier
		.size(22.dp)
		.clip(RoundedCornerShape(11.dp))
		.background(Color.Transparent)
		.border(
			if (selected) 2.5.dp else 1.5.dp,
			if (selected) config.colors.primary else config.colors.textMuted,
			RoundedCornerShape(11.dp)
		)
		.futureGlow(
			color = config.colors.primary.copy(alpha = glowAlpha * 0.3f),
			blurRadius = 8f,
			shape = RoundedCornerShape(11.dp),
			intensity = glowAlpha
		)
		.clickable(
			interactionSource = interactionSource,
			indication = null,
			enabled = enabled,
			onClick = onClick
		),
		contentAlignment = Alignment.Center
	) {
		Box(
			modifier = Modifier
			.size(12.dp * innerScale)
			.clip(RoundedCornerShape(6.dp))
			.background(
				Brush.radialGradient(
					listOf(Color.White, config.colors.primary),
					center = Offset(4.dp.value, 4.dp.value),
					radius = 14.dp.value
				)
			)
		)
	}
}

@Composable
fun FutureSlider(
	value: Float,
	onValueChange: (Float) -> Unit,
	onValueChangeFinished: (() -> Unit)? = null,
	modifier: Modifier = Modifier,
	enabled: Boolean = true,
	valueRange: ClosedFloatingPointRange<Float> = 0f..1f
) {
	val config = FutureTheme.config
	val interactionSource = remember { MutableInteractionSource() }
	val isHovered by interactionSource.collectIsHoveredAsState()
	val glowAlpha by animateFloatAsState(
		targetValue = if (isHovered) 0.8f else 0.4f,
		animationSpec = tween(config.animation.defaultMs)
	)

	val rangeSpan = valueRange.endInclusive - valueRange.start
	val progress = if (rangeSpan > 0f) (value - valueRange.start) / rangeSpan else 0f

	BoxWithConstraints(
		modifier = modifier
		.fillMaxWidth()
		.height(32.dp)
		.pointerInput(Unit) {
			detectHorizontalDragGestures(
				onDragStart = { offset ->
					if (!enabled) return@detectHorizontalDragGestures
					val fraction = (offset.x / size.width).coerceIn(0f, 1f)
					onValueChange((valueRange.start + fraction * rangeSpan).coerceIn(valueRange))
				},
				onHorizontalDrag = { change, _ ->
					if (!enabled) return@detectHorizontalDragGestures
					change.consume()
					val fraction = (change.position.x / size.width).coerceIn(0f, 1f)
					onValueChange((valueRange.start + fraction * rangeSpan).coerceIn(valueRange))
				},
				onDragEnd = {
					onValueChangeFinished?.invoke()
				}
			)
		},
		contentAlignment = Alignment.CenterStart
	) {
		val trackWidth = maxWidth
		val thumbSize = 20.dp
		val thumbOffset = (trackWidth - thumbSize) * progress

		Box(
			modifier = Modifier
			.fillMaxWidth()
			.height(4.dp)
			.clip(RoundedCornerShape(2.dp))
			.background(config.colors.componentBg)
		)

		Box(
			modifier = Modifier
			.fillMaxWidth(progress)
			.height(4.dp)
			.clip(RoundedCornerShape(2.dp))
			.background(
				Brush.horizontalGradient(
					listOf(
						config.colors.primary.copy(alpha = 0.6f),
						config.colors.accent ?: config.colors.primary
					)
				)
			)
			.drawBehind {
				val tipX = size.width
				val tipY = size.height / 2
				drawCircle(config.colors.primary.copy(alpha = 0.35f), radius = 10.dp.toPx(), center = Offset(tipX, tipY))
				drawCircle(Color.White.copy(alpha = 0.9f), radius = 2.5.dp.toPx(), center = Offset(tipX, tipY))
			}
		)

		Box(
			modifier = Modifier
			.offset(x = thumbOffset)
			.size(thumbSize)
			.clip(RoundedCornerShape(10.dp))
			.background(config.colors.componentBg)
			.border(2.dp, config.colors.primary, RoundedCornerShape(10.dp))
			.futureGlow(
				color = config.colors.primary.copy(alpha = glowAlpha),
				blurRadius = 8f,
				shape = RoundedCornerShape(10.dp),
				intensity = glowAlpha
			),
			contentAlignment = Alignment.Center
		) {
			Box(
				modifier = Modifier
				.size(8.dp)
				.clip(RoundedCornerShape(4.dp))
				.background(config.colors.primary)
			)
		}
	}
}

@Composable
fun FutureDropdown(
	expanded: Boolean,
	onExpandedChange: (Boolean) -> Unit,
	modifier: Modifier = Modifier,
	selectedText: String = "",
	options: List<String> = listOf(),
	onOptionSelected: (String) -> Unit = {},
	expandUpward: Boolean = false
) {
	val config = FutureTheme.config
	val accent = config.colors.accent ?: config.colors.primary
	val rotation by animateFloatAsState(
		targetValue = if (expanded) 180f else 0f,
		animationSpec = tween(config.animation.defaultMs, easing = EaseOutQuart),
		label = "arrow_rotation"
	)

	Column(
		modifier = modifier
		.clip(RoundedCornerShape(8.dp))
		.background(config.colors.componentBg)
		.border(
			1.dp,
			Brush.sweepGradient(
				listOf(
					config.colors.primary.copy(alpha = 0.4f),
					config.colors.primary,
					accent,
					config.colors.primary,
					config.colors.primary.copy(alpha = 0.4f)
				)
			),
			RoundedCornerShape(8.dp)
		)
	) {
		Row(
			modifier = Modifier
			.fillMaxWidth()
			.clickable { onExpandedChange(!expanded) }
			.padding(horizontal = 16.dp, vertical = 14.dp),
			horizontalArrangement = Arrangement.SpaceBetween,
			verticalAlignment = Alignment.CenterVertically
		) {
			FutureText(
				text = selectedText.ifEmpty { "SELECT..." },
				variant = if (selectedText.isNotEmpty()) TextVariant.Primary else TextVariant.Muted,
				style = FutureTheme.typography.bodyLarge.copy(letterSpacing = 1.sp)
			)
			Box(
				modifier = Modifier
				.size(24.dp)
				.graphicsLayer { rotationZ = rotation }
				.drawBehind {
					val sw = 2.dp.toPx()
					val p = 6.dp.toPx()
					val cy = size.height / 2
					drawLine(
						config.colors.primary,
						Offset(p, cy - 3.dp.toPx()),
						Offset(size.width / 2, cy + 3.dp.toPx()),
						sw,
						cap = androidx.compose.ui.graphics.StrokeCap.Round
					)
					drawLine(
						config.colors.primary,
						Offset(size.width - p, cy - 3.dp.toPx()),
						Offset(size.width / 2, cy + 3.dp.toPx()),
						sw,
						cap = androidx.compose.ui.graphics.StrokeCap.Round
					)
				}
			)
		}

		AnimatedVisibility(
			visible = expanded,
			enter = fadeIn(tween(config.animation.defaultMs)),
			exit = fadeOut(tween(config.animation.defaultMs))
		) {
			Box(
				modifier = Modifier
				.padding(horizontal = 16.dp)
				.fillMaxWidth()
				.futureSeparator(thickness = 0.5.dp)
			)
		}

		AnimatedVisibility(
			visible = expanded,
			enter = expandVertically(tween(config.animation.defaultMs, easing = EaseOutQuart)) + fadeIn(tween(config.animation.defaultMs)),
			exit = shrinkVertically(tween(config.animation.defaultMs, easing = EaseInQuart)) + fadeOut(tween(config.animation.defaultMs))
		) {
			Column(
				modifier = Modifier
				.fillMaxWidth()
				.padding(horizontal = 16.dp, vertical = 8.dp)
			) {
				options.forEachIndexed { index, option ->
					val isSelected = option == selectedText
					val interactionSource = remember { MutableInteractionSource() }
					val isHovered by interactionSource.collectIsHoveredAsState()
					val bgAlpha by animateFloatAsState(
						targetValue = if (isHovered) 0.1f else 0f,
						animationSpec = tween(config.animation.defaultMs)
					)

					Box(
						modifier = Modifier
						.fillMaxWidth()
						.clickable(
							interactionSource = interactionSource,
							indication = null,
							onClick = {
								onOptionSelected(option)
								onExpandedChange(false)
							}
						)
						.background(config.colors.primary.copy(alpha = bgAlpha))
						.padding(vertical = 10.dp, horizontal = 8.dp)
					) {
						FutureText(
							text = option,
							variant = TextVariant.Primary,
							color = if (isSelected) config.colors.primary else config.colors.textPrimary,
							style = FutureTheme.typography.bodyMedium.copy(
								fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
							)
						)
					}

					if (index < options.size - 1) {
						Spacer(
							modifier = Modifier
							.padding(horizontal = 8.dp)
							.fillMaxWidth()
							.futureSeparator(thickness = 0.5.dp)
						)
					}
				}
			}
		}
	}
}

@Composable
fun FutureLabeledCheckbox(
	label: String,
	checked: Boolean,
	onCheckedChange: (Boolean) -> Unit,
	modifier: Modifier = Modifier,
	enabled: Boolean = true
) {
	Row(
		modifier = modifier
		.clickable(enabled = enabled) { onCheckedChange(!checked) }
		.padding(vertical = 8.dp),
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.spacedBy(12.dp)
	) {
		FutureCheckbox(
			checked = checked,
			onCheckedChange = onCheckedChange,
			enabled = enabled
		)
		FutureText(
			text = label,
			variant = TextVariant.Primary
		)
	}
}

@Composable
fun FutureLabeledToggle(
	label: String,
	checked: Boolean,
	onCheckedChange: (Boolean) -> Unit,
	modifier: Modifier = Modifier,
	enabled: Boolean = true
) {
	Row(
		modifier = modifier
		.clickable(enabled = enabled) { onCheckedChange(!checked) }
		.padding(vertical = 8.dp),
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.spacedBy(10.dp)
	) {
		FutureText(
			text = label,
			variant = TextVariant.Primary
		)
		FutureToggle(
			checked = checked,
			onCheckedChange = onCheckedChange,
			enabled = enabled
		)
	}
}

@Composable
fun FutureLabeledRadio(
	label: String,
	selected: Boolean,
	onClick: () -> Unit,
	modifier: Modifier = Modifier,
	enabled: Boolean = true
) {
	Row(
		modifier = modifier
		.clickable(enabled = enabled) { onClick() }
		.padding(vertical = 8.dp),
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.spacedBy(12.dp)
	) {
		FutureRadio(
			selected = selected,
			onClick = onClick,
			enabled = enabled
		)
		FutureText(
			text = label,
			variant = TextVariant.Primary
		)
	}
}

@Composable
fun FutureLabeledSlider(
	label: String,
	value: Float,
	onValueChange: (Float) -> Unit,
	modifier: Modifier = Modifier,
	enabled: Boolean = true,
	valueRange: ClosedFloatingPointRange<Float> = 0f..1f
) {
	Column(modifier = modifier) {
		FutureText(
			text = label.uppercase(),
			variant = TextVariant.Muted,
			style = FutureTheme.typography.labelSmall.copy(letterSpacing = 1.5.sp)
		)
		Spacer(modifier = Modifier.height(8.dp))
		FutureSlider(
			value = value,
			onValueChange = onValueChange,
			enabled = enabled,
			valueRange = valueRange
		)
	}
}
