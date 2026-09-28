package com.mlib.future

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.remember
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.dp
import android.os.Build

enum class BackgroundAnimationType { NONE, RADIAL_PULSE, CYBER_GRID, SCANLINE_FLOW, HEX_PATTERN }

data class FutureConfig(
	val colors: FutureColors = FutureColors(),
	val fonts: FutureFonts = FutureFonts(),
	val typography: FutureTypography = FutureTypography(),
	val animation: FutureAnimation = FutureAnimation(),
	val shapes: FutureShapes = FutureShapes(),
	val glassMode: Boolean = false,
	val backgroundAnimation: BackgroundAnimationType = BackgroundAnimationType.NONE
) {
	companion object {
		val Default: FutureConfig = FutureConfig()

		val MasemLight: FutureConfig = FutureConfig(colors = FutureColors.MasemLight)
		val MasemDark: FutureConfig = FutureConfig(colors = FutureColors.MasemDark)

		val AuroraLight = FutureConfig(colors = FutureColors.AuroraLight)
		val AuroraDark  = FutureConfig(colors = FutureColors.AuroraDark)

		val JuleeLight = FutureConfig(colors = FutureColors.JuleeLight)
		val JuleeDark  = FutureConfig(colors = FutureColors.JuleeDark)

		val AutumnLight = FutureConfig(colors = FutureColors.AutumnLight)
		val AutumnDark  = FutureConfig(colors = FutureColors.AutumnDark)

		val SunriseLight = FutureConfig(colors = FutureColors.SunriseLight)
		val SunriseDark  = FutureConfig(colors = FutureColors.SunriseDark)

		val SunsetLight = FutureConfig(colors = FutureColors.SunsetLight)
		val SunsetDark  = FutureConfig(colors = FutureColors.SunsetDark)

		val PaintingLight = FutureConfig(colors = FutureColors.PaintingLight)
		val PaintingDark  = FutureConfig(colors = FutureColors.PaintingDark)

		val CharcoalLight = FutureConfig(colors = FutureColors.CharcoalLight)
		val CharcoalDark  = FutureConfig(colors = FutureColors.CharcoalDark)

		val CelesteLight = FutureConfig(colors = FutureColors.CelesteLight)
		val CelesteDark  = FutureConfig(colors = FutureColors.CelesteDark)
	}
}

private val LocalFutureConfig = staticCompositionLocalOf { FutureConfig.Default }

fun hexColor(hex: String): Color {
	val sanitized = hex.removePrefix("#")
	return Color(("FF$sanitized").toLong(16))
}

