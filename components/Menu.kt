package com.mlib.future.components

import androidx.compose.foundation.clickable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.EaseOutQuart
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.mlib.future.FutureTheme
import com.mlib.future.modifiers.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.graphics.Color

@Composable
fun FutureMenu(
	items: List<String>,
	onItemClick: (String) -> Unit,
	modifier: Modifier = Modifier,
	selectedItem: String? = null
) {
	val config = FutureTheme.config
	val accent = config.colors.accent ?: config.colors.primary

	Column(
		modifier = modifier
		.fillMaxWidth()
		.clip(config.shapes.input)
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
			config.shapes.input
		)
		.futureCornerBrackets(shape = config.shapes.input, length = 12.dp, width = 2.dp)
	) {
		items.forEachIndexed { index, item ->
			val interactionSource = remember { MutableInteractionSource() }
			val isHovered by interactionSource.collectIsHoveredAsState()
			val bgAlpha by animateFloatAsState(
				targetValue = if (isHovered) 0.12f else 0f,
				animationSpec = tween(config.animation.defaultMs)
			)
			val isSelected = item == selectedItem

			Box(
				modifier = Modifier
				.fillMaxWidth()
				.clickable(
					interactionSource = interactionSource,
					indication = null,
					onClick = { onItemClick(item) }
				)
				.background(config.colors.primary.copy(alpha = bgAlpha))
				.padding(horizontal = 16.dp, vertical = 12.dp)
			) {
				FutureText(
					text = item,
					variant = TextVariant.Primary,
					color = if (isSelected) config.colors.primary else config.colors.textMuted,
					style = FutureTheme.typography.bodyMedium.copy(
						fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
					)
				)
			}
			if (index < items.size - 1) {
				Spacer(
					modifier = Modifier
					.padding(horizontal = 16.dp)
					.fillMaxWidth()
					.futureSeparator(thickness = 0.5.dp)
				)
			}
		}
	}
}

@Composable
fun FutureDropdownMenu(
	expanded: Boolean,
	onDismissRequest: () -> Unit,
	modifier: Modifier = Modifier,
	menuModifier: Modifier = Modifier.fillMaxWidth(),
	anchor: @Composable () -> Unit,
	content: @Composable androidx.compose.foundation.layout.ColumnScope.() -> Unit
) {
	val config = FutureTheme.config
	Box(modifier = modifier) {
		anchor()
		androidx.compose.material3.DropdownMenu(
			expanded = expanded,
			onDismissRequest = onDismissRequest,
			modifier = menuModifier
			.background(config.colors.componentBg)
			.border(
				1.dp,
				Brush.sweepGradient(
					listOf(
						config.colors.primary.copy(alpha = 0.3f),
						config.colors.primary,
						config.colors.primary.copy(alpha = 0.3f)
					)
				),
				RoundedCornerShape(8.dp)
			)
			.futureCornerBrackets(shape = RoundedCornerShape(8.dp), length = 12.dp, width = 2.dp),
			content = content
		)
	}
}

data class RadialMenuItem(
	val id: String,
	val label: String,
	val icon: @Composable (Modifier) -> Unit
)

@Composable
fun RadialDots(color: Color = FutureTheme.config.colors.primary) {
	Canvas(modifier = Modifier.size(22.dp)) {
		val r = 2.2.dp.toPx()
		val s = size.width / 3f
		for (row in 0..2) for (col in 0..2) {
			drawCircle(
				color = color,
				radius = r,
				center = Offset(s * (col + 0.5f), s * (row + 0.5f))
			)
		}
	}
}

