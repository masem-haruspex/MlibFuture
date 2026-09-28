package com.mlib.future.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mlib.future.modifiers.futureSeparator
import com.mlib.future.modifiers.futureSeparatorVertical
import com.mlib.future.FutureTheme

@Composable
fun FutureSeparator(
	modifier: Modifier = Modifier,
	thickness: Dp = 0.5.dp,
	color: Color? = null,
	glowIntensity: Float = 1f
) {
	Box(
		modifier = modifier
		.futureSeparator(
			coreColor = color,
			thickness = thickness,
			glowIntensity = glowIntensity
		)
		.height(thickness * 5)
	)
}

@Composable
fun FutureVerticalSeparator(
	modifier: Modifier = Modifier,
	thickness: Dp = 1.dp,
	color: Color? = null
) {
	Box(
		modifier = modifier
		.futureSeparatorVertical(
			color = color,
			thickness = thickness
		)
		.width(thickness * 5)
	)
}
