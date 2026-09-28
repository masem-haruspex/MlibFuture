package com.mlib.future.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.unit.dp
import com.mlib.future.FutureTheme
import com.mlib.future.components.FutureText
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.PI

@Composable
private fun IconBox(
	modifier: Modifier = Modifier,
	size: Int = 24,
	color: Color? = null,
	content: @Composable () -> Unit
) {
	val c = color ?: FutureTheme.config.colors.primary
	Box(
		modifier = modifier.size(size.dp),
		contentAlignment = Alignment.Center
	) {
		content()
	}
}

@Composable
fun FutureIconCheckmark(modifier: Modifier = Modifier, size: Int = 24, color: Color? = null) {
	val c = color ?: FutureTheme.config.colors.primary
	IconBox(modifier = modifier, size = size, color = c) {
		Canvas(modifier = Modifier.fillMaxSize()) {
			val size = this.size.width
			val stroke = size / 24f
			val cx = size / 2f
			val cy = size / 2f
			val unit = size / 24f
			val neonBrush = Brush.radialGradient(colors = listOf(c, c.copy(alpha = 0.3f)), center = Offset(cx, cy), radius = size / 1.5f)
			drawLine(neonBrush, Offset(cx - 5 * unit, cy), Offset(cx - 1 * unit, cy + 4 * unit), stroke, cap = StrokeCap.Square)
			drawLine(neonBrush, Offset(cx - 1 * unit, cy + 4 * unit), Offset(cx + 7 * unit, cy - 5 * unit), stroke, cap = StrokeCap.Square)
		}
	}
}

@Composable
fun FutureIconRemove(modifier: Modifier = Modifier, size: Int = 24, color: Color? = null) {
	val c = color ?: FutureTheme.config.colors.primary
	IconBox(modifier = modifier, size = size, color = c) {
		Canvas(modifier = Modifier.fillMaxSize()) {
			val size = this.size.width
			val stroke = size / 24f
			val cx = size / 2f
			val cy = size / 2f
			val unit = size / 24f
			val d = 5.5f * unit
			val neonBrush = Brush.radialGradient(colors = listOf(c, c.copy(alpha = 0.3f)), center = Offset(cx, cy), radius = size / 1.5f)
			drawLine(neonBrush, Offset(cx - d, cy - d), Offset(cx + d, cy + d), stroke, cap = StrokeCap.Square)
			drawLine(neonBrush, Offset(cx + d, cy - d), Offset(cx - d, cy + d), stroke, cap = StrokeCap.Square)
		}
	}
}

@Composable
fun FutureIconClose(modifier: Modifier = Modifier, size: Int = 24, color: Color? = null) =
FutureIconRemove(modifier = modifier, size = size, color = color)

@Composable
fun FutureIconShare(modifier: Modifier = Modifier, size: Int = 24, color: Color? = null) {
	val c = color ?: FutureTheme.config.colors.primary
	IconBox(modifier = modifier, size = size, color = c) {
		Canvas(modifier = Modifier.fillMaxSize()) {
			val size = this.size.width
			val stroke = size / 24f
			val cx = size / 2f
			val cy = size / 2f
			val unit = size / 24f
			val neonBrush = Brush.radialGradient(colors = listOf(c, c.copy(alpha = 0.3f)), center = Offset(cx, cy), radius = size / 1.5f)
			drawCircle(neonBrush, radius = 2.5f * unit, center = Offset(cx + 4 * unit, cy - 4 * unit))
			drawCircle(neonBrush, radius = 2.5f * unit, center = Offset(cx - 4 * unit, cy))
			drawCircle(neonBrush, radius = 2.5f * unit, center = Offset(cx + 4 * unit, cy + 4 * unit))
			drawLine(neonBrush, Offset(cx + 2 * unit, cy - 2.5f * unit), Offset(cx - 2 * unit, cy - 1 * unit), stroke, cap = StrokeCap.Square)
			drawLine(neonBrush, Offset(cx + 2 * unit, cy + 2.5f * unit), Offset(cx - 2 * unit, cy + 1 * unit), stroke, cap = StrokeCap.Square)
		}
	}
}

@Composable
fun FutureIconData(modifier: Modifier = Modifier, size: Int = 24, color: Color? = null) {
	val c = color ?: FutureTheme.config.colors.primary
	IconBox(modifier = modifier, size = size, color = c) {
		Canvas(modifier = Modifier.fillMaxSize()) {
			val size = this.size.width
			val stroke = size / 24f
			val cx = size / 2f
			val cy = size / 2f
			val unit = size / 24f
			val r = 7f * unit
			val path = Path().apply {
				moveTo(cx, cy - r)
				lineTo(cx + r * 0.866f, cy - r * 0.5f)
				lineTo(cx + r * 0.866f, cy + r * 0.5f)
				lineTo(cx, cy + r)
				lineTo(cx - r * 0.866f, cy + r * 0.5f)
				lineTo(cx - r * 0.866f, cy - r * 0.5f)
				close()
			}
			val neonBrush = Brush.radialGradient(colors = listOf(c, c.copy(alpha = 0.3f)), center = Offset(cx, cy), radius = size / 1.5f)
			drawPath(path, brush = neonBrush, style = Stroke(width = stroke))
			drawLine(neonBrush, Offset(cx - r * 0.866f, cy - r * 0.5f), Offset(cx, cy), stroke, cap = StrokeCap.Square)
			drawLine(neonBrush, Offset(cx, cy), Offset(cx + r * 0.866f, cy - r * 0.5f), stroke, cap = StrokeCap.Square)
			drawLine(neonBrush, Offset(cx, cy), Offset(cx, cy + r), stroke, cap = StrokeCap.Square)
		}
	}
}

@Composable
fun FutureIconRecord(modifier: Modifier = Modifier, size: Int = 24, color: Color? = null) {
	val c = color ?: FutureTheme.config.colors.primary
	IconBox(modifier = modifier, size = size, color = c) {
		Canvas(modifier = Modifier.fillMaxSize()) {
			val size = this.size.width
			val cx = size / 2f
			val cy = size / 2f
			val unit = size / 24f
			val stroke = size / 24f
			val neonBrush = Brush.radialGradient(colors = listOf(c, c.copy(alpha = 0.3f)), center = Offset(cx, cy), radius = size / 1.5f)
			drawCircle(neonBrush, radius = 6 * unit, center = Offset(cx, cy), style = Stroke(width = stroke))
			drawCircle(neonBrush, radius = 2.5f * unit, center = Offset(cx, cy))
		}
	}
}

@Composable
fun FutureIconStopRecording(modifier: Modifier = Modifier, size: Int = 24, color: Color? = null) {
	val c = color ?: FutureTheme.config.colors.primary
	IconBox(modifier = modifier, size = size, color = c) {
		Canvas(modifier = Modifier.fillMaxSize()) {
			val size = this.size.width
			val cx = size / 2f
			val cy = size / 2f
			val unit = size / 24f
			val stroke = size / 24f
			val neonBrush = Brush.radialGradient(colors = listOf(c, c.copy(alpha = 0.3f)), center = Offset(cx, cy), radius = size / 1.5f)
			drawCircle(neonBrush, radius = 6 * unit, center = Offset(cx, cy), style = Stroke(width = stroke))
			drawRect(neonBrush, topLeft = Offset(cx - 2.5f * unit, cy - 2.5f * unit), size = Size(5 * unit, 5 * unit))
		}
	}
}

@Composable
fun FutureIconLocation(modifier: Modifier = Modifier, size: Int = 24, color: Color? = null) {
	val c = color ?: FutureTheme.config.colors.primary
	IconBox(modifier = modifier, size = size, color = c) {
		Canvas(modifier = Modifier.fillMaxSize()) {
			val size = this.size.width
			val cx = size / 2f
			val cy = size / 2f
			val unit = size / 24f
			val stroke = size / 24f
			val neonBrush = Brush.radialGradient(colors = listOf(c, c.copy(alpha = 0.3f)), center = Offset(cx, cy), radius = size / 1.5f)
			drawCircle(neonBrush, radius = 4 * unit, center = Offset(cx, cy - 2 * unit), style = Stroke(width = stroke))
			drawLine(neonBrush, Offset(cx, cy + 2 * unit), Offset(cx, cy + 7 * unit), stroke, cap = StrokeCap.Square)
			drawLine(neonBrush, Offset(cx - 3 * unit, cy + 7 * unit), Offset(cx + 3 * unit, cy + 7 * unit), stroke, cap = StrokeCap.Square)
			drawCircle(neonBrush, radius = 1.5f * unit, center = Offset(cx, cy - 2 * unit))
		}
	}
}

