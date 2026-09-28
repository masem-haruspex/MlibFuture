package com.mlib.future.components

import androidx.compose.ui.draw.shadow
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mlib.future.modifiers.*
import com.mlib.future.FutureTheme

@Composable
fun FutureCard(
	modifier: Modifier = Modifier,
	onClick: (() -> Unit)? = null,
	componentBg: Boolean = true,
	border: Boolean = true,
	padding: PaddingValues = PaddingValues(20.dp),
	content: @Composable () -> Unit
) {
	val config = FutureTheme.config
	val interactionSource = remember { MutableInteractionSource() }
	val isHovered by interactionSource.collectIsHoveredAsState()
	val accent = config.colors.accent ?: config.colors.primary

	val glowAlpha by animateFloatAsState(
		targetValue = if (isHovered && onClick != null) 0.6f else 0.15f,
		animationSpec = tween(config.animation.defaultMs)
	)

	val shape = config.shapes.card
	val bgColor = if (componentBg) config.colors.componentBg else config.colors.globalBg

	var cardModifier = modifier
	.shadow(
		elevation = (6f + glowAlpha * 12f).dp,
		shape = shape,
		clip = false,
		ambientColor = config.colors.primary.copy(alpha = glowAlpha * 0.45f),
		spotColor = config.colors.primary.copy(alpha = glowAlpha * 0.3f)
	)
	.clip(shape)
	.background(bgColor)

	if (border) {
		cardModifier = cardModifier.border(
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
			shape = shape
		)
	}

	if (onClick != null) {
		cardModifier = cardModifier.clickable(
			interactionSource = interactionSource,
			indication = null,
			onClick = onClick
		)
	}

	cardModifier = cardModifier.padding(padding)

	Box(
		modifier = cardModifier,
		contentAlignment = Alignment.TopStart
	) {
		Column { content() }
	}
}

@Composable
fun FutureBadge(
	text: String,
	modifier: Modifier = Modifier,
	color: Color? = null
) {
	val config = FutureTheme.config
	val c = color ?: config.colors.primary
	val badgeStyle = FutureTheme.typography.labelSmall.copy(
		fontWeight = FontWeight.SemiBold,
		letterSpacing = 1.sp
	)
	Box(
		modifier = modifier
		.clip(RoundedCornerShape(4.dp))
		.background(c.copy(alpha = 0.15f))
		.border(1.dp, c.copy(alpha = 0.4f), RoundedCornerShape(4.dp))
		.futureGlowSubtle(color = c.copy(alpha = 0.4f), shape = RoundedCornerShape(4.dp))
		.padding(horizontal = 8.dp, vertical = 4.dp),
		contentAlignment = Alignment.Center
	) {
		Box {
			FutureText(
				text = text,
				color = Color.Black,
				style = badgeStyle.copy(
					drawStyle = Stroke(
						width = 1f,
						join = StrokeJoin.Round,
						cap = StrokeCap.Round
					)
				)
			)
			FutureText(
				text = text,
				variant = TextVariant.Glow,
				color = c,
				style = badgeStyle
			)
		}
	}
}

@Composable
fun FutureAvatar(
	text: String,
	modifier: Modifier = Modifier,
	color: Color? = null
) {
	val config = FutureTheme.config
	val c = color ?: config.colors.primary
	val initials = text.split(" ").take(2).map { it.firstOrNull()?.uppercase() ?: "" }.joinToString("")
	Box(
		modifier = modifier
		.size(40.dp)
		.clip(config.shapes.avatar)
		.background(c.copy(alpha = 0.15f))
		.border(1.5.dp, c.copy(alpha = 0.5f), config.shapes.avatar)
		.futureGlow(color = c.copy(alpha = 0.3f), blurRadius = 12f, shape = config.shapes.avatar, intensity = 0.5f),
		contentAlignment = Alignment.Center
	) {
		FutureText(initials,
		variant = TextVariant.Glow,
		color = c,
		style = FutureTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
	)
}
}

