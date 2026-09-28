package com.mlib.future.components

import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.ui.draw.scale
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mlib.future.modifiers.*
import com.mlib.future.FutureTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.*

enum class ButtonType { Primary, Highlighted, Disabled, Destructive }

@Composable
fun FutureButton(
	text: String = "",
	onClick: () -> Unit,
	modifier: Modifier = Modifier,
	enabled: Boolean = true,
	type: ButtonType = ButtonType.Primary,
	loading: Boolean = false,
	color: Color? = null,
	icon: @Composable (() -> Unit)? = null
) {
	FutureButtonImpl(
		text = text,
		onClick = onClick,
		modifier = modifier,
		enabled = enabled,
		type = if (!enabled) ButtonType.Disabled else type,
		loading = loading,
		color = color,
		icon = icon
	)
}

@Composable
fun FutureButton(
	icon: @Composable () -> Unit,
	onClick: () -> Unit,
	modifier: Modifier = Modifier,
	enabled: Boolean = true,
	type: ButtonType = ButtonType.Primary,
	loading: Boolean = false,
	color: Color? = null
) {
	FutureButtonImpl(
		text = "",
		onClick = onClick,
		modifier = modifier,
		enabled = enabled,
		type = if (!enabled) ButtonType.Disabled else type,
		loading = loading,
		color = color,
		icon = icon
	)
}

