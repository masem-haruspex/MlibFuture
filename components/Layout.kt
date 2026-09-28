@file:OptIn(androidx.compose.foundation.layout.ExperimentalLayoutApi::class)
package com.mlib.future.components

import android.os.Build
import android.view.WindowManager
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.window.DialogWindowProvider
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.heightIn
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.core.*
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.collectIsHoveredAsState
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.style.TextOverflow
import com.mlib.future.modifiers.*
import com.mlib.future.FutureTheme
import com.mlib.future.components.FutureText

@Composable
fun FutureButtonRow(
	modifier: Modifier = Modifier,
	content: @Composable () -> Unit
) {
	Row(
		modifier = modifier,
		horizontalArrangement = Arrangement.spacedBy(12.dp),
		verticalAlignment = Alignment.CenterVertically
	) {
		content()
	}
}

@Composable
fun FutureButtonColumn(
	modifier: Modifier = Modifier,
	content: @Composable () -> Unit
) {
	Column(
		modifier = modifier,
		verticalArrangement = Arrangement.spacedBy(12.dp),
		horizontalAlignment = Alignment.Start
	) {
		content()
	}
}

@Composable
fun FutureScrollArea(
	modifier: Modifier = Modifier,
	isBottomToTop: Boolean = false,
	scrollState: ScrollState = rememberScrollState(),
	contentPadding: PaddingValues = PaddingValues(16.dp),
	content: @Composable ColumnScope.() -> Unit,
) {
	var didAutoScroll by remember(scrollState) { mutableStateOf(false) }
	LaunchedEffect(isBottomToTop, scrollState.maxValue) {
		if (isBottomToTop && !didAutoScroll && scrollState.maxValue > 0) {
			if (scrollState.value == 0) {
				scrollState.scrollTo(scrollState.maxValue)
			}
			didAutoScroll = true
		}
	}

	BoxWithConstraints(modifier = modifier.fillMaxSize()) {
		val viewportHeight = maxHeight

		Column(
			modifier = Modifier
			.fillMaxWidth()
			.verticalScroll(scrollState)
			.heightIn(min = viewportHeight)
			.padding(contentPadding),
			verticalArrangement = if (isBottomToTop) {
				Arrangement.spacedBy(16.dp, Alignment.Bottom)
			} else {
				Arrangement.spacedBy(16.dp, Alignment.Top)
			},
			content = content,
		)
	}
}

@Composable
fun FutureDialog(
	title: String = "",
	onDismissRequest: () -> Unit,
	modifier: Modifier = Modifier,
	content: @Composable () -> Unit
) {
	val config = FutureTheme.config

	Dialog(
		onDismissRequest = onDismissRequest,
		properties = DialogProperties(
			usePlatformDefaultWidth = false,
			dismissOnBackPress = true,
			dismissOnClickOutside = true,
		),
	) {
		val view = LocalView.current
		val window = (view.parent as? DialogWindowProvider)?.window
		DisposableEffect(window) {
			if (window != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
				window.addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND)
				window.attributes = window.attributes.apply {
					blurBehindRadius = 40
				}
			}
			onDispose {
				if (window != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
					window.clearFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND)
				}
			}
		}

		val scrimInteraction = remember { MutableInteractionSource() }

		BoxWithConstraints(
			modifier = Modifier
			.fillMaxSize()
			.windowInsetsPadding(WindowInsets.safeDrawing)
			.clickable(
				interactionSource = scrimInteraction,
				indication = null,
				onClick = onDismissRequest,
			),
			contentAlignment = Alignment.Center,
		) {
			val maxDialogHeight = maxHeight

			Box(
				modifier = modifier
				.fillMaxWidth(0.9f)
				.widthIn(max = 480.dp)
				.heightIn(max = maxDialogHeight)
				.clip(config.shapes.dialog)
				.background(config.colors.componentBg)
				.border(1.dp, config.colors.primary.copy(alpha = 0.6f), config.shapes.dialog)
				.futureAnimatedBorder(color = config.colors.primary, shape = config.shapes.dialog, width = 1.5.dp)
				.futureCornerBrackets(shape = config.shapes.dialog, length = 18.dp, width = 2.5.dp)
				.pointerInput(Unit) { detectTapGestures { } }
			) {
				Column {
					if (title.isNotEmpty()) {
						FutureTextHeading(title, modifier = Modifier.padding(18.dp))
						FutureSeparator(modifier = Modifier.fillMaxWidth())
					}
					Column(modifier = Modifier.padding(18.dp)) {
						content()
					}
				}
			}
		}
	}
}