@Composable
fun FutureIconPin(modifier: Modifier = Modifier, size: Int = 24, color: Color? = null) =
FutureIconLocation(modifier = modifier, size = size, color = color)

@Composable
fun FutureIconImport(modifier: Modifier = Modifier, size: Int = 24, color: Color? = null) {
	val c = color ?: FutureTheme.config.colors.primary
	IconBox(modifier = modifier, size = size, color = c) {
		Canvas(modifier = Modifier.fillMaxSize()) {
			val size = this.size.width
			val stroke = size / 24f
			val cx = size / 2f
			val cy = size / 2f
			val unit = size / 24f
			val neonBrush = Brush.radialGradient(colors = listOf(c, c.copy(alpha = 0.3f)), center = Offset(cx, cy), radius = size / 1.5f)
			drawLine(neonBrush, Offset(cx, cy - 6 * unit), Offset(cx, cy + 2 * unit), stroke, cap = StrokeCap.Square)
			drawLine(neonBrush, Offset(cx - 3.5f * unit, cy - 2 * unit), Offset(cx, cy + 2 * unit), stroke, cap = StrokeCap.Square)
			drawLine(neonBrush, Offset(cx + 3.5f * unit, cy - 2 * unit), Offset(cx, cy + 2 * unit), stroke, cap = StrokeCap.Square)
			drawLine(neonBrush, Offset(cx - 5 * unit, cy + 5 * unit), Offset(cx + 5 * unit, cy + 5 * unit), stroke, cap = StrokeCap.Square)
			drawLine(neonBrush, Offset(cx - 5 * unit, cy + 5 * unit), Offset(cx - 5 * unit, cy + 2 * unit), stroke, cap = StrokeCap.Square)
			drawLine(neonBrush, Offset(cx + 5 * unit, cy + 5 * unit), Offset(cx + 5 * unit, cy + 2 * unit), stroke, cap = StrokeCap.Square)
		}
	}
}

@Composable
fun FutureIconExport(modifier: Modifier = Modifier, size: Int = 24, color: Color? = null) {
	val c = color ?: FutureTheme.config.colors.primary
	IconBox(modifier = modifier, size = size, color = c) {
		Canvas(modifier = Modifier.fillMaxSize()) {
			val size = this.size.width
			val stroke = size / 24f
			val cx = size / 2f
			val cy = size / 2f
			val unit = size / 24f
			val neonBrush = Brush.radialGradient(colors = listOf(c, c.copy(alpha = 0.3f)), center = Offset(cx, cy), radius = size / 1.5f)
			drawLine(neonBrush, Offset(cx, cy + 2 * unit), Offset(cx, cy - 6 * unit), stroke, cap = StrokeCap.Square)
			drawLine(neonBrush, Offset(cx - 3.5f * unit, cy - 2 * unit), Offset(cx, cy - 6 * unit), stroke, cap = StrokeCap.Square)
			drawLine(neonBrush, Offset(cx + 3.5f * unit, cy - 2 * unit), Offset(cx, cy - 6 * unit), stroke, cap = StrokeCap.Square)
			drawLine(neonBrush, Offset(cx - 5 * unit, cy + 5 * unit), Offset(cx + 5 * unit, cy + 5 * unit), stroke, cap = StrokeCap.Square)
			drawLine(neonBrush, Offset(cx - 5 * unit, cy + 5 * unit), Offset(cx - 5 * unit, cy + 2 * unit), stroke, cap = StrokeCap.Square)
			drawLine(neonBrush, Offset(cx + 5 * unit, cy + 5 * unit), Offset(cx + 5 * unit, cy + 2 * unit), stroke, cap = StrokeCap.Square)
		}
	}
}

@Composable
fun FutureIconSend(modifier: Modifier = Modifier, size: Int = 24, color: Color? = null) {
	val c = color ?: FutureTheme.config.colors.primary
	IconBox(modifier = modifier, size = size, color = c) {
		Canvas(modifier = Modifier.fillMaxSize()) {
			val size = this.size.width
			val stroke = size / 24f
			val cx = size / 2f
			val cy = size / 2f
			val unit = size / 24f
			val path = Path().apply {
				moveTo(cx - 7 * unit, cy - 3 * unit)
				lineTo(cx + 7 * unit, cy)
				lineTo(cx - 7 * unit, cy + 3 * unit)
				lineTo(cx - 4 * unit, cy)
				close()
			}
			val neonBrush = Brush.radialGradient(colors = listOf(c, c.copy(alpha = 0.3f)), center = Offset(cx, cy), radius = size / 1.5f)
			drawPath(path, brush = neonBrush, style = Stroke(width = stroke))
		}
	}
}

@Composable
fun FutureIconSubmit(modifier: Modifier = Modifier, size: Int = 24, color: Color? = null) =
FutureIconSend(modifier = modifier, size = size, color = color)

@Composable
fun FutureIconDiscard(modifier: Modifier = Modifier, size: Int = 24, color: Color? = null) =
FutureIconRemove(modifier = modifier, size = size, color = color)

@Composable
fun FutureIconEdit(modifier: Modifier = Modifier, size: Int = 24, color: Color? = null) {
	val c = color ?: FutureTheme.config.colors.primary
	IconBox(modifier = modifier, size = size, color = c) {
		Canvas(modifier = Modifier.fillMaxSize()) {
			val size = this.size.width
			val stroke = size / 24f
			val cx = size / 2f
			val cy = size / 2f
			val unit = size / 24f
			val path = Path().apply {
				moveTo(cx + 3 * unit, cy - 7 * unit)
				lineTo(cx + 7 * unit, cy - 3 * unit)
				lineTo(cx - 3 * unit, cy + 7 * unit)
				lineTo(cx - 7 * unit, cy + 7 * unit)
				lineTo(cx - 7 * unit, cy + 3 * unit)
				close()
			}
			val neonBrush = Brush.radialGradient(colors = listOf(c, c.copy(alpha = 0.3f)), center = Offset(cx, cy), radius = size / 1.5f)
			drawPath(path, brush = neonBrush, style = Stroke(width = stroke))
		}
	}
}

@Composable
fun FutureIconSubscriptions(modifier: Modifier = Modifier, size: Int = 24, color: Color? = null) {
	val c = color ?: FutureTheme.config.colors.primary
	IconBox(modifier = modifier, size = size, color = c) {
		Canvas(modifier = Modifier.fillMaxSize()) {
			val size = this.size.width
			val stroke = size / 24f
			val cx = size / 2f
			val cy = size / 2f
			val unit = size / 24f
			val neonBrush = Brush.radialGradient(colors = listOf(c, c.copy(alpha = 0.3f)), center = Offset(cx, cy), radius = size / 1.5f)
			drawLine(neonBrush, Offset(cx - 7 * unit, cy - 4 * unit), Offset(cx + 7 * unit, cy - 4 * unit), stroke, cap = StrokeCap.Square)
			drawLine(neonBrush, Offset(cx - 7 * unit, cy), Offset(cx + 7 * unit, cy), stroke, cap = StrokeCap.Square)
			drawLine(neonBrush, Offset(cx - 7 * unit, cy + 4 * unit), Offset(cx + 3 * unit, cy + 4 * unit), stroke, cap = StrokeCap.Square)
			drawCircle(neonBrush, radius = 2 * unit, center = Offset(cx + 5 * unit, cy + 4 * unit))
		}
	}
}

@Composable
fun FutureIconHistory(modifier: Modifier = Modifier, size: Int = 24, color: Color? = null) {
	val c = color ?: FutureTheme.config.colors.primary
	IconBox(modifier = modifier, size = size, color = c) {
		Canvas(modifier = Modifier.fillMaxSize()) {
			val size = this.size.width
			val stroke = size / 24f
			val cx = size / 2f
			val cy = size / 2f
			val unit = size / 24f
			val neonBrush = Brush.radialGradient(colors = listOf(c, c.copy(alpha = 0.3f)), center = Offset(cx, cy), radius = size / 1.5f)
			drawCircle(neonBrush, radius = 7 * unit, center = Offset(cx, cy), style = Stroke(width = stroke))
			drawLine(neonBrush, Offset(cx, cy - 4 * unit), Offset(cx, cy), stroke, cap = StrokeCap.Square)
			drawLine(neonBrush, Offset(cx, cy), Offset(cx + 3 * unit, cy), stroke, cap = StrokeCap.Square)
			drawLine(neonBrush, Offset(cx, cy - 8 * unit), Offset(cx, cy - 7 * unit), stroke, cap = StrokeCap.Square)
			drawLine(neonBrush, Offset(cx, cy + 7 * unit), Offset(cx, cy + 8 * unit), stroke, cap = StrokeCap.Square)
			drawLine(neonBrush, Offset(cx - 8 * unit, cy), Offset(cx - 7 * unit, cy), stroke, cap = StrokeCap.Square)
			drawLine(neonBrush, Offset(cx + 7 * unit, cy), Offset(cx + 8 * unit, cy), stroke, cap = StrokeCap.Square)
		}
	}
}