@Composable
fun RadialMenuOverlay(
	items: List<RadialMenuItem>,
	selectedIndex: Int?,
	fingerPos: Offset,
	buttonProgress: Float,
	iconBounds: MutableMap<Int, Rect>,
	editing: Boolean,
	onRemove: (Int) -> Unit,
	onSwap: (from: Int, to: Int) -> Unit,
	onAddClick: () -> Unit,
	onExitEdit: () -> Unit,
	cornerPadding: Dp = 24.dp,
	buttonSize: Dp = 56.dp,
	iconSize: Dp = 64.dp,
	radius: Dp = 120.dp,
	verticalBias: Float = 0.80f,
	modifier: Modifier = Modifier
) {
	val config = FutureTheme.config
	val density = LocalDensity.current

	var draggingIndex by remember { mutableStateOf<Int?>(null) }
	var dragOffset by remember { mutableStateOf(Offset.Zero) }
	var dragRootPos by remember { mutableStateOf(Offset.Zero) }

	BoxWithConstraints(modifier = modifier.fillMaxSize()) {
		val wPx = with(density) { maxWidth.toPx() }
		val hPx = with(density) { maxHeight.toPx() }
		val btnPx = with(density) { buttonSize.toPx() }
		val iconPx = with(density) { iconSize.toPx() }
		val radiusPx = with(density) { radius.toPx() }
		val padPx = with(density) { cornerPadding.toPx() }

		val corner = Offset(wPx - padPx - btnPx / 2f, hPx - padPx - btnPx / 2f)
		val center = Offset(wPx / 2f, hPx * verticalBias)

		val buttonCenter = Offset(
			x = corner.x + (center.x - corner.x) * buttonProgress,
			y = corner.y + (center.y - corner.y) * buttonProgress
		)

		val displaySlots = when {
			editing && items.size < 8 -> items.size + 1
			items.isEmpty() -> 1
			else -> items.size
		}
		val angleStep = if (displaySlots > 0) 360f / displaySlots else 360f

		fun slotCenter(index: Int): Offset {
			val angleRad = (-90f + index * angleStep) * PI.toFloat() / 180f
			return Offset(
				center.x + radiusPx * cos(angleRad),
				center.y + radiusPx * sin(angleRad)
			)
		}

		if (buttonProgress > 0.01f) {
			Box(
				modifier = Modifier
				.matchParentSize()
				.background(config.colors.globalBg.copy(alpha = 0.55f * buttonProgress))
				.then(
					if (editing) Modifier
					.pointerInput(Unit) {
						detectTapGestures {
							onExitEdit()
						}
					}
					else Modifier
				)
			)
		}

		if (!editing) {
			Canvas(modifier = Modifier.matchParentSize()) {
				val progress = ((buttonProgress - 0.5f) * 2f).coerceIn(0f, 1f)
				if (progress <= 0f) return@Canvas
				val end = if (selectedIndex != null && selectedIndex in items.indices) {
					slotCenter(selectedIndex)
				} else {
					Offset(
						center.x + (fingerPos.x - center.x) * progress,
						center.y + (fingerPos.y - center.y) * progress
					)
				}
				drawLine(
					color = config.colors.primary.copy(alpha = progress * 0.25f),
					start = center,
					end = end,
					strokeWidth = 8.dp.toPx(),
					cap = StrokeCap.Round
				)
				drawLine(
					color = config.colors.primary.copy(alpha = progress),
					start = center,
					end = end,
					strokeWidth = 2.5.dp.toPx(),
					cap = StrokeCap.Round
				)
			}
		}

		if (buttonProgress > 0.3f) {
			val iconAlpha = ((buttonProgress - 0.3f) / 0.7f).coerceIn(0f, 1f)
			items.forEachIndexed { i, item ->
				val c = slotCenter(i)
				val isHovered = !editing && selectedIndex == i
				val isDragging = draggingIndex == i

				val visualOffset = if (isDragging) dragOffset else Offset.Zero

				Box(
					modifier = Modifier
					.offset {
						IntOffset(
							(c.x - iconPx / 2f).roundToInt(),
							(c.y - iconPx / 2f).roundToInt()
						)
					}
					.size(iconSize)
					.graphicsLayer {
						alpha = iconAlpha
						scaleX = 0.6f + 0.4f * iconAlpha
						scaleY = 0.6f + 0.4f * iconAlpha
						translationX = visualOffset.x
						translationY = visualOffset.y
					}
					.onGloballyPositioned {
						iconBounds[i] = it.boundsInRoot()
					}
					.then(
						if (editing) Modifier.pointerInput(i, items.size) {
							detectTapGestures(onTap = { })
						} else Modifier
					)
					.then(
						if (editing) Modifier.pointerInput(i, items.size) {
							detectDragGesturesAfterLongPress(
								onDragStart = { local ->
									draggingIndex = i
									dragOffset = Offset.Zero
									val r = iconBounds[i]
									if (r != null) dragRootPos = r.topLeft + local
								},
								onDrag = { change, amount ->
									change.consume()
									dragOffset += amount
									val r = iconBounds[i]
									if (r != null) {
										dragRootPos = r.topLeft + change.position
									}
								},
								onDragEnd = {
									val from = draggingIndex
									draggingIndex = null
									dragOffset = Offset.Zero
									if (from != null) {
										val target = iconBounds.entries
										.firstOrNull { (k, r) ->
											k != from && r.contains(dragRootPos)
										}
										?.key
										if (target != null) onSwap(from, target)
									}
								},
								onDragCancel = {
									draggingIndex = null
									dragOffset = Offset.Zero
								}
							)
						} else Modifier
					)
				) {
					Box(
						modifier = Modifier
						.fillMaxSize()
						.clip(CircleShape)
						.background(config.colors.componentBg)
						.border(
							width = if (isHovered || isDragging) 3.dp else 1.5.dp,
							color = if (isHovered || isDragging) config.colors.primary
							else config.colors.textMuted,
							shape = CircleShape
						),
						contentAlignment = Alignment.Center
					) {
						item.icon(Modifier.size(iconSize * 0.6f))
					}

					if (editing) {
						Box(
							modifier = Modifier
							.align(Alignment.TopEnd)
							.offset(x = 8.dp, y = (-8).dp)
							.size(24.dp)
							.clip(CircleShape)
							.background(config.colors.componentBg)
							.border(1.5.dp, config.colors.error, CircleShape)
							.clickable { onRemove(i) },
							contentAlignment = Alignment.Center
						) {
							FutureIconRemove(size = 14, color = config.colors.error)
						}
					}
				}
			}

			if (editing && items.size < 8) {
				val c = slotCenter(items.size)
				Box(
					modifier = Modifier
					.offset {
						IntOffset(
							(c.x - iconPx / 2f).roundToInt(),
							(c.y - iconPx / 2f).roundToInt()
						)
					}
					.size(iconSize)
					.graphicsLayer {
						alpha = iconAlpha
						scaleX = 0.6f + 0.4f * iconAlpha
						scaleY = 0.6f + 0.4f * iconAlpha
					}
					.clip(CircleShape)
					.background(config.colors.componentBg)
					.border(1.5.dp, config.colors.textMuted, CircleShape)
					.clickable { onAddClick() },
					contentAlignment = Alignment.Center
				) {
					FutureIconAdd(size = 28, color = config.colors.primary)
				}
			}
		}

		Box(
			modifier = Modifier
			.offset {
				IntOffset(
					(buttonCenter.x - btnPx / 2f).roundToInt(),
					(buttonCenter.y - btnPx / 2f).roundToInt()
				)
			}
			.size(buttonSize)
			.clip(CircleShape)
			.background(config.colors.componentBg)
			.border(
				width = if (buttonProgress > 0.5f) 2.5.dp else 2.dp,
				color = config.colors.primary,
				shape = CircleShape
			),
			contentAlignment = Alignment.Center
		) {
			if (buttonProgress > 0.5f) {
				if (editing) {
					FutureIconCheckmark(size = 24)
				} else {
					FutureIconAdd(size = 24)
				}
			} else {
				RadialDots()
			}
		}
	}
}