@Composable
fun FutureDrawer(
	modifier: Modifier = Modifier,
	content: @Composable () -> Unit
) {
	val config = FutureTheme.config
	Box(
		modifier = modifier
		.fillMaxHeight()
		.widthIn(min = 280.dp, max = 320.dp)
		.clip(config.shapes.drawer)
		.background(
			Brush.horizontalGradient(
				listOf(
					config.colors.componentBg,
					config.colors.componentBg,
					config.colors.primary.copy(alpha = 0.05f)
				)
			)
		)
		.border(1.dp, config.colors.primary, config.shapes.drawer)
		.futureGlow(
			color = config.colors.primary.copy(alpha = 0.1f),
			blurRadius = 24f,
			shape = config.shapes.drawer,
			intensity = 0.4f
		)
		.futureCornerBrackets(shape = config.shapes.drawer, length = 16.dp, width = 2.dp)
		.padding(20.dp)
	) {
		content()
	}
}

data class FutureNavItem(
	val label: String,
	val icon: @Composable () -> Unit
)

@Composable
fun FutureNavbar(
	modifier: Modifier = Modifier.fillMaxSize(),
	selectedIndex: Int = 0,
	onItemSelected: (Int) -> Unit = {},
	items: List<FutureNavItem> = listOf(),
	isBottom: Boolean = true
) {
	val config = FutureTheme.config
	val alignment = if (isBottom) Alignment.BottomCenter else Alignment.TopCenter

	Box(modifier = modifier) {
		Box(
			modifier = Modifier
			.align(alignment)
			.fillMaxWidth()
			.height(64.dp)
			.clip(config.shapes.nav)
			.background(config.colors.componentBg)
			.futureUnderline(
				color = config.colors.primary,
				height = 2.dp,
				top = isBottom
			)
			.futureGlowSubtle(
				color = config.colors.primary.copy(alpha = 0.1f),
				shape = config.shapes.nav
			)
		) {
			Row(
				modifier = Modifier.fillMaxWidth(),
				horizontalArrangement = Arrangement.SpaceEvenly,
				verticalAlignment = Alignment.CenterVertically
			) {
				items.forEachIndexed { index, item ->
					val isSelected = index == selectedIndex
					val interactionSource = remember { MutableInteractionSource() }
					val isHovered by interactionSource.collectIsHoveredAsState()

					val glowAlpha by animateFloatAsState(
						targetValue = if (isSelected) 0.8f else if (isHovered) 0.4f else 0f,
						animationSpec = tween(config.animation.defaultMs)
					)
					val iconColor by animateColorAsState(
						targetValue = if (isSelected) config.colors.primary else config.colors.textMuted,
						animationSpec = tween(config.animation.defaultMs)
					)

					Column(
						modifier = Modifier
						.clickable(
							interactionSource = interactionSource,
							indication = null,
							onClick = { onItemSelected(index) }
						)
						.padding(horizontal = 12.dp, vertical = 8.dp),
						horizontalAlignment = Alignment.CenterHorizontally,
						verticalArrangement = Arrangement.spacedBy(4.dp)
					) {
						Box(
							modifier = Modifier
							.size(32.dp)
							.clip(RoundedCornerShape(8.dp))
							.background(config.colors.primary.copy(alpha = glowAlpha * 0.2f))
							.futureGlow(
								color = config.colors.primary.copy(alpha = glowAlpha * 0.3f),
								blurRadius = 8f,
								shape = RoundedCornerShape(8.dp),
								intensity = glowAlpha
							),
							contentAlignment = Alignment.Center
						) {
							item.icon()
						}
						FutureText(
							item.label,
							variant = TextVariant.Primary,
							color = iconColor,
							style = FutureTheme.typography.labelSmall.copy(
								fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
								letterSpacing = 0.5.sp
							)
						)
					}
				}
			}
		}
	}
}

@Composable
fun FutureGrid(
	modifier: Modifier = Modifier,
	columns: Int = 2,
	items: List<@Composable () -> Unit>
) {
	Column(
		modifier = modifier,
		verticalArrangement = Arrangement.spacedBy(12.dp)
	) {
		items.chunked(columns).forEach { row ->
			Row(
				modifier = Modifier.fillMaxWidth(),
				horizontalArrangement = Arrangement.spacedBy(12.dp)
			) {
				row.forEach { item ->
					Box(modifier = Modifier.weight(1f)) { item() }
				}
				if (row.size < columns) {
					repeat(columns - row.size) {
						Spacer(modifier = Modifier.weight(1f))
					}
				}
			}
		}
	}
}