@Composable
fun FutureIconBookmark(modifier: Modifier = Modifier, size: Int = 24, color: Color? = null) {
	val c = color ?: FutureTheme.config.colors.primary
	IconBox(modifier = modifier, size = size, color = c) {
		Canvas(modifier = Modifier.fillMaxSize()) {
			val size = this.size.width
			val stroke = size / 24f
			val cx = size / 2f
			val cy = size / 2f
			val unit = size / 24f
			val path = Path().apply {
				moveTo(cx - 5 * unit, cy - 7 * unit)
				lineTo(cx + 5 * unit, cy - 7 * unit)
				lineTo(cx + 5 * unit, cy + 7 * unit)
				lineTo(cx, cy + 4 * unit)
				lineTo(cx - 5 * unit, cy + 7 * unit)
				close()
			}
			val neonBrush = Brush.radialGradient(colors = listOf(c, c.copy(alpha = 0.3f)), center = Offset(cx, cy), radius = size / 1.5f)
			drawPath(path, brush = neonBrush, style = Stroke(width = stroke))
		}
	}
}

@Composable
fun FutureIconDownload(modifier: Modifier = Modifier, size: Int = 24, color: Color? = null) {
	val c = color ?: FutureTheme.config.colors.primary
	IconBox(modifier = modifier, size = size, color = c) {
		Canvas(modifier = Modifier.fillMaxSize()) {
			val size = this.size.width
			val stroke = size / 24f
			val cx = size / 2f
			val cy = size / 2f
			val unit = size / 24f
			val neonBrush = Brush.radialGradient(colors = listOf(c, c.copy(alpha = 0.3f)), center = Offset(cx, cy), radius = size / 1.5f)
			drawLine(neonBrush, Offset(cx, cy - 5 * unit), Offset(cx, cy + 4 * unit), stroke, cap = StrokeCap.Square)
			drawLine(neonBrush, Offset(cx - 4 * unit, cy + 1 * unit), Offset(cx, cy + 5 * unit), stroke, cap = StrokeCap.Square)
			drawLine(neonBrush, Offset(cx + 4 * unit, cy + 1 * unit), Offset(cx, cy + 5 * unit), stroke, cap = StrokeCap.Square)
			drawLine(neonBrush, Offset(cx - 6 * unit, cy + 6 * unit), Offset(cx + 6 * unit, cy + 6 * unit), stroke, cap = StrokeCap.Square)
		}
	}
}

@Composable
fun FutureIconInfo(modifier: Modifier = Modifier, size: Int = 24, color: Color? = null) {
	val c = color ?: FutureTheme.config.colors.primary
	IconBox(modifier = modifier, size = size, color = c) {
		Canvas(modifier = Modifier.fillMaxSize()) {
			val size = this.size.width
			val stroke = size / 24f
			val cx = size / 2f
			val cy = size / 2f
			val unit = size / 24f
			val neonBrush = Brush.radialGradient(colors = listOf(c, c.copy(alpha = 0.3f)), center = Offset(cx, cy), radius = size / 1.5f)
			drawCircle(neonBrush, radius = 7 * unit, center = Offset(cx, cy), style = Stroke(width = stroke))
			drawCircle(neonBrush, radius = 1 * unit, center = Offset(cx, cy - 3 * unit))
			drawLine(neonBrush, Offset(cx, cy - 1 * unit), Offset(cx, cy + 4 * unit), stroke, cap = StrokeCap.Square)
		}
	}
}

@Composable
fun FutureIconAdd(modifier: Modifier = Modifier, size: Int = 24, color: Color? = null) {
	val c = color ?: FutureTheme.config.colors.primary
	IconBox(modifier = modifier, size = size, color = c) {
		Canvas(modifier = Modifier.fillMaxSize()) {
			val size = this.size.width
			val stroke = size / 24f
			val cx = size / 2f
			val cy = size / 2f
			val unit = size / 24f
			val neonBrush = Brush.radialGradient(colors = listOf(c, c.copy(alpha = 0.3f)), center = Offset(cx, cy), radius = size / 1.5f)
			drawLine(neonBrush, Offset(cx, cy - 6 * unit), Offset(cx, cy + 6 * unit), stroke, cap = StrokeCap.Square)
			drawLine(neonBrush, Offset(cx - 6 * unit, cy), Offset(cx + 6 * unit, cy), stroke, cap = StrokeCap.Square)
		}
	}
}

@Composable
fun FutureIconFolder(modifier: Modifier = Modifier, size: Int = 24, color: Color? = null) {
	val c = color ?: FutureTheme.config.colors.primary
	IconBox(modifier = modifier, size = size, color = c) {
		Canvas(modifier = Modifier.fillMaxSize()) {
			val size = this.size.width
			val stroke = size / 24f
			val cx = size / 2f
			val cy = size / 2f
			val unit = size / 24f
			val path = Path().apply {
				moveTo(cx - 6 * unit, cy - 1 * unit)
				lineTo(cx - 3 * unit, cy - 1 * unit)
				lineTo(cx - 1 * unit, cy - 4 * unit)
				lineTo(cx + 6 * unit, cy - 4 * unit)
				lineTo(cx + 6 * unit, cy + 5 * unit)
				lineTo(cx - 6 * unit, cy + 5 * unit)
				close()
			}
			val neonBrush = Brush.radialGradient(colors = listOf(c, c.copy(alpha = 0.3f)), center = Offset(cx, cy), radius = size / 1.5f)
			drawPath(path, brush = neonBrush, style = Stroke(width = stroke))
		}
	}
}

@Composable
fun FutureIconNote(modifier: Modifier = Modifier, size: Int = 24, color: Color? = null) {
	val c = color ?: FutureTheme.config.colors.primary
	IconBox(modifier = modifier, size = size, color = c) {
		Canvas(modifier = Modifier.fillMaxSize()) {
			val size = this.size.width
			val stroke = size / 24f
			val cx = size / 2f
			val cy = size / 2f
			val unit = size / 24f
			val neonBrush = Brush.radialGradient(colors = listOf(c, c.copy(alpha = 0.3f)), center = Offset(cx, cy), radius = size / 1.5f)
			drawRect(neonBrush, topLeft = Offset(cx - 5 * unit, cy - 6 * unit), size = Size(10 * unit, 12 * unit), style = Stroke(width = stroke))
			drawLine(neonBrush, Offset(cx - 3 * unit, cy - 2 * unit), Offset(cx + 3 * unit, cy - 2 * unit), stroke, cap = StrokeCap.Square)
			drawLine(neonBrush, Offset(cx - 3 * unit, cy + 2 * unit), Offset(cx + 2 * unit, cy + 2 * unit), stroke, cap = StrokeCap.Square)
		}
	}
}

@Composable
fun FutureIconSettings(modifier: Modifier = Modifier, size: Int = 24, color: Color? = null) {
	val c = color ?: FutureTheme.config.colors.primary
	IconBox(modifier = modifier, size = size, color = c) {
		Canvas(modifier = Modifier.fillMaxSize()) {
			val s = this.size.width
			val stroke = s / 24f
			val cx = s / 2f
			val cy = s / 2f
			val unit = s / 24f
			val neonBrush = Brush.radialGradient(
				colors = listOf(c, c.copy(alpha = 0.3f)),
				center = Offset(cx, cy),
				radius = s / 1.5f
			)

			val toothCount = 8
			val rBody = 5.5f  * unit
			val rTip  = 8.25f * unit
			val rHub  = 2.42f * unit
			val halfRoot = 11.5f
			val halfTip  = 7.5f

			fun polar(deg: Float, r: Float) = Offset(
				cx + cos(deg * PI.toFloat() / 180f) * r,
				cy + sin(deg * PI.toFloat() / 180f) * r
			)

			val step = 360f / toothCount
			val gear = Path()
			for (i in 0 until toothCount) {
				val a = i * step
				val pts = listOf(
					polar(a - halfRoot, rBody),
					polar(a - halfTip,  rTip),
					polar(a + halfTip,  rTip),
					polar(a + halfRoot, rBody)
				)
				pts.forEachIndexed { idx, p ->
					if (i == 0 && idx == 0) gear.moveTo(p.x, p.y)
					else gear.lineTo(p.x, p.y)
				}
			}
			gear.close()
			drawPath(gear, brush = neonBrush, style = Stroke(width = stroke))

			drawCircle(
				brush = neonBrush,
				radius = rHub,
				center = Offset(cx, cy),
				style = Stroke(width = stroke)
			)
		}
	}
}