data class FutureColors(
	val primary: Color = hexColor("#00F3FF"),
	val globalBg: Color = hexColor("#050508"),
	val componentBg: Color = hexColor("#0D0D14"),

	val success: Color = hexColor("#00FF9D"),
	val error: Color = hexColor("#FF2A6D"),
	val warning: Color = hexColor("#FF9D00"),

	val textPrimary: Color = hexColor("#F0F0FF"),
	val textMuted: Color = hexColor("#8080A0"),

	val accent: Color? = null,
	val accentSecondary: Color? = null
) {
	companion object {

		val MasemLight = FutureColors(
			primary = hexColor("#0077FF"),
			globalBg = hexColor("#FFFFFF"),
			componentBg = hexColor("#F9F9F9"),
			textPrimary = hexColor("#000000"),
			textMuted = hexColor("#474759"),
			accent = hexColor("#00C2FF"),
			accentSecondary = hexColor("#7B2FFF"),
		)
		val MasemDark = FutureColors(
			primary = hexColor("#0077FF"),
			globalBg = hexColor("#050508"),
			componentBg = hexColor("#0D0D14"),
			textPrimary = hexColor("#F0F0FF"),
			textMuted = hexColor("#8080A0"),
			accent = hexColor("#00E5FF"),
			accentSecondary = hexColor("#7B2FFF"),
		)

		val AuroraLight = FutureColors(
			primary = hexColor("#00524D"),
			globalBg = hexColor("#FFFFFF"),
			componentBg = hexColor("#EFFFF4"),
			textPrimary = hexColor("#022A3B"),
			textMuted = hexColor("#00524D"),
			accent = hexColor("#02C397"),
			accentSecondary = hexColor("#7C5CFF"),
		)
		val AuroraDark = FutureColors(
			primary = hexColor("#02C397"),
			globalBg = hexColor("#011C27"),
			componentBg = hexColor("#012522"),
			textPrimary = hexColor("#CEBFEB"),
			textMuted = hexColor("#48A89A"),
			accent = hexColor("#7CF5D3"),
			accentSecondary = hexColor("#7C5CFF"),
		)

		val JuleeLight = FutureColors(
			primary = hexColor("#69A0D0"),
			globalBg = hexColor("#D5DCE3"),
			componentBg = hexColor("#BBCBD9"),
			textPrimary = hexColor("#02030A"),
			textMuted = hexColor("#111A22"),
			accent = hexColor("#9D7BE8"),
			accentSecondary = hexColor("#5CC8E8"),
		)
		val JuleeDark = FutureColors(
			primary = hexColor("#69A0D0"),
			globalBg = hexColor("#060A21"),
			componentBg = hexColor("#0E182E"),
			textPrimary = hexColor("#BDC6CD"),
			textMuted = hexColor("#9FA7AD"),
			accent = hexColor("#9D7BE8"),
			accentSecondary = hexColor("#5CC8E8"),
		)

		val AutumnLight = FutureColors(
			primary = hexColor("#A44916"),
			globalBg = hexColor("#FDF8F3"), componentBg = hexColor("#F5E8D8"),
			textPrimary = hexColor("#2D0E09"), textMuted = hexColor("#75280F"),
			accent = hexColor("#FFC24B"),
			accentSecondary = hexColor("#DE621E"),
		)
		val AutumnDark = FutureColors(
			primary = hexColor("#DE621E"),
			success = hexColor("#4D1A0D"), error = hexColor("#FF5555"), warning = hexColor("#0F0506"),
			globalBg = hexColor("#0F0506"), componentBg = hexColor("#2D0E09"),
			textPrimary = hexColor("#F5E8D8"), textMuted = hexColor("#B43E17"),
			accent = hexColor("#FFC24B"),
			accentSecondary = hexColor("#F5763B"),
		)

		val SunsetLight = FutureColors(
			primary = hexColor("#FBB825"),
			success = hexColor("#FBE8C9"), error = hexColor("#DBBD7E"), warning = hexColor("#B23409"),
			globalBg = hexColor("#FFF8E1"), componentBg = hexColor("#FFF5D3"),
			textPrimary = hexColor("#B23409"), textMuted = hexColor("#DBBD7E"),
			accent = hexColor("#FF6B4A"),
			accentSecondary = hexColor("#FDE27A"),
		)
		val SunsetDark = FutureColors(
			primary = hexColor("#FBB825"),
			success = hexColor("#FBE8C9"), error = hexColor("#FF6B4A"), warning = hexColor("#B23409"),
			globalBg = hexColor("#1A120B"), componentBg = hexColor("#2A1F14"),
			textPrimary = hexColor("#FFF0C2"), textMuted = hexColor("#FBCF67"),
			accent = hexColor("#FF6B4A"),
			accentSecondary = hexColor("#FDE27A"),
		)

		val PaintingLight = FutureColors(
			primary = hexColor("#DC4814"),
			globalBg = hexColor("#F7F2FF"),
			componentBg = hexColor("#DAD8FF"),
			textPrimary = hexColor("#060505"),
			textMuted = hexColor("#551009"),
			accent = hexColor("#8A5CFF"),
			accentSecondary = hexColor("#E8A33D"),
		)
		val PaintingDark = FutureColors(
			primary = hexColor("#DC4814"),
			globalBg = hexColor("#0D0B1D"),
			componentBg = hexColor("#14102B"),
			textPrimary = hexColor("#F5EDE4"),
			textMuted = hexColor("#AE8C6F"),
			accent = hexColor("#8A5CFF"),
			accentSecondary = hexColor("#E8A33D"),
		)

		val SunriseLight = FutureColors(
			primary = hexColor("#FC7174"),
			success = hexColor("#FC8862"), error = hexColor("#FDB863"), warning = hexColor("#FC7174"),
			globalBg = hexColor("#FFF5F0"), componentBg = hexColor("#FFE5DC"),
			textPrimary = hexColor("#2D1A15"), textMuted = hexColor("#EB6859"),
			accent = hexColor("#FFB86B"),
			accentSecondary = hexColor("#B56BFF"),
		)
		val SunriseDark = FutureColors(
			primary = hexColor("#FC7174"),
			success = hexColor("#FC8862"), error = hexColor("#FF5252"), warning = hexColor("#FDB863"),
			globalBg = hexColor("#1A0F0C"), componentBg = hexColor("#2A1A15"),
			textPrimary = hexColor("#FFF0E8"), textMuted = hexColor("#FC8862"),
			accent = hexColor("#FFB86B"),
			accentSecondary = hexColor("#B56BFF"),
		)

		val CharcoalLight = FutureColors(
			primary = hexColor("#393939"),
			globalBg = hexColor("#F7F7F7"),
			componentBg = hexColor("#EFEFEF"),
			textPrimary = hexColor("#101010"),
			textMuted = hexColor("#696969"),
			accent = hexColor("#0077FF"),
			accentSecondary = hexColor("#00A2B4"),
		)
		val CharcoalDark = FutureColors(
			primary = hexColor("#DFDFDF"),
			globalBg = hexColor("#000000"),
			componentBg = hexColor("#1E1E1E"),
			textPrimary = hexColor("#DFDFDF"),
			textMuted = hexColor("#8E8E8E"),
			accent = hexColor("#00F3FF"),
			accentSecondary = hexColor("#7B7BFF"),
		)

		val CelesteLight = FutureColors(
			primary = hexColor("#A4BACC"),
			globalBg = hexColor("#F5F8FA"),
			componentBg = hexColor("#E7EEF5"),
			textPrimary = hexColor("#2D3748"),
			textMuted = hexColor("#6B7280"),
			accent = hexColor("#5C8FD9"),
			accentSecondary = hexColor("#8FD0C2"),
		)
		val CelesteDark = FutureColors(
			primary = hexColor("#AFC6D9"),
			globalBg = hexColor("#0B1015"),
			componentBg = hexColor("#141C24"),
			textPrimary = hexColor("#E2E8F0"),
			textMuted = hexColor("#8FA0B2"),
			accent = hexColor("#7FB3E8"),
			accentSecondary = hexColor("#7CD9C3"),
		)
	}
}

