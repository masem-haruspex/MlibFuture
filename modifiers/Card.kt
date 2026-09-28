package com.mlib.future.modifiers

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import com.mlib.future.FutureTheme

fun Modifier.futureCard(
	onClick: (() -> Unit)? = null,
	shape: RoundedCornerShape? = null
) = composed {
	val config = FutureTheme.config
	val interactionSource = remember { MutableInteractionSource() }
	val isHovered by interactionSource.collectIsHoveredAsState()

	val glowAlpha by animateFloatAsState(
		targetValue = if (isHovered && onClick != null) 0.6f else 0.15f,
		animationSpec = tween(config.animation.defaultMs)
	)

	val s : RoundedCornerShape = shape ?: config.shapes.card
	val accent = config.colors.accent ?: config.colors.primary

	this
	.shadow(
		elevation = (6.dp.value + glowAlpha * 12f).dp,
		shape = s,
		clip = false,
		ambientColor = config.colors.primary.copy(alpha = glowAlpha * 0.45f),
		spotColor = config.colors.primary.copy(alpha = glowAlpha * 0.3f)
	)
	.clip(s)
	.background(config.colors.componentBg)
	.border(
		width = 1.dp,
		brush = Brush.sweepGradient(
			listOf(
				config.colors.primary.copy(alpha = 0.35f),
				config.colors.primary,
				accent,
				config.colors.primary,
				config.colors.primary.copy(alpha = 0.35f)
			)
		),
		shape = s
	)
	.then(
		if (onClick != null) {
			Modifier.clickable(
				interactionSource = interactionSource,
				indication = null,
				onClick = onClick
			)
		} else {
			Modifier
		}
	)
	.padding(20.dp)
}