@Composable
fun FutureIconScan(modifier: Modifier = Modifier, size: Int = 24, color: Color? = null) {
	val c = color ?: FutureTheme.config.colors.primary
	IconBox(modifier = modifier, size = size, color = c) {
		Canvas(modifier = Modifier.fillMaxSize()) {
			val size = this.size.width
			val stroke = size / 24f
			val cx = size / 2f
			val cy = size / 2f
			val unit = size / 24f
			val d = 6 * unit
			val neonBrush = Brush.radialGradient(colors = listOf(c, c.copy(alpha = 0.3f)), center = Offset(cx, cy), radius = size / 1.5f)
			drawLine(neonBrush, Offset(cx - d, cy - d + 3 * unit), Offset(cx - d, cy - d), stroke, cap = StrokeCap.Square)
			drawLine(neonBrush, Offset(cx - d, cy - d), Offset(cx - d + 3 * unit, cy - d), stroke, cap = StrokeCap.Square)
			drawLine(neonBrush, Offset(cx + d - 3 * unit, cy - d), Offset(cx + d, cy - d), stroke, cap = StrokeCap.Square)
			drawLine(neonBrush, Offset(cx + d, cy - d), Offset(cx + d, cy - d + 3 * unit), stroke, cap = StrokeCap.Square)
			drawLine(neonBrush, Offset(cx + d, cy + d - 3 * unit), Offset(cx + d, cy + d), stroke, cap = StrokeCap.Square)
			drawLine(neonBrush, Offset(cx + d, cy + d), Offset(cx + d - 3 * unit, cy + d), stroke, cap = StrokeCap.Square)
			drawLine(neonBrush, Offset(cx - d + 3 * unit, cy + d), Offset(cx - d, cy + d), stroke, cap = StrokeCap.Square)
			drawLine(neonBrush, Offset(cx - d, cy + d), Offset(cx - d, cy + d - 3 * unit), stroke, cap = StrokeCap.Square)
			drawLine(neonBrush, Offset(cx - d, cy), Offset(cx + d, cy), stroke, cap = StrokeCap.Square)
		}
	}
}

@Composable
fun FutureIconChooseTheme(modifier: Modifier = Modifier, size: Int = 24, color: Color? = null) {
	val c = color ?: FutureTheme.config.colors.primary
	IconBox(modifier = modifier, size = size, color = c) {
		Canvas(modifier = Modifier.fillMaxSize()) {
			val size = this.size.width
			val cx = size / 2f
			val cy = size / 2f
			val unit = size / 24f
			val r = 7 * unit
			val stroke = size / 24f
			val neonBrush = Brush.radialGradient(colors = listOf(c, c.copy(alpha = 0.3f)), center = Offset(cx, cy), radius = size / 1.5f)
			drawCircle(neonBrush, radius = r, center = Offset(cx, cy), style = Stroke(width = stroke))
			drawArc(
				brush = neonBrush,
				startAngle = 90f,
				sweepAngle = 180f,
				useCenter = true,
				topLeft = Offset(cx - r, cy - r),
				size = Size(r * 2, r * 2)
			)
		}
	}
}

@Composable
fun FutureIconPlay(modifier: Modifier = Modifier, size: Int = 24, color: Color? = null) {
	val c = color ?: FutureTheme.config.colors.primary
	IconBox(modifier = modifier, size = size, color = c) {
		Canvas(modifier = Modifier.fillMaxSize()) {
			val size = this.size.width
			val stroke = size / 24f
			val cx = size / 2f
			val cy = size / 2f
			val unit = size / 24f
			val path = Path().apply {
				moveTo(cx - 4 * unit, cy - 6 * unit)
				lineTo(cx + 6 * unit, cy)
				lineTo(cx - 4 * unit, cy + 6 * unit)
				close()
			}
			val neonBrush = Brush.radialGradient(colors = listOf(c, c.copy(alpha = 0.3f)), center = Offset(cx, cy), radius = size / 1.5f)
			drawPath(path, brush = neonBrush, style = Stroke(width = stroke))
		}
	}
}

@Composable
fun FutureIconPause(modifier: Modifier = Modifier, size: Int = 24, color: Color? = null) {
	val c = color ?: FutureTheme.config.colors.primary
	IconBox(modifier = modifier, size = size, color = c) {
		Canvas(modifier = Modifier.fillMaxSize()) {
			val size = this.size.width
			val stroke = size / 24f
			val cx = size / 2f
			val cy = size / 2f
			val unit = size / 24f
			val neonBrush = Brush.radialGradient(colors = listOf(c, c.copy(alpha = 0.3f)), center = Offset(cx, cy), radius = size / 1.5f)
			drawLine(neonBrush, Offset(cx - 2.5f * unit, cy - 5 * unit), Offset(cx - 2.5f * unit, cy + 5 * unit), stroke, cap = StrokeCap.Square)
			drawLine(neonBrush, Offset(cx + 2.5f * unit, cy - 5 * unit), Offset(cx + 2.5f * unit, cy + 5 * unit), stroke, cap = StrokeCap.Square)
		}
	}
}

@Composable
fun FutureIconBlacklist(modifier: Modifier = Modifier, size: Int = 24, color: Color? = null) {
	val c = color ?: FutureTheme.config.colors.primary
	IconBox(modifier = modifier, size = size, color = c) {
		Canvas(modifier = Modifier.fillMaxSize()) {
			val size = this.size.width
			val stroke = size / 24f
			val cx = size / 2f
			val cy = size / 2f
			val unit = size / 24f
			val neonBrush = Brush.radialGradient(colors = listOf(c, c.copy(alpha = 0.3f)), center = Offset(cx, cy), radius = size / 1.5f)
			drawCircle(neonBrush, radius = 3.5f * unit, center = Offset(cx, cy - 2 * unit), style = Stroke(width = stroke))
			drawArc(
				brush = neonBrush,
				startAngle = 0f,
				sweepAngle = 180f,
				useCenter = false,
				topLeft = Offset(cx - 4 * unit, cy + 1 * unit),
				size = Size(8 * unit, 4 * unit),
				style = Stroke(width = stroke)
			)
			drawLine(neonBrush, Offset(cx - 5 * unit, cy - 5 * unit), Offset(cx + 5 * unit, cy + 5 * unit), stroke, cap = StrokeCap.Square)
		}
	}
}

@Composable
fun FutureIconPreviousSong(modifier: Modifier = Modifier, size: Int = 24, color: Color? = null) {
	val c = color ?: FutureTheme.config.colors.primary
	IconBox(modifier = modifier, size = size, color = c) {
		Canvas(modifier = Modifier.fillMaxSize()) {
			val size = this.size.width
			val stroke = size / 24f
			val cx = size / 2f
			val cy = size / 2f
			val unit = size / 24f
			val path = Path().apply {
				moveTo(cx + 2 * unit, cy - 6 * unit)
				lineTo(cx - 5 * unit, cy)
				lineTo(cx + 2 * unit, cy + 6 * unit)
				close()
			}
			val neonBrush = Brush.radialGradient(colors = listOf(c, c.copy(alpha = 0.3f)), center = Offset(cx, cy), radius = size / 1.5f)
			drawPath(path, brush = neonBrush, style = Stroke(width = stroke))
			drawLine(neonBrush, Offset(cx - 6 * unit, cy - 6 * unit), Offset(cx - 6 * unit, cy + 6 * unit), stroke, cap = StrokeCap.Square)
		}
	}
}

@Composable
fun FutureIconNextSong(modifier: Modifier = Modifier, size: Int = 24, color: Color? = null) {
	val c = color ?: FutureTheme.config.colors.primary
	IconBox(modifier = modifier, size = size, color = c) {
		Canvas(modifier = Modifier.fillMaxSize()) {
			val size = this.size.width
			val stroke = size / 24f
			val cx = size / 2f
			val cy = size / 2f
			val unit = size / 24f
			val path = Path().apply {
				moveTo(cx - 2 * unit, cy - 6 * unit)
				lineTo(cx + 5 * unit, cy)
				lineTo(cx - 2 * unit, cy + 6 * unit)
				close()
			}
			val neonBrush = Brush.radialGradient(colors = listOf(c, c.copy(alpha = 0.3f)), center = Offset(cx, cy), radius = size / 1.5f)
			drawPath(path, brush = neonBrush, style = Stroke(width = stroke))
			drawLine(neonBrush, Offset(cx + 6 * unit, cy - 6 * unit), Offset(cx + 6 * unit, cy + 6 * unit), stroke, cap = StrokeCap.Square)
		}
	}
}

