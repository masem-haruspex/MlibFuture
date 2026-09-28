package com.mlib.future.components

import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.Shadow
import androidx.compose.runtime.Composable
import androidx.compose.foundation.text.BasicText
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shader
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.LinearGradientShader
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.ui.graphics.Brush
import androidx.compose.runtime.getValue
import com.mlib.future.FutureTheme

enum class TextVariant { Primary, Muted, Heading, Glow, Highlighted, Title }

@Composable
fun FutureText(
	text: String,
	modifier: Modifier = Modifier,
	variant: TextVariant = TextVariant.Primary,
	color: Color? = null,
	size: TextUnit? = null,
	maxLines: Int = Int.MAX_VALUE,
	overflow: TextOverflow = TextOverflow.Ellipsis,
	textAlign: TextAlign? = null,
	style: TextStyle? = null
) {
	val variantStyle = futureTextStyle(variant)

	val baseStyle = style?.let { customStyle ->
		var updated = customStyle

		if (updated.color == Color.Unspecified) {
			updated = updated.copy(color = variantStyle.color)
		}
		if (updated.brush == null && variantStyle.brush != null) {
			updated = updated.copy(brush = variantStyle.brush)
		}

		updated
	} ?: variantStyle

	val hasBrush = baseStyle.brush != null

	val withColor = when {
		color != null -> baseStyle.copy(brush = null).copy(color = color)
		hasBrush      -> baseStyle.copy(color = Color.Unspecified)
		else          -> baseStyle
	}

	val resolvedStyle = withColor.copy(
		textAlign = textAlign ?: withColor.textAlign,
		fontSize = size ?: withColor.fontSize
	)

	BasicText(
		text = text,
		modifier = modifier,
		style = resolvedStyle,
		maxLines = maxLines,
		overflow = overflow
	)
}

@Composable
fun FutureTextTitle(
	text: String,
	modifier: Modifier = Modifier,
	color: Color? = null,
	size: TextUnit? = null,
	maxLines: Int = Int.MAX_VALUE,
	overflow: TextOverflow = TextOverflow.Ellipsis,
	textAlign: TextAlign? = null,
	style: TextStyle? = null
) {
	FutureText(
		text = text, modifier = modifier, variant = TextVariant.Title, color = color,
		size = size, maxLines = maxLines, overflow = overflow, textAlign = textAlign, style = style
	)
}

@Composable
fun FutureTextHeading(
	text: String,
	modifier: Modifier = Modifier,
	color: Color? = null,
	size: TextUnit? = null,
	maxLines: Int = Int.MAX_VALUE,
	overflow: TextOverflow = TextOverflow.Ellipsis,
	textAlign: TextAlign? = null,
	style: TextStyle? = null
) {
	FutureText(
		text = text, modifier = modifier, variant = TextVariant.Heading, color = color,
		size = size, maxLines = maxLines, overflow = overflow, textAlign = textAlign, style = style
	)
}

@Composable
fun FutureTextMuted(
	text: String,
	modifier: Modifier = Modifier,
	color: Color? = null,
	size: TextUnit? = null,
	maxLines: Int = Int.MAX_VALUE,
	overflow: TextOverflow = TextOverflow.Ellipsis,
	textAlign: TextAlign? = null,
	style: TextStyle? = null
) {
	FutureText(
		text = text, modifier = modifier, variant = TextVariant.Muted, color = color,
		size = size, maxLines = maxLines, overflow = overflow, textAlign = textAlign, style = style
	)
}

private class ShimmerBrush(
	private val base: Color,
	private val progress: Float,
	private val peak: Color = Color.White,
	private val edgeAlpha: Float = 0.55f
) : ShaderBrush() {
	override fun createShader(size: Size): Shader {
		val w = size.width
		val x = -w + (3f * w) * progress
		return LinearGradientShader(
			colors = listOf(
				base.copy(alpha = edgeAlpha),
				base,
				peak,
				base,
				base.copy(alpha = edgeAlpha)
			),
			from = Offset(x - w * 0.25f, 0f),
			to = Offset(x + w * 0.25f, 0f),
			tileMode = TileMode.Clamp
		)
	}
}

@Composable
private fun futureTextStyle(variant: TextVariant): TextStyle {
	val config = FutureTheme.config
	val typography = config.typography
	val colors = config.colors
	val fonts = config.fonts
	val accent = colors.accent ?: colors.primary

	val shimmerTransition = rememberInfiniteTransition(label = "text_shimmer")
	val shimmerProgress by shimmerTransition.animateFloat(
		initialValue = 0f, targetValue = 1f,
		animationSpec = infiniteRepeatable(
			tween(2600, easing = LinearEasing),
			RepeatMode.Restart
		),
		label = "progress"
	)

	return when (variant) {
		TextVariant.Primary -> typography.bodyMedium.copy(
			color = colors.textPrimary,
			fontWeight = FontWeight.Medium,
			fontFamily = fonts.regular,
			shadow = Shadow(
				color = colors.primary.copy(alpha = 0.3f),
				blurRadius = 8f
			)
		)

		TextVariant.Muted -> typography.bodyMedium.copy(
			color = colors.textMuted,
			fontWeight = FontWeight.Normal,
			fontFamily = fonts.regular
		)

		TextVariant.Heading -> typography.headlineMedium.copy(
			color = colors.primary,
			fontFamily = fonts.highlight,
			fontSize = 20.sp,
			shadow = Shadow(
				color = colors.primary.copy(alpha = 0.4f),
				offset = Offset(0f, 2f),
				blurRadius = 12f
			)
		)

		TextVariant.Glow -> typography.bodyLarge.copy(
			brush = ShimmerBrush(colors.primary, shimmerProgress),
			fontWeight = FontWeight.SemiBold,
			fontFamily = fonts.regular,
			shadow = Shadow(
				color = colors.primary.copy(alpha = 0.6f),
				blurRadius = 16f
			)
		)

		TextVariant.Highlighted -> typography.bodyLarge.copy(
			brush = Brush.linearGradient(
				listOf(colors.primary, accent),
				start = Offset(0f, 0f),
				end = Offset(160f, 0f)
			),
			fontWeight = FontWeight.SemiBold,
			fontFamily = fonts.highlight,
			shadow = Shadow(
				color = colors.primary.copy(alpha = 0.6f),
				blurRadius = 12f
			)
		)

		TextVariant.Title -> typography.titleMedium.copy(
			brush = ShimmerBrush(
				base = colors.primary,
				progress = shimmerProgress,
				peak = lerp(colors.primary, Color.White, 0.05f),
				edgeAlpha = 0.82f
			),
			fontFamily = fonts.title,
			fontWeight = FontWeight.Bold,
			letterSpacing = 2.sp,
			shadow = Shadow(
				color = colors.primary.copy(alpha = 0.4f),
				offset = Offset(0f, 2f),
				blurRadius = 16f
			)
		)
	}
}