private val RegularFontFamily = FontFamily(
	Font(R.font.valera_round, FontWeight.Normal),
	Font(R.font.valera_round,  FontWeight.Medium),
	Font(R.font.valera_round,    FontWeight.Bold)
)

private val HighlightFontFamily = FontFamily(
	Font(R.font.valera_round, FontWeight.Normal),
	Font(R.font.valera_round,  FontWeight.Medium),
	Font(R.font.valera_round,    FontWeight.Bold)
)

private val TitleFontFamily = FontFamily(
	Font(R.font.aicon_regular, FontWeight.Normal),
	Font(R.font.aicon_medium, FontWeight.Medium),
	Font(R.font.aicon_bold, FontWeight.Bold)
)

data class FutureFonts(
	val regular: FontFamily = RegularFontFamily,
	val title: FontFamily = TitleFontFamily,
	val highlight: FontFamily = HighlightFontFamily,
)

data class FutureTypography(
	val displayLarge: TextStyle = TextStyle(fontSize = 57.sp, lineHeight = 64.sp, fontWeight = FontWeight.Normal),
	val displayMedium: TextStyle = TextStyle(fontSize = 45.sp, lineHeight = 52.sp, fontWeight = FontWeight.Normal),
	val headlineLarge: TextStyle = TextStyle(fontSize = 32.sp, lineHeight = 40.sp, fontWeight = FontWeight.SemiBold),
	val headlineMedium: TextStyle = TextStyle(fontSize = 28.sp, lineHeight = 36.sp, fontWeight = FontWeight.SemiBold),
	val titleLarge: TextStyle = TextStyle(fontSize = 22.sp, lineHeight = 28.sp, fontWeight = FontWeight.SemiBold),
	val titleMedium: TextStyle = TextStyle(fontSize = 16.sp, lineHeight = 24.sp, fontWeight = FontWeight.Medium, letterSpacing = 0.15.sp),
	val bodyLarge: TextStyle = TextStyle(fontSize = 16.sp, lineHeight = 24.sp, fontWeight = FontWeight.Normal, letterSpacing = 0.5.sp),
	val bodyMedium: TextStyle = TextStyle(fontSize = 14.sp, lineHeight = 20.sp, fontWeight = FontWeight.Normal, letterSpacing = 0.25.sp),
	val labelLarge: TextStyle = TextStyle(fontSize = 14.sp, lineHeight = 20.sp, fontWeight = FontWeight.Medium, letterSpacing = 0.1.sp),
	val labelMedium: TextStyle = TextStyle(fontSize = 12.sp, lineHeight = 16.sp, fontWeight = FontWeight.Medium, letterSpacing = 0.5.sp),
	val labelSmall: TextStyle = TextStyle(fontSize = 11.sp, lineHeight = 16.sp, fontWeight = FontWeight.Medium, letterSpacing = 0.5.sp)
)