@Composable
fun FutureIconSearch(modifier: Modifier = Modifier, size: Int = 24, color: Color? = null) {
	val c = color ?: FutureTheme.config.colors.primary
	IconBox(modifier = modifier, size = size, color = c) {
		Canvas(modifier = Modifier.fillMaxSize()) {
			val size = this.size.width
			val stroke = size / 24f
			val cx = size / 2f
			val cy = size / 2f
			val unit = size / 24f
			val neonBrush = Brush.radialGradient(colors = listOf(c, c.copy(alpha = 0.3f)), center = Offset(cx, cy), radius = size / 1.5f)
			drawCircle(neonBrush, radius = 5 * unit, center = Offset(cx - 1 * unit, cy - 1 * unit), style = Stroke(width = stroke))
			drawLine(neonBrush, Offset(cx + 3 * unit, cy + 3 * unit), Offset(cx + 6 * unit, cy + 6 * unit), stroke, cap = StrokeCap.Square)
		}
	}
}

@Composable
fun FutureIconAudio(modifier: Modifier = Modifier, size: Int = 24, color: Color? = null) {
	val c = color ?: FutureTheme.config.colors.primary
	IconBox(modifier = modifier, size = size, color = c) {
		Canvas(modifier = Modifier.fillMaxSize()) {
			val size = this.size.width
			val stroke = size / 24f
			val cx = size / 2f
			val cy = size / 2f
			val unit = size / 24f
			val neonBrush = Brush.radialGradient(colors = listOf(c, c.copy(alpha = 0.3f)), center = Offset(cx, cy), radius = size / 1.5f)
			val bars = listOf(-6f, -3f, 0f, 3f, 6f)
			val heights = listOf(4f, 7f, 9f, 7f, 4f)
			bars.zip(heights).forEach { (x, h) ->
				drawLine(neonBrush, Offset(cx + x * unit, cy - h / 2 * unit), Offset(cx + x * unit, cy + h / 2 * unit), stroke, cap = StrokeCap.Square)
			}
		}
	}
}

@Composable
fun FutureIconCamera(modifier: Modifier = Modifier, size: Int = 24, color: Color? = null) {
	val c = color ?: FutureTheme.config.colors.primary
	IconBox(modifier = modifier, size = size, color = c) {
		Canvas(modifier = Modifier.fillMaxSize()) {
			val size = this.size.width
			val stroke = size / 24f
			val cx = size / 2f
			val cy = size / 2f
			val unit = size / 24f
			val path = Path().apply {
				moveTo(cx - 6 * unit, cy - 2 * unit)
				lineTo(cx - 4 * unit, cy - 2 * unit)
				lineTo(cx - 2 * unit, cy - 4 * unit)
				lineTo(cx + 3 * unit, cy - 4 * unit)
				lineTo(cx + 5 * unit, cy - 2 * unit)
				lineTo(cx + 6 * unit, cy - 2 * unit)
				lineTo(cx + 6 * unit, cy + 5 * unit)
				lineTo(cx - 6 * unit, cy + 5 * unit)
				close()
			}
			val neonBrush = Brush.radialGradient(colors = listOf(c, c.copy(alpha = 0.3f)), center = Offset(cx, cy), radius = size / 1.5f)
			drawPath(path, brush = neonBrush, style = Stroke(width = stroke))
			drawCircle(neonBrush, radius = 3 * unit, center = Offset(cx, cy + 1 * unit), style = Stroke(width = stroke))
		}
	}
}

@Composable
fun FutureIconGallery(modifier: Modifier = Modifier, size: Int = 24, color: Color? = null) {
	val c = color ?: FutureTheme.config.colors.primary
	IconBox(modifier = modifier, size = size, color = c) {
		Canvas(modifier = Modifier.fillMaxSize()) {
			val size = this.size.width
			val stroke = size / 24f
			val cx = size / 2f
			val cy = size / 2f
			val unit = size / 24f
			val neonBrush = Brush.radialGradient(colors = listOf(c, c.copy(alpha = 0.3f)), center = Offset(cx, cy), radius = size / 1.5f)
			drawRect(neonBrush, topLeft = Offset(cx - 6 * unit, cy - 5 * unit), size = Size(12 * unit, 10 * unit), style = Stroke(width = stroke))
			drawLine(neonBrush, Offset(cx - 4 * unit, cy + 3 * unit), Offset(cx - 1 * unit, cy), stroke, cap = StrokeCap.Square)
			drawLine(neonBrush, Offset(cx - 1 * unit, cy), Offset(cx + 2 * unit, cy + 3 * unit), stroke, cap = StrokeCap.Square)
			drawCircle(neonBrush, radius = 1.5f * unit, center = Offset(cx + 3 * unit, cy - 2 * unit))
			drawLine(neonBrush, Offset(cx + 3 * unit, cy - 5 * unit), Offset(cx + 6 * unit, cy - 5 * unit), stroke, cap = StrokeCap.Square)
			drawLine(neonBrush, Offset(cx + 6 * unit, cy - 5 * unit), Offset(cx + 6 * unit, cy - 2 * unit), stroke, cap = StrokeCap.Square)
		}
	}
}

@Composable
fun FutureIconDraw(modifier: Modifier = Modifier, size: Int = 24, color: Color? = null) {
	val c = color ?: FutureTheme.config.colors.primary
	IconBox(modifier = modifier, size = size, color = c) {
		Canvas(modifier = Modifier.fillMaxSize()) {
			val size = this.size.width
			val stroke = size / 24f
			val cx = size / 2f
			val cy = size / 2f
			val unit = size / 24f
			val path = Path().apply {
				moveTo(cx - 6 * unit, cy + 5 * unit)
				lineTo(cx - 3 * unit, cy + 4 * unit)
				lineTo(cx + 5 * unit, cy - 4 * unit)
				lineTo(cx + 4 * unit, cy - 5 * unit)
				lineTo(cx - 4 * unit, cy + 3 * unit)
				close()
			}
			val neonBrush = Brush.radialGradient(colors = listOf(c, c.copy(alpha = 0.3f)), center = Offset(cx, cy), radius = size / 1.5f)
			drawPath(path, brush = neonBrush, style = Stroke(width = stroke))
			drawLine(neonBrush, Offset(cx + 4 * unit, cy - 5 * unit), Offset(cx + 5 * unit, cy - 4 * unit), stroke, cap = StrokeCap.Square)
		}
	}
}

@Composable
fun FutureIconText(modifier: Modifier = Modifier, size: Int = 24, color: Color? = null) {
	val c = color ?: FutureTheme.config.colors.primary
	IconBox(modifier = modifier, size = size, color = c) {
		Canvas(modifier = Modifier.fillMaxSize()) {
			val size = this.size.width
			val stroke = size / 24f
			val cx = size / 2f
			val cy = size / 2f
			val unit = size / 24f
			val neonBrush = Brush.radialGradient(colors = listOf(c, c.copy(alpha = 0.3f)), center = Offset(cx, cy), radius = size / 1.5f)
			drawLine(neonBrush, Offset(cx - 5 * unit, cy - 5 * unit), Offset(cx + 5 * unit, cy - 5 * unit), stroke, cap = StrokeCap.Square)
			drawLine(neonBrush, Offset(cx - 5 * unit, cy - 5 * unit), Offset(cx - 5 * unit, cy - 3 * unit), stroke, cap = StrokeCap.Square)
			drawLine(neonBrush, Offset(cx + 5 * unit, cy - 5 * unit), Offset(cx + 5 * unit, cy - 3 * unit), stroke, cap = StrokeCap.Square)
			drawLine(neonBrush, Offset(cx, cy - 5 * unit), Offset(cx, cy + 5 * unit), stroke, cap = StrokeCap.Square)
			drawLine(neonBrush, Offset(cx - 3 * unit, cy + 5 * unit), Offset(cx + 3 * unit, cy + 5 * unit), stroke, cap = StrokeCap.Square)
		}
	}
}