@Composable
private fun FutureButtonImpl(
	text: String = "",
	onClick: () -> Unit,
	modifier: Modifier = Modifier,
	enabled: Boolean = true,
	type: ButtonType = ButtonType.Primary,
	loading: Boolean = false,
	color: Color? = null,
	icon: @Composable (() -> Unit)? = null
) {
	val config = FutureTheme.config
	val interactionSource = remember { MutableInteractionSource() }
	val isHovered by interactionSource.collectIsHoveredAsState()
	val isPressed by interactionSource.collectIsPressedAsState()

	val isIconOnly = icon != null && text.isEmpty()
	val actualEnabled = enabled && !loading

	val typeColor = when (type) {
		ButtonType.Primary -> color ?: config.colors.primary
		ButtonType.Highlighted -> color ?: config.colors.primary
		ButtonType.Disabled -> color ?: config.colors.textMuted
		ButtonType.Destructive -> color ?: config.colors.error
	}

	val animSpec = tween<Float>(durationMillis = config.animation.defaultMs, easing = EaseOutQuart)
	val scale by animateFloatAsState(
		targetValue = if (isPressed) 0.97f else if (isHovered) 1.02f else 1f,
		animationSpec = animSpec
	)
	val glowAlpha by animateFloatAsState(
		targetValue = when {
			loading -> 1f
			isHovered -> 0.8f
			actualEnabled -> 0.4f
			else -> 0.1f
		},
		animationSpec = tween(config.animation.defaultMs)
	)

	val pulse by if (type == ButtonType.Highlighted) {
		val infinite = rememberInfiniteTransition(label = "btn_pulse")
		infinite.animateFloat(
			initialValue = 0.7f, targetValue = 1f,
			animationSpec = infiniteRepeatable(
				tween(config.animation.slowMs, easing = EaseInOutSine),
				RepeatMode.Reverse
			),
			label = "pulse"
		)
	} else remember { mutableFloatStateOf(1f) }

	val displayText = if (loading && text.isNotEmpty()) {
		val infinite = rememberInfiniteTransition(label = "btn_dots")
		val dotOffset by infinite.animateFloat(
			initialValue = 0f, targetValue = 3f,
			animationSpec = infiniteRepeatable(
				tween(config.animation.flowMs * 2, easing = LinearEasing),
				RepeatMode.Restart
			),
			label = "dots"
		)
		text + ".".repeat(((dotOffset % 3) + 1).toInt())
	} else text

	val shape = config.shapes.button
	val accent = config.colors.accent ?: typeColor

	var btnModifier = modifier
	.graphicsLayer { scaleX = scale; scaleY = scale }
	.clip(shape)


	btnModifier = btnModifier.clickable(
		interactionSource = interactionSource,
		indication = null,
		enabled = actualEnabled,
		onClick = onClick
	)

	btnModifier = when (type) {
		ButtonType.Highlighted -> btnModifier
		.futureAnimatedBorder(color = typeColor, shape = shape, width = 1.5.dp)
		.futureGlowStrong(
			color = typeColor.copy(alpha = glowAlpha * 0.6f), shape = shape
		)
		ButtonType.Destructive -> btnModifier
		.futureAnimatedBorder(color = typeColor, shape = shape, width = 1.5.dp)
		.futureGlow(
			color = typeColor.copy(alpha = glowAlpha * 0.5f),
			blurRadius = 14f,
			shape = shape,
			intensity = glowAlpha
		)
		ButtonType.Primary -> btnModifier
		.futureAnimatedBorder(
			color = if (actualEnabled) typeColor else config.colors.textMuted,
			shape = shape,
			width = 1.dp,
			halo = actualEnabled
		)
		else -> btnModifier
	}

	btnModifier = when (type) {
		ButtonType.Disabled -> btnModifier.futureUnderlineAnimated(
			color = typeColor,
			height = 1.dp,
			progress = if (isHovered) 1f else 0.3f
		)

		ButtonType.Highlighted -> btnModifier.futureUnderline(
			color = typeColor,
			height = if (isHovered) 3.dp else 2.dp,
			glowIntensity = glowAlpha,
			showSpark = true 
		)

		else -> btnModifier.futureUnderline(
			color = if (actualEnabled) typeColor else config.colors.textMuted,
			height = if (isHovered) 3.dp else 2.dp,
			glowIntensity = glowAlpha,
			showSpark = false
		)
	}

	Box(
		modifier = if (isIconOnly) btnModifier.size(48.dp) else btnModifier.padding(horizontal = 24.dp, vertical = 14.dp),
		contentAlignment = Alignment.Center
	) {
		if (isIconOnly && icon != null) {
			Box(
				modifier = Modifier.scale(1.4f),
				contentAlignment = Alignment.Center
			) { icon() }
		} else {
			Row(
				horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
				verticalAlignment = Alignment.CenterVertically
			) {
				if (icon != null) icon()
				if (text.isNotEmpty()) {
					FutureText(displayText,
					color = if (actualEnabled) typeColor else config.colors.textMuted,
					textAlign = TextAlign.Start,
					style = FutureTheme.typography.labelLarge.copy(
						fontWeight = if (type == ButtonType.Highlighted) FontWeight.Bold else FontWeight.SemiBold,
						letterSpacing = if (type == ButtonType.Highlighted) 3.sp else 2.sp
					)
				)
			}
		}
	}
}
			}

			@Composable
			fun FutureTextField(
				label: String = "",
				value: String,
				onValueChange: (String) -> Unit,
				modifier: Modifier = Modifier,
				placeholder: String = "",
				enabled: Boolean = true,
				singleLine: Boolean = true,
				keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
				keyboardActions: KeyboardActions = KeyboardActions.Default,
				visualTransformation: VisualTransformation = VisualTransformation.None
			) {
				val config = FutureTheme.config
				val interactionSource = remember { MutableInteractionSource() }
				val isFocused by interactionSource.collectIsFocusedAsState()
				val focusRequester = remember { FocusRequester() }

				Column(modifier = modifier) {
					if (label.isNotEmpty()) {
						val labelStyle = FutureTheme.typography.labelSmall.copy(
							letterSpacing = 1.5.sp,
							fontWeight = FontWeight.Medium
						)
						if (isFocused) FutureText(label, style = labelStyle)
						else FutureTextMuted(label, style = labelStyle)
					}
					BasicTextField(
						value = value,
						onValueChange = onValueChange,
						modifier = Modifier.fillMaxWidth().focusRequester(focusRequester),
						enabled = enabled,
						singleLine = singleLine,
						textStyle = FutureTheme.typography.bodyLarge.copy(
							color = if (enabled) config.colors.textPrimary else config.colors.textMuted,
							fontWeight = FontWeight.Normal,
							fontFamily = config.fonts.regular
						),
						keyboardOptions = keyboardOptions,
						keyboardActions = keyboardActions,
						visualTransformation = visualTransformation,
						interactionSource = interactionSource,
						cursorBrush = SolidColor(config.colors.primary),
						decorationBox = { innerTextField ->
							Column {
								Box(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp)) {
									if (value.isEmpty() && placeholder.isNotEmpty()) {
										FutureTextMuted(placeholder, style = FutureTheme.typography.bodyLarge)
									}
									innerTextField()
								}
								Box(
									modifier = Modifier
									.fillMaxWidth()
									.height(2.dp)
									.futureInputUnderline(focused = isFocused)
								)
							}
						}
					)
				}
			}

			@Composable
			fun FutureTextArea(
				label: String = "",
				value: String,
				onValueChange: (String) -> Unit,
				modifier: Modifier = Modifier,
				placeholder: String = "",
				enabled: Boolean = true,
				minLines: Int = 3,
				maxLines: Int = 6,
				showUnderline: Boolean = true,
				fillHeight: Boolean = false
			) {
				val config = FutureTheme.config
				val interactionSource = remember { MutableInteractionSource() }
				val isFocused by interactionSource.collectIsFocusedAsState()

				Column(modifier = modifier) {
					if (label.isNotEmpty()) {
						val labelStyle = FutureTheme.typography.labelSmall.copy(
							letterSpacing = 1.5.sp,
							fontWeight = FontWeight.Medium
						)
						if (isFocused)
						FutureText(label, style = labelStyle)
						else
						FutureTextMuted(label, style = labelStyle)
					}

					BasicTextField(
						value = value,
						onValueChange = onValueChange,
						modifier = Modifier
						.fillMaxWidth()
						.then(if (fillHeight) Modifier.weight(1f) else Modifier)
						.defaultMinSize(minHeight = (minLines * 24).dp),
						enabled = enabled,
						textStyle = FutureTheme.typography.bodyLarge.copy(
							color = if (enabled) config.colors.textPrimary else config.colors.textMuted,
							fontFamily = config.fonts.regular
						),
						interactionSource = interactionSource,
						cursorBrush = SolidColor(config.colors.primary),
						maxLines = if (fillHeight) Int.MAX_VALUE else maxLines,
						decorationBox = { innerTextField ->
							Column(modifier = if (fillHeight) Modifier.fillMaxSize() else Modifier) {
								Box(
									modifier = Modifier
									.fillMaxWidth()
									.then(if (fillHeight) Modifier.weight(1f) else Modifier)
									.padding(horizontal = 16.dp, vertical = 14.dp)
								) {
									if (value.isEmpty() && placeholder.isNotEmpty()) {
										FutureTextMuted(placeholder, style = FutureTheme.typography.bodyLarge)
									}
									innerTextField()
								}

								if (showUnderline) {
									Box(
										modifier = Modifier
										.fillMaxWidth()
										.height(2.dp)
										.futureInputUnderline(focused = isFocused)
									)
								}
							}
						}
					)
				}
			}

			@Composable
			fun FutureSearch(
				placeholder: String = "Search...",
				value: String,
				onValueChange: (String) -> Unit,
				modifier: Modifier = Modifier,
				searchResults: List<String> = emptyList(),
				onResultClick: ((String) -> Unit)? = null,
				onSearch: () -> Unit = {},
				onFocusChanged: ((Boolean) -> Unit)? = null,
				bordered: Boolean = false,
				componentBg: Boolean? = null,
				border: Boolean? = null
			) {
				val config = FutureTheme.config
				val interactionSource = remember { MutableInteractionSource() }
				val isFocused by interactionSource.collectIsFocusedAsState()
				val accent = config.colors.accent ?: config.colors.primary

				val hasBg = componentBg ?: bordered
				val hasBorder = border ?: bordered
				val surfaced = hasBg || hasBorder

				LaunchedEffect(isFocused) {
					onFocusChanged?.invoke(isFocused)
				}

				val shape = RoundedCornerShape(8.dp)

				var surfaceModifier = modifier
				if (surfaced) {
					surfaceModifier = surfaceModifier.clip(shape)
				}
				if (hasBg) {
					surfaceModifier = surfaceModifier.background(config.colors.componentBg)
				}
				if (hasBorder) {
					surfaceModifier = surfaceModifier.border(
						width = if (isFocused) 1.5.dp else 1.dp,
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
				if (isFocused && surfaced) {
					surfaceModifier = surfaceModifier.futureGlow(
						color = config.colors.primary.copy(alpha = 0.25f),
						blurRadius = 10f,
						shape = shape,
						intensity = 0.4f
					)
				}

				Column(modifier = surfaceModifier) {
					Box(
						modifier = Modifier
						.fillMaxWidth()
						.padding(
							horizontal = if (surfaced) 12.dp else 20.dp,
							vertical = 12.dp
						),
						contentAlignment = Alignment.CenterStart
					) {
						Row(
							verticalAlignment = Alignment.CenterVertically,
							horizontalArrangement = Arrangement.spacedBy(8.dp)
						) {
							FutureIconSearch(color = if (isFocused) config.colors.primary else config.colors.textMuted)

							BasicTextField(
								value = value,
								onValueChange = onValueChange,
								modifier = Modifier.weight(1f),
								textStyle = FutureTheme.typography.bodyLarge.copy(
									color = config.colors.textPrimary,
									fontFamily = config.fonts.regular
								),
								singleLine = true,
								keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
								keyboardActions = KeyboardActions(onSearch = { onSearch() }),
								interactionSource = interactionSource,
								cursorBrush = SolidColor(config.colors.primary),
								decorationBox = { innerTextField ->
									if (value.isEmpty()) {
										FutureTextMuted(
											placeholder,
											style = FutureTheme.typography.bodyLarge,
											maxLines = 1
										)
									}
									innerTextField()
								}
							)

							if (value.isNotEmpty()) {
								Box(
									modifier = Modifier.size(20.dp).clickable { onValueChange("") }.drawBehind {
										val c = config.colors.textMuted
										val sw = 1.5.dp.toPx()
										val o = 5.dp.toPx()
										drawLine(c, Offset(o, o), Offset(size.width - o, size.height - o), sw)
										drawLine(c, Offset(size.width - o, o), Offset(o, size.height - o), sw)
									}
								)
							}
						}
					}

					if (!surfaced) {
						FutureSeparator(modifier = Modifier.fillMaxWidth())
					}

					if (isFocused && searchResults.isNotEmpty()) {
						FutureMenu(
							items = searchResults,
							onItemClick = { onResultClick?.invoke(it) },
							modifier = Modifier.padding(top = 4.dp)
						)
					}
				}
			}

			@Composable
			fun FutureNumberInput(
				label: String = "",
				value: String,
				onValueChange: (String) -> Unit,
				modifier: Modifier = Modifier,
				placeholder: String = "",
				enabled: Boolean = true
			) {
				FutureTextField(
					value = value,
					onValueChange = { input -> onValueChange(input.filter { it.isDigit() || it == '.' || it == '-' }) },
					modifier = modifier,
					placeholder = placeholder,
					label = label,
					enabled = enabled,
					keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
				)
			}

			@Composable
			fun FutureDateInput(
				label: String = "",
				value: String,
				onClick: () -> Unit,
				modifier: Modifier = Modifier,
				enabled: Boolean = true
			) {
				val config = FutureTheme.config
				val interactionSource = remember { MutableInteractionSource() }
				val isHovered by interactionSource.collectIsHoveredAsState()
				val accent = config.colors.accent ?: config.colors.primary

				val glowAlpha by animateFloatAsState(
					targetValue = if (isHovered) 0.4f else 0.2f,
					animationSpec = tween(config.animation.defaultMs)
				)

				Column(modifier = modifier.clickable(interactionSource = interactionSource, indication = null, enabled = enabled, onClick = onClick)) {
					if (label.isNotEmpty()) {
						FutureTextMuted(label,
						style = FutureTheme.typography.labelSmall.copy(letterSpacing = 1.5.sp),
						modifier = Modifier.padding(bottom = 6.dp)
					)
				}
				Box(
					modifier = Modifier
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
					.futureGlow(
						color = config.colors.primary.copy(alpha = glowAlpha * 0.3f),
						blurRadius = 12f,
						shape = config.shapes.input,
						intensity = glowAlpha
					)
					.padding(horizontal = 16.dp, vertical = 14.dp)
				) {
					Row(
						modifier = Modifier.fillMaxWidth(),
						horizontalArrangement = Arrangement.SpaceBetween,
						verticalAlignment = Alignment.CenterVertically
					) {
						val displayText = value.ifEmpty { "SELECT DATE..." }
						if (value.isNotEmpty()) {
							FutureText(displayText)
						} else {
							FutureTextMuted(displayText, style = FutureTheme.typography.bodyLarge)
						}
						Box(modifier = Modifier.size(20.dp).drawBehind {
							val c = config.colors.primary
							val sw = 1.5.dp.toPx()
							drawLine(c, Offset(4.dp.toPx(), 8.dp.toPx()), Offset(10.dp.toPx(), 14.dp.toPx()), sw)
							drawLine(c, Offset(16.dp.toPx(), 8.dp.toPx()), Offset(10.dp.toPx(), 14.dp.toPx()), sw)
						})
					}
				}
			}
		}

		@Composable
		fun FutureFileInput(
			fileName: String,
			onBrowse: () -> Unit,
			modifier: Modifier = Modifier,
			label: String = "",
			enabled: Boolean = true
		) {
			val config = FutureTheme.config
			val interactionSource = remember { MutableInteractionSource() }
			val isHovered by interactionSource.collectIsHoveredAsState()
			val accent = config.colors.accent ?: config.colors.primary

			val glowAlpha by animateFloatAsState(
				targetValue = if (isHovered) 0.4f else 0.2f,
				animationSpec = tween(config.animation.defaultMs)
			)

			Column(modifier = modifier) {
				if (label.isNotEmpty()) {
					FutureTextMuted(label,
					style = FutureTheme.typography.labelSmall.copy(letterSpacing = 1.5.sp),
					modifier = Modifier.padding(bottom = 6.dp)
				)
			}
			Row(
				modifier = Modifier.fillMaxWidth(),
				horizontalArrangement = Arrangement.spacedBy(8.dp),
				verticalAlignment = Alignment.CenterVertically
			) {
				Box(
					modifier = Modifier
					.weight(1f)
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
					.padding(horizontal = 16.dp, vertical = 14.dp)
				) {
					val displayText = fileName.ifEmpty { "NO FILE SELECTED" }
					if (fileName.isNotEmpty()) {
						FutureText(displayText)
					} else {
						FutureTextMuted(displayText, style = FutureTheme.typography.bodyLarge)
					}
				}
				FutureButton(
					onClick = onBrowse,
					text = "BROWSE",
					enabled = enabled,
				)
			}
		}
	}

	@Composable
	fun FutureCameraInput(
		capturedLabel: String = "",
		onCapture: () -> Unit,
		modifier: Modifier = Modifier,
		enabled: Boolean = true
	) {
		val config = FutureTheme.config
		val hasImage = capturedLabel.isNotEmpty()
		val accent = config.colors.accent ?: config.colors.primary

		Box(
			modifier = modifier
			.fillMaxWidth()
			.height(180.dp)
			.clip(config.shapes.card)
			.background(config.colors.componentBg)
			.border(
				1.dp,
				Brush.sweepGradient(
					listOf(
						config.colors.primary.copy(alpha = 0.35f),
						if (hasImage) config.colors.primary else config.colors.textMuted,
						accent,
						config.colors.primary,
						config.colors.primary.copy(alpha = 0.35f)
					)
				),
				config.shapes.card
			)
			.futureCornerBrackets(shape = config.shapes.card, length = 14.dp, width = 2.dp)
			.futureGlow(
				color = config.colors.primary.copy(alpha = if (hasImage) 0.35f else 0.15f),
				blurRadius = 16f,
				shape = config.shapes.card,
				intensity = if (hasImage) 0.7f else 0.3f
			)
			.clickable(enabled = enabled, onClick = onCapture),
			contentAlignment = Alignment.Center
		) {
			if (hasImage) {
				Column(horizontalAlignment = Alignment.CenterHorizontally) {
					FutureText(
						text = capturedLabel,
						variant = TextVariant.Glow,
						style = FutureTheme.typography.bodyLarge
					)
					Spacer(modifier = Modifier.height(8.dp))
					FutureButton(onClick = onCapture, text = "RETAKE", type = ButtonType.Primary)
				}
			} else {
				Column(horizontalAlignment = Alignment.CenterHorizontally) {
					Box(modifier = Modifier.size(48.dp).drawBehind {
						val c = config.colors.textMuted
						val sw = 2.dp.toPx()
						drawCircle(c, radius = 20.dp.toPx(), style = androidx.compose.ui.graphics.drawscope.Stroke(width = sw))
						drawLine(c, Offset(size.width / 2, 12.dp.toPx()), Offset(size.width / 2, size.height - 12.dp.toPx()), sw)
						drawLine(c, Offset(12.dp.toPx(), size.height / 2), Offset(size.width - 12.dp.toPx(), size.height / 2), sw)
					})
					Spacer(modifier = Modifier.height(8.dp))
					FutureText("CAPTURE IMAGE",
					variant = TextVariant.Muted,
					style = FutureTheme.typography.labelLarge
				)
			}
		}
	}
}

@Composable
private fun FutureMaterial3Theme(content: @Composable () -> Unit) {
	val config = FutureTheme.config
	val container = config.colors.componentBg
	val onContainer = config.colors.textPrimary

	MaterialTheme(
		colorScheme = darkColorScheme(
			primary = config.colors.primary,
			onPrimary = config.colors.globalBg,
			primaryContainer = config.colors.primary.copy(alpha = 0.30f),
			onPrimaryContainer = config.colors.primary,

			surface = container,
			onSurface = onContainer,
			surfaceVariant = container,
			onSurfaceVariant = config.colors.textMuted,

			surfaceContainerLowest = container,
			surfaceContainerLow = container,
			surfaceContainer = container,
			surfaceContainerHigh = container,
			surfaceContainerHighest = container,

			outline = config.colors.primary.copy(alpha = 0.5f),
			outlineVariant = config.colors.primary.copy(alpha = 0.25f),
			background = config.colors.globalBg,
			onBackground = onContainer,
			scrim = config.colors.globalBg.copy(alpha = 0.55f)
		),
		content = content
	)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FutureDatePicker(
	label: String = "",
	value: String,
	onValueChange: (String) -> Unit,
	modifier: Modifier = Modifier
) {
	var showDialog by remember { mutableStateOf(false) }
	val config = FutureTheme.config

	val initialMillis = remember(value) {
		try {
			SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(value)?.time
		} catch (_: Exception) { null }
	}
	val datePickerState = rememberDatePickerState(initialSelectedDateMillis = initialMillis)

	if (showDialog) {
		FutureMaterial3Theme {
			DatePickerDialog(
				onDismissRequest = { showDialog = false },
				confirmButton = {
					TextButton(
						onClick = {
							datePickerState.selectedDateMillis?.let { millis ->
								val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
								onValueChange(sdf.format(Date(millis)))
							}
							showDialog = false
						}
					) {
						FutureText("OK")
					}
				},
				dismissButton = {
					TextButton(onClick = { showDialog = false }) {
						FutureTextMuted("Cancel")
					}
				}
			) {
				DatePicker(
					state = datePickerState,
					colors = DatePickerDefaults.colors(
						containerColor = config.colors.componentBg,
						titleContentColor = config.colors.primary,
						headlineContentColor = config.colors.textPrimary,
						weekdayContentColor = config.colors.textMuted,
						selectedDayContainerColor = config.colors.primary,
						selectedDayContentColor = config.colors.globalBg,
						todayDateBorderColor = config.colors.primary,
						todayContentColor = config.colors.primary,
						dayContentColor = config.colors.textPrimary,
						dayInSelectionRangeContainerColor = config.colors.primary.copy(alpha = 0.15f),
						dayInSelectionRangeContentColor = config.colors.textPrimary,
						dividerColor = config.colors.primary.copy(alpha = 0.4f)
					)
				)
			}
		}
	}

	FutureDateInput(
		label = label,
		value = value,
		onClick = { showDialog = true },
		modifier = modifier
	)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FutureTimePicker(
	label: String = "",
	value: String,
	onValueChange: (String) -> Unit,
	modifier: Modifier = Modifier
) {
	var showDialog by remember { mutableStateOf(false) }
	val config = FutureTheme.config

	val initialHour = remember(value) {
		value.split(":").getOrNull(0)?.toIntOrNull()
		?: java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY)
	}
	val initialMinute = remember(value) {
		value.split(":").getOrNull(1)?.toIntOrNull()
		?: java.util.Calendar.getInstance().get(java.util.Calendar.MINUTE)
	}
	val timePickerState = rememberTimePickerState(
		initialHour = initialHour,
		initialMinute = initialMinute
	)

	if (showDialog) {
		FutureMaterial3Theme {
			AlertDialog(
				onDismissRequest = { showDialog = false },
				containerColor = FutureTheme.config.colors.componentBg,
				title = { FutureTextHeading("Select Time") },
				text = {
					TimePicker(
						state = timePickerState,
						colors = TimePickerDefaults.colors(
							timeSelectorSelectedContainerColor = config.colors.primary,
							timeSelectorSelectedContentColor = config.colors.globalBg,
							timeSelectorUnselectedContainerColor = config.colors.componentBg,
							timeSelectorUnselectedContentColor = config.colors.textPrimary,
							clockDialColor = config.colors.componentBg,
							clockDialSelectedContentColor = config.colors.globalBg,
							clockDialUnselectedContentColor = config.colors.textPrimary,
							selectorColor = config.colors.primary,
							containerColor = config.colors.componentBg,
							periodSelectorBorderColor = config.colors.primary,
							periodSelectorSelectedContainerColor = config.colors.primary.copy(alpha = 0.2f),
							periodSelectorSelectedContentColor = config.colors.primary,
							periodSelectorUnselectedContainerColor = config.colors.componentBg,
							periodSelectorUnselectedContentColor = config.colors.textMuted
						)
					)
				},
				confirmButton = {
					TextButton(
						onClick = {
							onValueChange(
								"${timePickerState.hour.toString().padStart(2, '0')}:${
									timePickerState.minute.toString().padStart(2, '0')
								}"
							)
							showDialog = false
						}
					) {
						FutureText("OK")
					}
				},
				dismissButton = {
					TextButton(onClick = { showDialog = false }) {
						FutureTextMuted("Cancel")
					}
				}
			)
		}
	}

	val interactionSource = remember { MutableInteractionSource() }
	val isHovered by interactionSource.collectIsHoveredAsState()
	val accent = config.colors.accent ?: config.colors.primary

	Column(modifier = modifier.clickable(interactionSource = interactionSource, indication = null) { showDialog = true }) {
		if (label.isNotEmpty()) {
			FutureText(
				text = label,
				variant = TextVariant.Muted,
				style = FutureTheme.typography.labelSmall.copy(letterSpacing = 1.5.sp),
				modifier = Modifier.padding(bottom = 6.dp)
			)
		}
		Box(
			modifier = Modifier
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
			.padding(horizontal = 16.dp, vertical = 14.dp)
		) {
			Row(
				modifier = Modifier.fillMaxWidth(),
				horizontalArrangement = Arrangement.SpaceBetween,
				verticalAlignment = Alignment.CenterVertically
			) {
				FutureText(
					text = value.ifEmpty { "Select Time..." },
					variant = if (value.isNotEmpty()) TextVariant.Primary else TextVariant.Muted,
					style = FutureTheme.typography.bodyLarge
				)
				FutureText(
					text = "◷",
					variant = TextVariant.Primary,
					color = config.colors.primary,
					style = FutureTheme.typography.bodyMedium.copy(fontSize = 18.sp)
				)
			}
		}
	}
}

@Composable
fun FutureColorPickerDialog(
	initialColor: Color,
	onColorSelected: (Color) -> Unit,
	onDismiss: () -> Unit
) {
	val initialHSV = remember(initialColor) { rgbToHsv(initialColor) }
	var hue by remember { mutableFloatStateOf(initialHSV[0]) }
	var saturation by remember { mutableFloatStateOf(initialHSV[1]) }
	var value by remember { mutableFloatStateOf(initialHSV[2]) }

	val currentColor = remember(hue, saturation, value) { hsvToColor(hue, saturation, value) }
	var r by remember { mutableIntStateOf((currentColor.red * 255).toInt()) }
	var g by remember { mutableIntStateOf((currentColor.green * 255).toInt()) }
	var b by remember { mutableIntStateOf((currentColor.blue * 255).toInt()) }

	LaunchedEffect(hue, saturation, value) {
		val c = hsvToColor(hue, saturation, value)
		r = (c.red * 255).toInt().coerceIn(0, 255)
		g = (c.green * 255).toInt().coerceIn(0, 255)
		b = (c.blue * 255).toInt().coerceIn(0, 255)
	}

	fun updateFromRGB(nr: Int, ng: Int, nb: Int) {
		r = nr.coerceIn(0, 255)
		g = ng.coerceIn(0, 255)
		b = nb.coerceIn(0, 255)
		val hsv = rgbToHsv(Color(r, g, b))
		hue = hsv[0]
		saturation = hsv[1]
		value = hsv[2]
	}

	var hexValue by remember { mutableStateOf(colorToHex(currentColor)) }
	LaunchedEffect(r, g, b) { hexValue = colorToHex(Color(r, g, b)) }

	val config = FutureTheme.config

	FutureDialog(
		onDismissRequest = onDismiss
	) {
		Column(
			modifier = Modifier
			.fillMaxWidth()
			.verticalScroll(rememberScrollState()),
			verticalArrangement = Arrangement.spacedBy(6.dp)
		) {
			Box(
				modifier = Modifier
				.fillMaxWidth()
				.height(48.dp)
				.clip(RoundedCornerShape(8.dp))
				.background(currentColor)
				.border(2.dp, config.colors.primary, RoundedCornerShape(8.dp))
				.futureGlowSubtle(color = config.colors.primary, shape = RoundedCornerShape(8.dp)),
				contentAlignment = Alignment.Center
			) {
				FutureText(
					text = hexValue.uppercase(),
					variant = TextVariant.Primary,
					color = if (luminance(r, g, b) > 0.5f) Color.Black else Color.White
				)
			}

			Row(
				modifier = Modifier
				.padding(top = 16.dp)
				.fillMaxWidth()
				.height(210.dp),
				horizontalArrangement = Arrangement.spacedBy(10.dp),
				verticalAlignment = Alignment.CenterVertically
			) {
				ColorWheel(
					hue = hue,
					saturation = saturation,
					value = value,
					onChange = { h, s -> hue = h; saturation = s },
					modifier = Modifier.weight(1f)
				)
				ValueSlider(
					value = value,
					hue = hue,
					saturation = saturation,
					onChange = { value = it },
					modifier = Modifier.width(32.dp).fillMaxHeight()
				)
			}

			CompactSliderRow(
				label = "H",
				value = hue,
				range = 0f..360f,
				brush = Brush.horizontalGradient(
					listOf(Color.Red, Color.Yellow, Color.Green, Color.Cyan, Color.Blue, Color.Magenta, Color.Red)
				)
			) { hue = it }

			CompactSliderRow(
				label = "S",
				value = saturation,
				range = 0f..1f,
				brush = Brush.horizontalGradient(
					listOf(hsvToColor(hue, 0f, value), hsvToColor(hue, 1f, value))
				)
			) { saturation = it }

			CompactSliderRow(
				label = "V",
				value = value,
				range = 0f..1f,
				brush = Brush.horizontalGradient(
					listOf(Color.Black, hsvToColor(hue, saturation, 1f))
				)
			) { value = it }

			CompactSliderRow(
				label = "R",
				value = r.toFloat(),
				range = 0f..255f,
				brush = Brush.horizontalGradient(listOf(Color.Black, Color.Red))
			) { updateFromRGB(it.toInt(), g, b) }

			CompactSliderRow(
				label = "G",
				value = g.toFloat(),
				range = 0f..255f,
				brush = Brush.horizontalGradient(listOf(Color.Black, Color.Green))
			) { updateFromRGB(r, it.toInt(), b) }

			CompactSliderRow(
				label = "B",
				value = b.toFloat(),
				range = 0f..255f,
				brush = Brush.horizontalGradient(listOf(Color.Black, Color.Blue))
			) { updateFromRGB(r, g, it.toInt()) }

			Row(
				modifier = Modifier.fillMaxWidth(),
				verticalAlignment = Alignment.CenterVertically,
				horizontalArrangement = Arrangement.spacedBy(8.dp)
			) {
				FutureText(
					text = "Hex Code:",
					variant = TextVariant.Muted,
					modifier = Modifier.width(68.dp)
				)
				BasicTextField(
					value = hexValue,
					onValueChange = { input ->
						hexValue = input
						val clean = input.trim().removePrefix("#")
						if (clean.length == 6) {
							try {
								updateFromRGB(
									clean.substring(0, 2).toInt(16),
									clean.substring(2, 4).toInt(16),
									clean.substring(4, 6).toInt(16)
								)
							} catch (_: Exception) { }
						}
					},
					modifier = Modifier.weight(1f),
					textStyle = config.typography.bodyLarge.copy(
						color = config.colors.textPrimary,
						fontFamily = config.fonts.regular
					),
					singleLine = true,
					cursorBrush = SolidColor(config.colors.primary),
					decorationBox = { innerTextField ->
						Box(
							modifier = Modifier
							.fillMaxWidth()
							.clip(RoundedCornerShape(6.dp))
							.background(config.colors.componentBg)
							.border(1.dp, config.colors.primary, RoundedCornerShape(6.dp))
							.padding(horizontal = 12.dp, vertical = 10.dp)
						) {
							innerTextField()
						}
					}
				)
			}

			Spacer(modifier = Modifier.height(4.dp))

			Row(
				modifier = Modifier.fillMaxWidth(),
				horizontalArrangement = Arrangement.spacedBy(12.dp)
			) {
				FutureButton(
					onClick = onDismiss,
					text = "CANCEL",
					modifier = Modifier.weight(1f)
				)
				FutureButton(
					onClick = { onColorSelected(currentColor); onDismiss() },
					text = "APPLY",
					type = ButtonType.Highlighted,
					modifier = Modifier.weight(1f)
				)
			}
		}
	}
}

@Composable
private fun CompactSliderRow(
	label: String,
	value: Float,
	range: ClosedFloatingPointRange<Float>,
	brush: Brush,
	onChange: (Float) -> Unit
) {
	val density = LocalDensity.current

	Row(
		modifier = Modifier
		.fillMaxWidth()
		.height(28.dp),
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.spacedBy(8.dp)
	) {
		FutureText(
			text = label,
			variant = TextVariant.Muted,
			modifier = Modifier.width(20.dp)
		)

		BoxWithConstraints(
			modifier = Modifier
			.weight(1f)
			.height(10.dp)
		) {
			val widthPx = with(density) { maxWidth.toPx() }

			Canvas(
				modifier = Modifier
				.fillMaxSize()
				.clip(RoundedCornerShape(5.dp))
				.pointerInput(Unit) {
					detectDragGestures { change, _ ->
						change.consume()
						val f = (change.position.x / widthPx).coerceIn(0f, 1f)
						onChange(range.start + f * (range.endInclusive - range.start))
					}
				}
			) {
				drawRect(brush)
			}

			val fraction = (value - range.start) / (range.endInclusive - range.start)
			Box(
				modifier = Modifier
				.offset(
					x = with(density) { (fraction * widthPx).toDp() } - 7.dp,
					y = (-2).dp
				)
				.size(14.dp)
				.clip(RoundedCornerShape(7.dp))
				.background(Color.White)
				.border(2.dp, FutureTheme.config.colors.primary, RoundedCornerShape(7.dp))
			)
		}

		val text = when {
			range.endInclusive == 360f -> "%.0f".format(value)
			range.endInclusive == 255f -> "%.0f".format(value)
			else -> "%.2f".format(value)
		}
		FutureText(
			text = text,
			variant = TextVariant.Primary,
			modifier = Modifier.width(36.dp),
			textAlign = TextAlign.End
		)
	}
}

@Composable
private fun ColorWheel(
	hue: Float,
	saturation: Float,
	value: Float,
	onChange: (Float, Float) -> Unit,
	modifier: Modifier = Modifier
) {
	val density = LocalDensity.current

	BoxWithConstraints(
		modifier = modifier.aspectRatio(1f),
		contentAlignment = Alignment.Center
	) {
		val size = minOf(maxWidth, maxHeight)
		val sizePx = with(density) { size.toPx() }
		val radius = sizePx / 3f
		val hueRad = remember(hue) { hue * PI.toFloat() / 180f }

		Canvas(
			modifier = Modifier
			.size(size)
			.pointerInput(Unit) {
				detectDragGestures { change, _ ->
					change.consume()
					val center = Offset(sizePx / 2, sizePx / 2)
					val dx = change.position.x - center.x
					val dy = change.position.y - center.y
					val dist = sqrt(dx * dx + dy * dy).coerceAtMost(radius)
					val sat = dist / radius
					val h = (atan2(dy, dx) * 180f / PI.toFloat() + 360f) % 360f
					onChange(h, sat)
				}
			}
		) {
			val center = Offset(sizePx / 2, sizePx / 2)

			drawCircle(
				brush = Brush.sweepGradient(
					colors = listOf(
						Color.Red, Color.Yellow, Color.Green,
						Color.Cyan, Color.Blue, Color.Magenta, Color.Red
					),
					center = center
				),
				radius = radius
			)

			drawCircle(
				brush = Brush.radialGradient(
					colors = listOf(Color.White, Color.Transparent),
					center = center,
					radius = radius
				),
				radius = radius
			)

			if (value < 1f) {
				drawCircle(
					color = Color.Black.copy(alpha = 1f - value),
					radius = radius
				)
			}
		}

		Box(
			modifier = Modifier
			.offset(
				x = with(density) { (saturation * radius * cos(hueRad)).toDp() },
				y = with(density) { (saturation * radius * sin(hueRad)).toDp() }
			)
			.size(14.dp)
			.border(2.dp, Color.White, RoundedCornerShape(7.dp))
			.background(Color.Black.copy(alpha = 0.35f), RoundedCornerShape(7.dp))
		)
	}
}

@Composable
private fun ValueSlider(
	value: Float,
	hue: Float,
	saturation: Float,
	onChange: (Float) -> Unit,
	modifier: Modifier = Modifier
) {
	val density = LocalDensity.current

	BoxWithConstraints(modifier = modifier) {
		val hPx = with(density) { maxHeight.toPx() }

		Canvas(
			modifier = Modifier
			.fillMaxSize()
			.clip(RoundedCornerShape(8.dp))
			.pointerInput(Unit) {
				detectDragGestures { change, _ ->
					change.consume()
					onChange((1f - change.position.y / hPx).coerceIn(0f, 1f))
				}
			}
		) {
			drawRect(
				brush = Brush.verticalGradient(
					colors = listOf(hsvToColor(hue, saturation, 1f), Color.Black),
					startY = 0f,
					endY = size.height
				)
			)
		}

		val thumbY = with(density) { ((1f - value) * hPx).toDp() - 6.dp }
		Box(
			modifier = Modifier
			.fillMaxWidth()
			.offset(y = thumbY)
			.height(12.dp)
			.border(2.dp, Color.White, RoundedCornerShape(2.dp))
			.background(Color.Black.copy(alpha = 0.35f), RoundedCornerShape(2.dp))
		)
	}
}

private fun rgbToHsv(color: Color): FloatArray {
	val r = color.red
	val g = color.green
	val b = color.blue
	val max = maxOf(r, g, b)
	val min = minOf(r, g, b)
	val d = max - min

	val h = when {
		d == 0f -> 0f
		max == r -> ((g - b) / d) % 6f
		max == g -> ((b - r) / d) + 2f
		else -> ((r - g) / d) + 4f
	} * 60f

	return floatArrayOf(
		if (h < 0) h + 360f else h,
		if (max == 0f) 0f else d / max,
		max
	)
}

private fun hsvToColor(h: Float, s: Float, v: Float): Color {
	val c = v * s
	val x = c * (1 - abs((h / 60f) % 2f - 1f))
	val m = v - c

	val (r, g, b) = when {
		h < 60f  -> Triple(c, x, 0f)
		h < 120f -> Triple(x, c, 0f)
		h < 180f -> Triple(0f, c, x)
		h < 240f -> Triple(0f, x, c)
		h < 300f -> Triple(x, 0f, c)
		else     -> Triple(c, 0f, x)
	}

	return Color(
		red   = (r + m).coerceIn(0f, 1f),
		green = (g + m).coerceIn(0f, 1f),
		blue  = (b + m).coerceIn(0f, 1f)
	)
}

private fun colorToHex(color: Color): String {
	return String.format(
		"#%02X%02X%02X",
		(color.red   * 255).toInt(),
		(color.green * 255).toInt(),
		(color.blue  * 255).toInt()
	)
}

private fun luminance(r: Int, g: Int, b: Int): Float {
	return (0.299f * r + 0.587f * g + 0.114f * b) / 255f
}