data class FutureAnimation(
	val speedMultiplier: Float = 2.0f,
	val defaultDuration: Int = 300,
	val flowDuration: Int = 500,
	val slowDuration: Int = 800,
	val useSprings: Boolean = false,
	val springDamping: Float = 1.0f,
	val springStiffness: Float = 300f
) {
	val defaultMs: Int get() = (defaultDuration / speedMultiplier).toInt().coerceAtLeast(0)
	val flowMs: Int get() = (flowDuration / speedMultiplier).toInt().coerceAtLeast(0)
	val slowMs: Int get() = (slowDuration / speedMultiplier).toInt().coerceAtLeast(0)
}

data class FutureShapes(
	val button: RoundedCornerShape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp, bottomStart = 8.dp, bottomEnd = 8.dp),
	val buttonSmall: RoundedCornerShape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp, bottomStart = 6.dp, bottomEnd = 6.dp),
	val card: RoundedCornerShape = RoundedCornerShape(12.dp),
	val input: RoundedCornerShape = RoundedCornerShape(8.dp),
	val chip: RoundedCornerShape = RoundedCornerShape(4.dp),
	val avatar: RoundedCornerShape = RoundedCornerShape(10.dp),
	val dialog: RoundedCornerShape = RoundedCornerShape(16.dp),
	val drawer: RoundedCornerShape = RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp),
	val nav: RoundedCornerShape = RoundedCornerShape(14.dp)
)

object FutureTheme {
	val config: FutureConfig
	@Composable @ReadOnlyComposable get() = LocalFutureConfig.current

	val colors: FutureColors
	@Composable @ReadOnlyComposable get() = config.colors

	val fonts: FutureFonts
	@Composable @ReadOnlyComposable get() = config.fonts

	val typography: FutureTypography
	@Composable @ReadOnlyComposable get() = config.typography

	val shapes: FutureShapes
	@Composable @ReadOnlyComposable get() = config.shapes

	val animation: FutureAnimation
	@Composable @ReadOnlyComposable get() = config.animation
}

@Composable
fun FutureTheme(config: FutureConfig = FutureConfig.Default, content: @Composable () -> Unit) {
	CompositionLocalProvider(LocalFutureConfig provides config, content = content)
}

@Composable
fun rememberSystemFutureConfig(): FutureConfig {
	val context = LocalContext.current
	val darkTheme = isSystemInDarkTheme()
	return remember(darkTheme) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
			val dynamic = if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
			FutureConfig(
				colors = FutureColors(
					primary = Color(dynamic.primary.toArgb()),
					globalBg = Color(dynamic.background.toArgb()),
					componentBg = Color(dynamic.surfaceContainer.toArgb()),
					textPrimary = Color(dynamic.onBackground.toArgb()),
					textMuted = Color(dynamic.onSurfaceVariant.toArgb()),
					success = hexColor("#00FF9D"),
					error = Color(dynamic.error.toArgb()),
					warning = hexColor("#FF9D00")
				)
			)
		} else {
			if (darkTheme) FutureConfig.MasemDark else FutureConfig.MasemLight
		}
	}
}