@Composable
fun FutureSvg(
	resourceId: Int,
	modifier: Modifier = Modifier,
	color: Color? = null
) {
	val config = FutureTheme.config
	val c = color ?: config.colors.primary
	Box(
		modifier = modifier
		.clip(RoundedCornerShape(4.dp))
		.shadow(8.dp, RoundedCornerShape(4.dp), clip = false, ambientColor = c.copy(alpha = 0.4f), spotColor = c.copy(alpha = 0.25f))
		.border(1.dp, Brush.sweepGradient(listOf(c.copy(alpha = 0.3f), c, c.copy(alpha = 0.3f))), RoundedCornerShape(4.dp))
		.drawBehind {
			val cornerLen = this.size.width * 0.25f
			val strokeW = 2f
			drawLine(c, Offset(0f, 0f), Offset(cornerLen, 0f), strokeW)
			drawLine(c, Offset(0f, 0f), Offset(0f, cornerLen), strokeW)
			drawLine(c, Offset(this.size.width, 0f), Offset(this.size.width - cornerLen, 0f), strokeW)
			drawLine(c, Offset(this.size.width, 0f), Offset(this.size.width, cornerLen), strokeW)
			drawLine(c, Offset(0f, this.size.height), Offset(cornerLen, this.size.height), strokeW)
			drawLine(c, Offset(0f, this.size.height), Offset(0f, this.size.height - cornerLen), strokeW)
			drawLine(c, Offset(this.size.width, this.size.height), Offset(this.size.width - cornerLen, this.size.height), strokeW)
			drawLine(c, Offset(this.size.width, this.size.height), Offset(this.size.width, this.size.height - cornerLen), strokeW)
		},
		contentAlignment = Alignment.Center
	) {
		FutureText(
			text = "SVG",
			style = FutureTheme.typography.labelSmall
		)
	}
}

@Composable
fun FutureIcon(
	resourceId: Int,
	modifier: Modifier = Modifier,
	color: Color? = null
) {
	val config = FutureTheme.config
	val c = color ?: config.colors.primary
	Box(
		modifier = modifier
		.size(24.dp)
		.clip(RoundedCornerShape(4.dp))
		.shadow(8.dp, RoundedCornerShape(4.dp), clip = false, ambientColor = c.copy(alpha = 0.4f), spotColor = c.copy(alpha = 0.25f))
		.background(
			Brush.radialGradient(
				colors = listOf(c.copy(alpha = 0.18f), Color.Black.copy(alpha = 0.8f))
			)
		),
		contentAlignment = Alignment.Center
	) {
		FutureText(
			text = "◆",
			variant = TextVariant.Glow,
			color = c.copy(alpha = 0.8f),
			style = FutureTheme.typography.labelSmall
		)
	}
}

@Composable
fun FutureIconRefresh(modifier: Modifier = Modifier, size: Int = 24, color: Color? = null) {
	val c = color ?: FutureTheme.config.colors.primary
	IconBox(modifier = modifier, size = size, color = c) {
		Canvas(modifier = Modifier.fillMaxSize()) {
			val s = this.size.width
			val stroke = s / 24f
			val cx = s / 2f
			val cy = s / 2f
			val unit = s / 24f
			val neonBrush = Brush.radialGradient(
				colors = listOf(c, c.copy(alpha = 0.3f)),
				center = Offset(cx, cy),
				radius = s / 1.5f
			)
			val r = 7f * unit
			drawArc(
				brush = neonBrush,
				startAngle = 45f,
				sweepAngle = 270f,
				useCenter = false,
				topLeft = Offset(cx - r, cy - r),
				size = Size(r * 2f, r * 2f),
				style = Stroke(width = stroke)
			)
			val endRad = 315f * PI.toFloat() / 180f
			val px = cx + r * cos(endRad)
			val py = cy + r * sin(endRad)
			val dx = -sin(endRad)
			val dy = cos(endRad)
			val headLen = 4.5f * unit
			val headHalf = 2.5f * unit
			val head = Path().apply {
				moveTo(px + dx * headLen, py + dy * headLen)
				lineTo(px - dy * headHalf, py + dx * headHalf)
				lineTo(px + dy * headHalf, py - dx * headHalf)
				close()
			}
			drawPath(head, brush = neonBrush)
		}
	}
}

@Composable
fun FutureIconForward10(modifier: Modifier = Modifier, size: Int = 24, color: Color? = null) {
	val c = color ?: FutureTheme.config.colors.primary
	IconBox(modifier = modifier, size = size, color = c) {
		Canvas(modifier = Modifier.fillMaxSize()) {
			val s = this.size.width
			val stroke = s / 24f
			val cx = s / 2f
			val cy = s / 2f
			val unit = s / 24f
			val neonBrush = Brush.radialGradient(
				colors = listOf(c, c.copy(alpha = 0.3f)),
				center = Offset(cx, cy),
				radius = s / 1.5f
			)
			val r = 7f * unit
			drawArc(
				brush = neonBrush,
				startAngle = 0f,
				sweepAngle = 270f,
				useCenter = false,
				topLeft = Offset(cx - r, cy - r),
				size = Size(r * 2f, r * 2f),
				style = Stroke(width = stroke)
			)
			val endRad = 270f * PI.toFloat() / 180f
			val px = cx + r * cos(endRad)
			val py = cy + r * sin(endRad)
			val dx = -sin(endRad)
			val dy = cos(endRad)
			val headLen = 4.5f * unit
			val headHalf = 2.5f * unit
			val head = Path().apply {
				moveTo(px + dx * headLen, py + dy * headLen)
				lineTo(px - dy * headHalf, py + dx * headHalf)
				lineTo(px + dy * headHalf, py - dx * headHalf)
				close()
			}
			drawPath(head, brush = neonBrush)
		}
	}
}

@Composable
fun FutureIconBack10(modifier: Modifier = Modifier, size: Int = 24, color: Color? = null) {
	val c = color ?: FutureTheme.config.colors.primary
	IconBox(modifier = modifier, size = size, color = c) {
		Canvas(modifier = Modifier.fillMaxSize()) {
			val s = this.size.width
			val stroke = s / 24f
			val cx = s / 2f
			val cy = s / 2f
			val unit = s / 24f
			val neonBrush = Brush.radialGradient(
				colors = listOf(c, c.copy(alpha = 0.3f)),
				center = Offset(cx, cy),
				radius = s / 1.5f
			)
			val r = 7f * unit
			drawArc(
				brush = neonBrush,
				startAngle = 180f,
				sweepAngle = -270f,
				useCenter = false,
				topLeft = Offset(cx - r, cy - r),
				size = Size(r * 2f, r * 2f),
				style = Stroke(width = stroke)
			)
			val endRad = 270f * PI.toFloat() / 180f
			val px = cx + r * cos(endRad)
			val py = cy + r * sin(endRad)
			val dx = sin(endRad)
			val dy = -cos(endRad)
			val headLen = 4.5f * unit
			val headHalf = 2.5f * unit
			val head = Path().apply {
				moveTo(px + dx * headLen, py + dy * headLen)
				lineTo(px - dy * headHalf, py + dx * headHalf)
				lineTo(px + dy * headHalf, py - dx * headHalf)
				close()
			}
			drawPath(head, brush = neonBrush)
		}
	}
}

@Composable
fun FutureIconArrowUp(modifier: Modifier = Modifier, size: Int = 24, color: Color? = null) {
	val c = color ?: FutureTheme.config.colors.primary
	IconBox(modifier = modifier, size = size, color = c) {
		Canvas(modifier = Modifier.fillMaxSize()) {
			val s = this.size.width
			val stroke = s / 24f
			val cx = s / 2f
			val cy = s / 2f
			val unit = s / 24f
			val neonBrush = Brush.radialGradient(
				colors = listOf(c, c.copy(alpha = 0.3f)),
				center = Offset(cx, cy),
				radius = s / 1.5f
			)
			drawLine(neonBrush, Offset(cx, cy + 6 * unit), Offset(cx, cy - 1 * unit), stroke, cap = StrokeCap.Square)
			drawLine(neonBrush, Offset(cx - 4 * unit, cy - 1 * unit), Offset(cx, cy - 7 * unit), stroke, cap = StrokeCap.Square)
			drawLine(neonBrush, Offset(cx + 4 * unit, cy - 1 * unit), Offset(cx, cy - 7 * unit), stroke, cap = StrokeCap.Square)
		}
	}
}

@Composable
fun FutureIconArrowDown(modifier: Modifier = Modifier, size: Int = 24, color: Color? = null) {
	val c = color ?: FutureTheme.config.colors.primary
	IconBox(modifier = modifier, size = size, color = c) {
		Canvas(modifier = Modifier.fillMaxSize()) {
			val s = this.size.width
			val stroke = s / 24f
			val cx = s / 2f
			val cy = s / 2f
			val unit = s / 24f
			val neonBrush = Brush.radialGradient(
				colors = listOf(c, c.copy(alpha = 0.3f)),
				center = Offset(cx, cy),
				radius = s / 1.5f
			)
			drawLine(neonBrush, Offset(cx, cy - 6 * unit), Offset(cx, cy + 1 * unit), stroke, cap = StrokeCap.Square)
			drawLine(neonBrush, Offset(cx - 4 * unit, cy + 1 * unit), Offset(cx, cy + 7 * unit), stroke, cap = StrokeCap.Square)
			drawLine(neonBrush, Offset(cx + 4 * unit, cy + 1 * unit), Offset(cx, cy + 7 * unit), stroke, cap = StrokeCap.Square)
		}
	}
}