@Composable
fun FutureTextList(
	items: List<String>,
	modifier: Modifier = Modifier,
	onItemClick: ((Int) -> Unit)? = null
) {
	val config = FutureTheme.config
	val accent = config.colors.accent ?: config.colors.primary
	Column(
		modifier = modifier
		.clip(RoundedCornerShape(8.dp))
		.background(config.colors.componentBg)
		.border(
			1.dp,
			Brush.sweepGradient(
				listOf(
					config.colors.primary.copy(alpha = 0.35f),
					config.colors.primary,
					accent,
					config.colors.primary,
					config.colors.primary.copy(alpha = 0.35f)
				)
			),
			RoundedCornerShape(8.dp)
		)
	) {
		items.forEachIndexed { index, item ->
			val interactionSource = remember { MutableInteractionSource() }
			val isHovered by interactionSource.collectIsHoveredAsState()
			val bgAlpha by animateFloatAsState(
				targetValue = if (isHovered && onItemClick != null) 0.1f else 0f,
				animationSpec = tween(config.animation.defaultMs)
			)

			var rowModifier = Modifier.fillMaxWidth().background(config.colors.primary.copy(alpha = bgAlpha))
			if (onItemClick != null) {
				rowModifier = rowModifier.clickable(
					interactionSource = interactionSource,
					indication = null,
					onClick = { onItemClick(index) }
				)
			}
			rowModifier = rowModifier.padding(horizontal = 16.dp, vertical = 12.dp)

			Box(modifier = rowModifier) {
				Row(
					verticalAlignment = Alignment.CenterVertically,
					horizontalArrangement = Arrangement.spacedBy(12.dp)
				) {
					Box(
						modifier = Modifier.size(6.dp).clip(RoundedCornerShape(2.dp))
						.background(config.colors.primary)
					)
					FutureText(item,
					variant = TextVariant.Primary,
					maxLines = 1,
					overflow = TextOverflow.Ellipsis
				)
			}
		}
		if (index < items.size - 1) {
			Spacer(modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth().futureSeparator())
		}
	}
}
}

@Composable
fun FutureTable(
	headers: List<String>,
	rows: List<List<String>>,
	modifier: Modifier = Modifier,
	onRowClick: ((Int) -> Unit)? = null,
	columnWeights: List<Float>? = null
) {
	val config = FutureTheme.config
	val accent = config.colors.accent ?: config.colors.primary
	Column(
		modifier = modifier
		.clip(RoundedCornerShape(8.dp))
		.background(config.colors.componentBg)
		.border(
			1.dp,
			Brush.sweepGradient(
				listOf(
					config.colors.primary.copy(alpha = 0.35f),
					config.colors.primary,
					accent,
					config.colors.primary,
					config.colors.primary.copy(alpha = 0.35f)
				)
			),
			RoundedCornerShape(8.dp)
		)
	) {
		Row(
			modifier = Modifier.fillMaxWidth().background(config.colors.componentBg)
			.padding(horizontal = 16.dp, vertical = 12.dp)
		) {
			headers.forEachIndexed { index, header ->
				val weight = columnWeights?.getOrNull(index) ?: 1f
				FutureText(header,
				modifier = Modifier.weight(weight),
				variant = TextVariant.Glow,
				style = FutureTheme.typography.labelSmall.copy(
					fontWeight = FontWeight.SemiBold,
					letterSpacing = 1.sp
				)
			)
		}
	}
	Spacer(modifier = Modifier.fillMaxWidth().futureSeparator())
	rows.forEachIndexed { rowIndex, row ->
		val interactionSource = remember { MutableInteractionSource() }
		val isHovered by interactionSource.collectIsHoveredAsState()
		val bgAlpha by animateFloatAsState(
			targetValue = if (isHovered && onRowClick != null) 0.05f else 0f,
			animationSpec = tween(config.animation.defaultMs)
		)

		var rowModifier = Modifier.fillMaxWidth().background(config.colors.primary.copy(alpha = bgAlpha))
		if (onRowClick != null) {
			rowModifier = rowModifier.clickable(
				interactionSource = interactionSource,
				indication = null,
				onClick = { onRowClick(rowIndex) }
			)
		}
		rowModifier = rowModifier.padding(horizontal = 16.dp, vertical = 12.dp)

		Row(modifier = rowModifier) {
			row.forEachIndexed { cellIndex, cell ->
				val weight = columnWeights?.getOrNull(cellIndex) ?: 1f
				FutureText(cell,
				modifier = Modifier.weight(weight),
				variant = TextVariant.Primary,
				maxLines = 1,
				overflow = TextOverflow.Ellipsis
			)
		}
	}
	if (rowIndex < rows.size - 1) {
		Spacer(modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth().futureSeparator(thickness = 0.5.dp))
	}
}
	}
}

