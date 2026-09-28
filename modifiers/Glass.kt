package com.mlib.future.modifiers

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Shape
import com.mlib.future.FutureTheme

fun Modifier.futureGlass(
	backgroundColor: Color? = null,
	shape: RoundedCornerShape = RoundedCornerShape(12.dp)
) = composed {
	val bg = backgroundColor ?: Color(0x15FFFFFF)
	this
	.shadow(4.dp, shape, clip = false, ambientColor = Color(0x33FFFFFF), spotColor = Color(0x1AFFFFFF))
	.clip(shape)
	.background(
		Brush.linearGradient(
			listOf(Color(0x22FFFFFF), bg, Color(0x08FFFFFF))
		)
	)
	.border(0.5.dp, Color(0x33FFFFFF), shape)
}

fun Modifier.futureGlassElevated(
	backgroundColor: Color? = null,
	shape: RoundedCornerShape = RoundedCornerShape(12.dp)
) = composed {
	val bg = backgroundColor ?: Color(0x20FFFFFF)
	this
	.shadow(10.dp, shape, clip = false, ambientColor = Color(0x40FFFFFF), spotColor = Color(0x28FFFFFF))
	.clip(shape)
	.background(
		Brush.linearGradient(
			listOf(Color(0x33FFFFFF), bg, Color(0x0CFFFFFF))
		)
	)
	.futureGlowSubtle(color = Color.White.copy(alpha = 0.6f), shape = shape)
}

fun Modifier.futureAutoGlass(shape: Shape = RoundedCornerShape(12.dp)) = composed {
	val config = FutureTheme.config
	if (config.glassMode) {
		this
		.shadow(8.dp, shape, clip = false, ambientColor = config.colors.primary.copy(alpha = 0.25f), spotColor = config.colors.primary.copy(alpha = 0.15f))
		.clip(shape)
		.background(
			Brush.linearGradient(
				listOf(Color(0x1EFFFFFF), Color(0x12FFFFFF), Color(0x08FFFFFF))
			)
		)
		.border(0.5.dp, config.colors.primary.copy(alpha = 0.25f), shape)
	} else this
}