@Composable
fun FutureIconArrowRight(modifier: Modifier = Modifier, size: Int = 24, color: Color? = null) {
	val c = color ?: FutureTheme.config.colors.primary
	IconBox(modifier = modifier, size = size, color = c) {
		Canvas(modifier = Modifier.fillMaxSize()) {
			val s = this.size.width
			val stroke = s / 24f
			val cx = s / 2f
			val cy = s / 2f
			val unit = s / 24f
			val neonBrush = Brush.radialGradient(
				colors = listOf(c, c.copy(alpha = 0.3f)),
				center = Offset(cx, cy),
				radius = s / 1.5f
			)
			drawLine(neonBrush, Offset(cx - 8 * unit, cy), Offset(cx - 1 * unit, cy), stroke, cap = StrokeCap.Square)
			drawLine(neonBrush, Offset(cx - 1 * unit, cy - 4 * unit), Offset(cx + 5 * unit, cy), stroke, cap = StrokeCap.Square)
			drawLine(neonBrush, Offset(cx - 1 * unit, cy + 4 * unit), Offset(cx + 5 * unit, cy), stroke, cap = StrokeCap.Square)
		}
	}
}

@Composable
fun FutureIconArrowLeft(modifier: Modifier = Modifier, size: Int = 24, color: Color? = null) {
	val c = color ?: FutureTheme.config.colors.primary
	IconBox(modifier = modifier, size = size, color = c) {
		Canvas(modifier = Modifier.fillMaxSize()) {
			val s = this.size.width
			val stroke = s / 24f
			val cx = s / 2f
			val cy = s / 2f
			val unit = s / 24f
			val neonBrush = Brush.radialGradient(
				colors = listOf(c, c.copy(alpha = 0.3f)),
				center = Offset(cx, cy),
				radius = s / 1.5f
			)
			drawLine(neonBrush, Offset(cx + 8 * unit, cy), Offset(cx + 1 * unit, cy), stroke, cap = StrokeCap.Square)
			drawLine(neonBrush, Offset(cx + 1 * unit, cy - 4 * unit), Offset(cx - 5 * unit, cy), stroke, cap = StrokeCap.Square)
			drawLine(neonBrush, Offset(cx + 1 * unit, cy + 4 * unit), Offset(cx - 5 * unit, cy), stroke, cap = StrokeCap.Square)
		}
	}
}

@Composable
fun FutureIconRepeat(modifier: Modifier = Modifier, size: Int = 24, color: Color? = null) {
	val c = color ?: FutureTheme.config.colors.primary
	IconBox(modifier = modifier, size = size, color = c) {
		Canvas(modifier = Modifier.fillMaxSize()) {
			val s = this.size.width
			val stroke = s / 24f
			val cx = s / 2f
			val cy = s / 2f
			val unit = s / 24f
			val neonBrush = Brush.radialGradient(
				colors = listOf(c, c.copy(alpha = 0.3f)),
				center = Offset(cx, cy),
				radius = s / 1.5f
			)

			val lx = cx - 8.4f * unit
			val rx = cx + 8.4f * unit
			val ty = cy - 4.8f * unit
			val by = cy + 4.8f * unit

			drawLine(neonBrush, Offset(lx, ty), Offset(rx - 3.6f * unit, ty), stroke, cap = StrokeCap.Square)
			drawLine(neonBrush, Offset(rx - 3.6f * unit, ty - 3f * unit), Offset(rx, ty), stroke, cap = StrokeCap.Square)
			drawLine(neonBrush, Offset(rx - 3.6f * unit, ty + 3f * unit), Offset(rx, ty), stroke, cap = StrokeCap.Square)
			drawLine(neonBrush, Offset(rx, ty), Offset(rx, by), stroke, cap = StrokeCap.Square)
			drawLine(neonBrush, Offset(rx, by), Offset(lx + 3.6f * unit, by), stroke, cap = StrokeCap.Square)
			drawLine(neonBrush, Offset(lx + 3.6f * unit, by - 3f * unit), Offset(lx, by), stroke, cap = StrokeCap.Square)
			drawLine(neonBrush, Offset(lx + 3.6f * unit, by + 3f * unit), Offset(lx, by), stroke, cap = StrokeCap.Square)
			drawLine(neonBrush, Offset(lx, by), Offset(lx, ty), stroke, cap = StrokeCap.Square)
		}
	}
}

@Composable
fun FutureIconRepeatSingle(modifier: Modifier = Modifier, size: Int = 24, color: Color? = null) {
	val c = color ?: FutureTheme.config.colors.primary
	IconBox(modifier = modifier, size = size, color = c) {
		Canvas(modifier = Modifier.fillMaxSize()) {
			val s = this.size.width
			val stroke = s / 24f
			val cx = s / 2f
			val cy = s / 2f
			val unit = s / 24f
			val neonBrush = Brush.radialGradient(
				colors = listOf(c, c.copy(alpha = 0.3f)),
				center = Offset(cx, cy),
				radius = s / 1.5f
			)

			val lx = cx - 8.4f * unit
			val rx = cx + 8.4f * unit
			val ty = cy - 4.8f * unit
			val by = cy + 4.8f * unit

			drawLine(neonBrush, Offset(lx, ty), Offset(rx - 3.6f * unit, ty), stroke, cap = StrokeCap.Square)
			drawLine(neonBrush, Offset(rx - 3.6f * unit, ty - 3f * unit), Offset(rx, ty), stroke, cap = StrokeCap.Square)
			drawLine(neonBrush, Offset(rx - 3.6f * unit, ty + 3f * unit), Offset(rx, ty), stroke, cap = StrokeCap.Square)
			drawLine(neonBrush, Offset(rx, ty), Offset(rx, by), stroke, cap = StrokeCap.Square)
			drawLine(neonBrush, Offset(rx, by), Offset(lx + 3.6f * unit, by), stroke, cap = StrokeCap.Square)
			drawLine(neonBrush, Offset(lx + 3.6f * unit, by - 3f * unit), Offset(lx, by), stroke, cap = StrokeCap.Square)
			drawLine(neonBrush, Offset(lx + 3.6f * unit, by + 3f * unit), Offset(lx, by), stroke, cap = StrokeCap.Square)
			drawLine(neonBrush, Offset(lx, by), Offset(lx, ty), stroke, cap = StrokeCap.Square)

			val oneStroke = stroke * 0.85f
			drawLine(neonBrush, Offset(cx - 1.62f * unit, cy - 1.62f * unit), Offset(cx, cy - 3.24f * unit), oneStroke, cap = StrokeCap.Square)
			drawLine(neonBrush, Offset(cx, cy - 3.24f * unit), Offset(cx, cy + 3.24f * unit), oneStroke, cap = StrokeCap.Square)
		}
	}
}

@Composable
fun FutureIconWhitelist(modifier: Modifier = Modifier, size: Int = 24, color: Color? = null) {
	val c = color ?: FutureTheme.config.colors.primary
	IconBox(modifier = modifier, size = size, color = c) {
		Canvas(modifier = Modifier.fillMaxSize()) {
			val s = this.size.width
			val stroke = s / 24f
			val cx = s / 2f
			val cy = s / 2f
			val unit = s / 24f
			val neonBrush = Brush.radialGradient(
				colors = listOf(c, c.copy(alpha = 0.3f)),
				center = Offset(cx, cy),
				radius = s / 1.5f
			)
			drawCircle(
				brush = neonBrush,
				radius = 3f * unit,
				center = Offset(cx - 2f * unit, cy - 3f * unit),
				style = Stroke(width = stroke)
			)
			drawArc(
				brush = neonBrush,
				startAngle = 180f,
				sweepAngle = 180f,
				useCenter = false,
				topLeft = Offset(cx - 6f * unit, cy + 1f * unit),
				size = Size(7f * unit, 5f * unit),
				style = Stroke(width = stroke)
			)
			drawLine(
				neonBrush,
				Offset(cx + 2f * unit, cy + 3f * unit),
				Offset(cx + 4f * unit, cy + 5f * unit),
				stroke,
				cap = StrokeCap.Square
			)
			drawLine(
				neonBrush,
				Offset(cx + 4f * unit, cy + 5f * unit),
				Offset(cx + 7f * unit, cy + 0f * unit),
				stroke,
				cap = StrokeCap.Square
			)
		}
	}
}