@Composable
fun FutureAccordion(
	title: String = "",
	expanded: Boolean,
	onToggle: () -> Unit,
	modifier: Modifier = Modifier,
	content: @Composable () -> Unit
) {
	val config = FutureTheme.config
	val accent = config.colors.accent ?: config.colors.primary
	val rotation by animateFloatAsState(
		targetValue = if (expanded) 180f else 0f,
		animationSpec = tween(config.animation.defaultMs, easing = EaseOutQuart)
	)

	Column(
		modifier = modifier
		.clip(RoundedCornerShape(8.dp))
		.background(config.colors.componentBg)
		.border(
			1.dp,
			Brush.sweepGradient(
				listOf(
					config.colors.primary.copy(alpha = 0.35f),
					config.colors.primary,
					accent,
					config.colors.primary,
					config.colors.primary.copy(alpha = 0.35f)
				)
			),
			RoundedCornerShape(8.dp)
		)
	) {
		Row(
			modifier = Modifier.fillMaxWidth().clickable(onClick = onToggle)
			.padding(horizontal = 16.dp, vertical = 14.dp),
			horizontalArrangement = Arrangement.SpaceBetween,
			verticalAlignment = Alignment.CenterVertically
		) {
			FutureTextHeading(title)
			Box(
				modifier = Modifier.size(24.dp).graphicsLayer { rotationZ = rotation }.drawBehind {
					val sw = 2.dp.toPx()
					val p = 6.dp.toPx()
					drawLine(config.colors.primary, Offset(p, size.height / 2 - 3.dp.toPx()), Offset(size.width / 2, size.height / 2 + 3.dp.toPx()), sw, cap = StrokeCap.Round)
					drawLine(config.colors.primary, Offset(size.width - p, size.height / 2 - 3.dp.toPx()), Offset(size.width / 2, size.height / 2 + 3.dp.toPx()), sw, cap = StrokeCap.Round)
				}
			)
		}
		AnimatedVisibility(
			visible = expanded,
			enter = expandVertically(tween(config.animation.defaultMs, easing = EaseOutQuart)) + fadeIn(tween(config.animation.defaultMs)),
			exit = shrinkVertically(tween(config.animation.defaultMs, easing = EaseInQuart)) + fadeOut(tween(config.animation.defaultMs))
		) {
			Column(
				modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp)
			) {
				Spacer(
					modifier = Modifier.fillMaxWidth().height(4.dp).futureSeparator(thickness = 1.dp)
				)
				Spacer(modifier = Modifier.height(12.dp))
				content()
			}
		}
	}
}

@Composable
fun FutureTabs(
	selected: Int,
	onTabSelected: (Int) -> Unit,
	modifier: Modifier = Modifier,
	tabs: List<String> = listOf()
) {
	val config = FutureTheme.config
	val accent = config.colors.accent ?: config.colors.primary
	Row(
		modifier = modifier
		.clip(RoundedCornerShape(8.dp))
		.background(config.colors.componentBg)
		.border(
			1.dp,
			Brush.sweepGradient(
				listOf(
					config.colors.primary.copy(alpha = 0.35f),
					config.colors.primary,
					accent,
					config.colors.primary,
					config.colors.primary.copy(alpha = 0.35f)
				)
			),
			RoundedCornerShape(8.dp)
		)
		.padding(4.dp),
		horizontalArrangement = Arrangement.spacedBy(4.dp)
	) {
		tabs.forEachIndexed { index, tab ->
			val isSelected = index == selected
			val interactionSource = remember { MutableInteractionSource() }
			val isHovered by interactionSource.collectIsHoveredAsState()

			val bgAlpha by animateFloatAsState(
				targetValue = if (isSelected) 0.15f else if (isHovered) 0.05f else 0f,
				animationSpec = tween(config.animation.defaultMs)
			)
			val textColor by animateColorAsState(
				targetValue = if (isSelected) config.colors.primary else config.colors.textPrimary,
				animationSpec = tween(config.animation.defaultMs)
			)
			val glowAlpha by animateFloatAsState(
				targetValue = if (isSelected) 0.6f else 0f,
				animationSpec = tween(config.animation.defaultMs)
			)

			Box(
				modifier = Modifier
				.clip(RoundedCornerShape(6.dp))
				.weight(1f)
				.clickable(interactionSource = interactionSource, indication = null) { onTabSelected(index) }
				.background(config.colors.primary.copy(alpha = bgAlpha))
				.futureGlow(
					color = config.colors.primary.copy(alpha = glowAlpha * 0.3f),
					blurRadius = 8f,
					shape = RoundedCornerShape(6.dp),
					intensity = glowAlpha
				)
				.padding(vertical = 10.dp),
				contentAlignment = Alignment.Center
			) {
				FutureText(tab,
				variant = TextVariant.Primary,
				color = textColor,
				style = FutureTheme.typography.labelMedium.copy(
					fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
					letterSpacing = 1.sp
				)
			)
		}
	}
}
}