@Composable
fun FutureHeader(
	title: String,
	modifier: Modifier = Modifier,
	subtitle: String = "",
	isBottom: Boolean = false,
	isTwoRows: Boolean = false,
	menuItems: List<String> = emptyList(),
	onMenuItemClick: ((String) -> Unit)? = null,
	onTitleClick: (() -> Unit)? = null,
	actions: @Composable RowScope.() -> Unit = {}
) {
	val config = FutureTheme.config
	var menuExpanded by remember { mutableStateOf(false) }

	Column(modifier = modifier) {
		if (isBottom && menuItems.isNotEmpty()) {
			AnimatedVisibility(
				visible = menuExpanded,
				enter = expandVertically(tween(config.animation.defaultMs, easing = EaseOutQuart)) + fadeIn(tween(config.animation.defaultMs)),
				exit = shrinkVertically(tween(config.animation.defaultMs, easing = EaseInQuart)) + fadeOut(tween(config.animation.defaultMs))
			) {
				FutureMenu(
					items = menuItems,
					selectedItem = null,
					onItemClick = {
						onMenuItemClick?.invoke(it)
						menuExpanded = false
					},
					modifier = Modifier.padding(bottom = 4.dp)
				)
			}
		}

		if (isBottom) {
			FutureSeparator(modifier = Modifier.fillMaxWidth())
		}

		if (isTwoRows) {
			Column(
				modifier = Modifier
				.fillMaxWidth()
				.background(
					Brush.horizontalGradient(
						listOf(
							config.colors.componentBg,
							config.colors.primary.copy(alpha = 0.04f)
						)
					)
				)
				.padding(horizontal = 16.dp, vertical = 12.dp)
			) {
				Column(
					modifier = Modifier
					.fillMaxWidth()
					.then(
						if (onTitleClick != null) Modifier.clickable(onClick = onTitleClick) else Modifier
					)
				) {
					FutureText(
						text = title,
						variant = TextVariant.Title,
						maxLines = 1,
						overflow = TextOverflow.Ellipsis
					)
					if (subtitle.isNotEmpty()) {
						FutureText(
							text = subtitle,
							variant = TextVariant.Muted,
							style = FutureTheme.typography.labelSmall
						)
					}
				}

				Spacer(Modifier.height(12.dp))

				Row(
					modifier = Modifier.fillMaxWidth(),
					verticalAlignment = Alignment.CenterVertically,
					horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End)
				) {
					if (menuItems.isNotEmpty()) {
						Box(
							modifier = Modifier
							.size(36.dp)
							.clip(RoundedCornerShape(8.dp))
							.clickable { menuExpanded = !menuExpanded }
							.background(config.colors.primary.copy(alpha = if (menuExpanded) 0.15f else 0.05f)),
							contentAlignment = Alignment.Center
						) {
							FutureText("☰", variant = TextVariant.Primary)
						}
					}
					Row(
						verticalAlignment = Alignment.CenterVertically,
						horizontalArrangement = Arrangement.spacedBy(8.dp),
						content = actions
					)
				}
			}
		} else {
			Box(
				modifier = Modifier
				.fillMaxWidth()
				.height(56.dp)
				.background(
					Brush.horizontalGradient(
						listOf(
							config.colors.componentBg,
							config.colors.primary.copy(alpha = 0.04f)
						)
					)
				)
				.padding(horizontal = 16.dp),
				contentAlignment = Alignment.CenterStart
			) {
				Row(
					modifier = Modifier.fillMaxWidth(),
					verticalAlignment = Alignment.CenterVertically,
					horizontalArrangement = Arrangement.SpaceBetween
				) {
					Column(
						modifier = Modifier.weight(1f).then(
							if (onTitleClick != null) Modifier.clickable(onClick = onTitleClick) else Modifier
						)
					) {
						FutureText(
							text = title,
							variant = TextVariant.Title,
							maxLines = 1,
							overflow = TextOverflow.Ellipsis
						)
						if (subtitle.isNotEmpty()) {
							FutureText(
								text = subtitle,
								variant = TextVariant.Muted,
								style = FutureTheme.typography.labelSmall
							)
						}
					}
					Row(
						verticalAlignment = Alignment.CenterVertically,
						horizontalArrangement = Arrangement.spacedBy(8.dp)
					) {
						if (menuItems.isNotEmpty()) {
							Box(
								modifier = Modifier
								.size(36.dp)
								.clip(RoundedCornerShape(8.dp))
								.clickable { menuExpanded = !menuExpanded }
								.background(config.colors.primary.copy(alpha = if (menuExpanded) 0.15f else 0.05f)),
								contentAlignment = Alignment.Center
							) {
								FutureText("☰", variant = TextVariant.Primary)
							}
						}
						Row(
							verticalAlignment = Alignment.CenterVertically,
							horizontalArrangement = Arrangement.spacedBy(8.dp),
							content = actions
						)
					}
				}
			}
		}

		if (!isBottom) {
			Box(modifier = Modifier.fillMaxWidth().futureSeparator(thickness = 0.5.dp))
		}

		if (!isBottom && menuItems.isNotEmpty()) {
			AnimatedVisibility(
				visible = menuExpanded,
				enter = expandVertically(tween(config.animation.defaultMs, easing = EaseOutQuart)) + fadeIn(tween(config.animation.defaultMs)),
				exit = shrinkVertically(tween(config.animation.defaultMs, easing = EaseInQuart)) + fadeOut(tween(config.animation.defaultMs))
			) {
				FutureMenu(
					items = menuItems,
					selectedItem = null,
					onItemClick = {
						onMenuItemClick?.invoke(it)
						menuExpanded = false
					},
					modifier = Modifier.padding(top = 4.dp)
				)
			}
		}
	}
}