@Composable
fun FutureIconAddNote(modifier: Modifier = Modifier, size: Int = 24, color: Color? = null) {
	val c = color ?: FutureTheme.config.colors.primary
	IconBox(modifier = modifier, size = size, color = c) {
		Canvas(modifier = Modifier.fillMaxSize()) {
			val s = this.size.width
			val stroke = s / 24f
			val cx = s / 2f
			val cy = s / 2f
			val unit = s / 24f
			val neonBrush = Brush.radialGradient(
				colors = listOf(c, c.copy(alpha = 0.3f)),
				center = Offset(cx, cy),
				radius = s / 1.5f
			)
			val badgeCx = cx + 7.8f * unit
			val badgeCy = cy + 5.5f * unit
			val badgeR = 4.4f * unit
			val plusStroke = stroke * 1.6f

			drawIntoCanvas { canvas ->
				canvas.saveLayer(Rect(Offset.Zero, Size(s, s)), Paint())

				drawRect(
					brush = neonBrush,
					topLeft = Offset(cx - 5.75f * unit, cy - 6.9f * unit),
					size = Size(11.5f * unit, 13.8f * unit),
					style = Stroke(width = stroke)
				)
				drawLine(neonBrush, Offset(cx - 3.45f * unit, cy - 2.3f * unit), Offset(cx + 3.45f * unit, cy - 2.3f * unit), stroke, cap = StrokeCap.Square)
				drawLine(neonBrush, Offset(cx - 3.45f * unit, cy + 2.3f * unit), Offset(cx + 2.3f * unit, cy + 2.3f * unit), stroke, cap = StrokeCap.Square)

				drawCircle(
					color = Color.Transparent,
					radius = badgeR + stroke,
					center = Offset(badgeCx, badgeCy),
					blendMode = BlendMode.Clear
				)

				drawCircle(neonBrush, radius = badgeR, center = Offset(badgeCx, badgeCy))

				drawLine(
					color = Color.Transparent,
					start = Offset(badgeCx - 2.2f * unit, badgeCy),
					end = Offset(badgeCx + 2.2f * unit, badgeCy),
					strokeWidth = plusStroke,
					cap = StrokeCap.Square,
					blendMode = BlendMode.Clear
				)
				drawLine(
					color = Color.Transparent,
					start = Offset(badgeCx, badgeCy - 2.2f * unit),
					end = Offset(badgeCx, badgeCy + 2.2f * unit),
					strokeWidth = plusStroke,
					cap = StrokeCap.Square,
					blendMode = BlendMode.Clear
				)

				canvas.restore()
			}
		}
	}
}

@Composable
fun FutureIconAddFolder(modifier: Modifier = Modifier, size: Int = 24, color: Color? = null) {
	val c = color ?: FutureTheme.config.colors.primary
	IconBox(modifier = modifier, size = size, color = c) {
		Canvas(modifier = Modifier.fillMaxSize()) {
			val s = this.size.width
			val stroke = s / 24f
			val cx = s / 2f
			val cy = s / 2f
			val unit = s / 24f
			val neonBrush = Brush.radialGradient(
				colors = listOf(c, c.copy(alpha = 0.3f)),
				center = Offset(cx, cy),
				radius = s / 1.5f
			)
			val badgeCx = cx + 7.8f * unit
			val badgeCy = cy + 5.5f * unit
			val badgeR = 4.4f * unit
			val plusStroke = stroke * 1.6f

			drawIntoCanvas { canvas ->
				canvas.saveLayer(Rect(Offset.Zero, Size(s, s)), Paint())

				val path = Path().apply {
					moveTo(cx - 8.28f * unit, cy - 1.38f * unit)
					lineTo(cx - 4.14f * unit, cy - 1.38f * unit)
					lineTo(cx - 1.38f * unit, cy - 5.52f * unit)
					lineTo(cx + 8.28f * unit, cy - 5.52f * unit)
					lineTo(cx + 8.28f * unit, cy + 6.9f * unit)
					lineTo(cx - 8.28f * unit, cy + 6.9f * unit)
					close()
				}
				drawPath(path, brush = neonBrush, style = Stroke(width = stroke))

				drawCircle(
					color = Color.Transparent,
					radius = badgeR + stroke,
					center = Offset(badgeCx, badgeCy),
					blendMode = BlendMode.Clear
				)

				drawCircle(neonBrush, radius = badgeR, center = Offset(badgeCx, badgeCy))

				drawLine(
					color = Color.Transparent,
					start = Offset(badgeCx - 2.2f * unit, badgeCy),
					end = Offset(badgeCx + 2.2f * unit, badgeCy),
					strokeWidth = plusStroke,
					cap = StrokeCap.Square,
					blendMode = BlendMode.Clear
				)
				drawLine(
					color = Color.Transparent,
					start = Offset(badgeCx, badgeCy - 2.2f * unit),
					end = Offset(badgeCx, badgeCy + 2.2f * unit),
					strokeWidth = plusStroke,
					cap = StrokeCap.Square,
					blendMode = BlendMode.Clear
				)

				canvas.restore()
			}
		}
	}
}

@Composable
fun FutureIconSave(modifier: Modifier = Modifier, size: Int = 24, color: Color? = null) {
	val c = color ?: FutureTheme.config.colors.primary
	IconBox(modifier = modifier, size = size, color = c) {
		Canvas(modifier = Modifier.fillMaxSize()) {
			val s = this.size.width
			val stroke = s / 24f
			val cx = s / 2f
			val cy = s / 2f
			val unit = s / 24f
			val neonBrush = Brush.radialGradient(
				colors = listOf(c, c.copy(alpha = 0.3f)),
				center = Offset(cx, cy),
				radius = s / 1.5f
			)
			val body = Path().apply {
				moveTo(cx - 7 * unit, cy - 7 * unit)
				lineTo(cx + 4 * unit, cy - 7 * unit)
				lineTo(cx + 7 * unit, cy - 4 * unit)
				lineTo(cx + 7 * unit, cy + 7 * unit)
				lineTo(cx - 7 * unit, cy + 7 * unit)
				close()
			}
			drawPath(body, brush = neonBrush, style = Stroke(width = stroke))

			drawRect(
				brush = neonBrush,
				topLeft = Offset(cx - 3 * unit, cy - 6 * unit),
				size = Size(6 * unit, 3.5f * unit),
				style = Stroke(width = stroke)
			)

			drawRect(
				brush = neonBrush,
				topLeft = Offset(cx - 5 * unit, cy + 1 * unit),
				size = Size(10 * unit, 5 * unit),
				style = Stroke(width = stroke)
			)
		}
	}
}

@Composable
fun FutureIconCopy(modifier: Modifier = Modifier, size: Int = 24, color: Color? = null) {
	val c = color ?: FutureTheme.config.colors.primary
	IconBox(modifier = modifier, size = size, color = c) {
		Canvas(modifier = Modifier.fillMaxSize()) {
			val s = this.size.width
			val stroke = s / 24f
			val cx = s / 2f
			val cy = s / 2f
			val unit = s / 24f
			val neonBrush = Brush.radialGradient(
				colors = listOf(c, c.copy(alpha = 0.3f)),
				center = Offset(cx, cy),
				radius = s / 1.5f
			)
			drawRect(
				brush = neonBrush,
				topLeft = Offset(cx - 8.5f * unit, cy - 8.5f * unit),
				size = Size(11f * unit, 13.5f * unit),
				style = Stroke(width = stroke)
			)
			drawRect(
				brush = neonBrush,
				topLeft = Offset(cx - 2.5f * unit, cy - 4f * unit),
				size = Size(11f * unit, 13.5f * unit),
				style = Stroke(width = stroke)
			)
		}
	}
}

@Composable
fun FutureIconClear(modifier: Modifier = Modifier, size: Int = 24, color: Color? = null) {
	val c = color ?: FutureTheme.config.colors.primary
	IconBox(modifier = modifier, size = size, color = c) {
		Canvas(modifier = Modifier.fillMaxSize()) {
			val s = this.size.width
			val stroke = s / 24f
			val cx = s / 2f
			val cy = s / 2f
			val unit = s / 24f
			val neonBrush = Brush.radialGradient(
				colors = listOf(c, c.copy(alpha = 0.3f)),
				center = Offset(cx, cy),
				radius = s / 1.5f
			)
			drawCircle(
				brush = neonBrush,
				radius = 8.5f * unit,
				center = Offset(cx, cy),
				style = Stroke(width = stroke)
			)
			val d = 3.5f * unit
			drawLine(neonBrush, Offset(cx - d, cy - d), Offset(cx + d, cy + d), stroke, cap = StrokeCap.Square)
			drawLine(neonBrush, Offset(cx + d, cy - d), Offset(cx - d, cy + d), stroke, cap = StrokeCap.Square)
		}
	}
}