@Composable
fun FuturePagination(
	currentPage: Int,
	onPageChange: (Int) -> Unit,
	modifier: Modifier = Modifier,
	totalPages: Int = 1
) {
	val config = FutureTheme.config
	if (totalPages <= 1) return

	Row(
		modifier = modifier,
		horizontalArrangement = Arrangement.spacedBy(8.dp),
		verticalAlignment = Alignment.CenterVertically
	) {
		FutureButton("",
		onClick = { if (currentPage > 0) onPageChange(currentPage - 1) },
		enabled = currentPage > 0,
		type = ButtonType.Primary,
		icon = {
			FutureText(
				text = "<",
				variant = TextVariant.Primary,
				color = if (currentPage > 0) config.colors.textPrimary else config.colors.textMuted,
				textAlign = TextAlign.Center
			)
		}
	)

	PaginationPageNumber(0, currentPage, onPageChange)

	if (currentPage > 3) {
		FutureTextMuted("...",
		modifier = Modifier.padding(horizontal = 4.dp)
	)
}

val startPage = (currentPage - 2).coerceAtLeast(1)
val endPage = (currentPage + 2).coerceAtMost(totalPages - 2)
for (page in startPage..endPage) {
	PaginationPageNumber(page, currentPage, onPageChange)
}

if (currentPage < totalPages - 4) {
	FutureTextMuted("...",
	modifier = Modifier.padding(horizontal = 4.dp)
)
		}

		if (totalPages > 1) {
			PaginationPageNumber(totalPages - 1, currentPage, onPageChange)
		}

		FutureButton("",
		onClick = { if (currentPage < totalPages - 1) onPageChange(currentPage + 1) },
		enabled = currentPage < totalPages - 1,
		type = ButtonType.Primary,
		icon = {
			FutureText(">",
			variant = TextVariant.Primary,
			color = if (currentPage < totalPages - 1) config.colors.textPrimary else config.colors.textMuted,
			textAlign = TextAlign.Center
		)
	}
)
	}
}

@Composable
private fun PaginationPageNumber(page: Int, currentPage: Int, onPageChange: (Int) -> Unit) {
	val config = FutureTheme.config
	val isSelected = page == currentPage
	val interactionSource = remember { MutableInteractionSource() }
	val isHovered by interactionSource.collectIsHoveredAsState()

	val bgAlpha by animateFloatAsState(
		targetValue = if (isSelected) 0.2f else if (isHovered) 0.1f else 0f,
		animationSpec = tween(config.animation.defaultMs)
	)
	val glowAlpha by animateFloatAsState(
		targetValue = if (isSelected) 0.6f else 0f,
		animationSpec = tween(config.animation.defaultMs)
	)

	Box(
		modifier = Modifier
		.size(36.dp)
		.clip(RoundedCornerShape(8.dp))
		.clickable(interactionSource = interactionSource, indication = null) { onPageChange(page) }
		.background(config.colors.primary.copy(alpha = bgAlpha))
		.futureGlow(
			color = config.colors.primary.copy(alpha = glowAlpha * 0.3f),
			blurRadius = 8f,
			shape = RoundedCornerShape(8.dp),
			intensity = glowAlpha
		)
		.border(
			if (isSelected) 1.5.dp else 1.dp,
			if (isSelected) config.colors.primary else config.colors.textMuted,
			RoundedCornerShape(8.dp)
		),
		contentAlignment = Alignment.Center
	) {
		FutureText((page + 1).toString(),
		variant = TextVariant.Primary,
		color = if (isSelected) config.colors.primary else config.colors.textPrimary,
		style = FutureTheme.typography.labelLarge.copy(
			fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
		)
	)
}
}

@Composable
fun FutureBreadcrumb(
	items: List<String>,
	modifier: Modifier = Modifier,
	onItemClick: ((Int) -> Unit)? = null
) {
	val config = FutureTheme.config
	Row(
		modifier = modifier,
		horizontalArrangement = Arrangement.spacedBy(8.dp),
		verticalAlignment = Alignment.CenterVertically
	) {
		items.forEachIndexed { index, item ->
			val isLast = index == items.size - 1
			val interactionSource = remember { MutableInteractionSource() }
			val isHovered by interactionSource.collectIsHoveredAsState()

			val textColor by animateColorAsState(
				targetValue = when {
					isLast -> config.colors.primary
					isHovered && onItemClick != null -> config.colors.textPrimary
					else -> config.colors.textMuted
				},
				animationSpec = tween(config.animation.defaultMs)
			)

			var textModifier: Modifier = Modifier
			if (!isLast && onItemClick != null) {
				textModifier = textModifier.clickable(
					interactionSource = interactionSource,
					indication = null,
					onClick = { onItemClick(index) }
				)
			}

			FutureText(item,
			variant = TextVariant.Primary,
			color = textColor,
			style = FutureTheme.typography.labelLarge.copy(
				fontWeight = if (isLast) FontWeight.Bold else FontWeight.Normal
			),
			modifier = textModifier
		)
		if (!isLast) {
			FutureTextMuted("/")
		}
	}
}
}
