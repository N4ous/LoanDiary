package com.ajshahariar.loandiary.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

data class CustomThemePalette(
    val name: String,
    val primary: Color,
    val background: Color,
    val surface: Color,
    val card: Color,
    val titleText: Color,
    val bodyText: Color,
    val hintText: Color,
    val isDark: Boolean
)

val LoanDiaryPresetThemes = listOf(
    CustomThemePalette("Classic Emerald", Color(0xFF2E7D32), Color(0xFFF1F8E9), Color(0xFFFFFFFF), Color(0xFFE8F5E9), Color(0xFF1B5E20), Color(0xFF333333), Color(0xFF757575), false),
    CustomThemePalette("Royal Sapphire", Color(0xFF1565C0), Color(0xFFE3F2FD), Color(0xFFFFFFFF), Color(0xFFBBDEFB), Color(0xFF0D47A1), Color(0xFF212121), Color(0xFF666666), false),
    CustomThemePalette("Amethyst Plum", Color(0xFF7B1FA2), Color(0xFFF3E5F5), Color(0xFFFFFFFF), Color(0xFFE1BEE7), Color(0xFF4A148C), Color(0xFF1F1F1F), Color(0xFF787878), false),
    CustomThemePalette("Crimson Velvet", Color(0xFFC62828), Color(0xFFFFEBEE), Color(0xFFFFFFFF), Color(0xFFFFCDD2), Color(0xFFB71C1C), Color(0xFF262626), Color(0xFF7F7F7F), false),
    CustomThemePalette("Sunset Amber", Color(0xFFE65100), Color(0xFFFFF3E0), Color(0xFFFFFFFF), Color(0xFFFFE0B2), Color(0xFF5D4037), Color(0xFF2B2B2B), Color(0xFF8D6E63), false),
    CustomThemePalette("Ocean Teal", Color(0xFF00695C), Color(0xFFE0F2F1), Color(0xFFFFFFFF), Color(0xFFB2DFDB), Color(0xFF004D40), Color(0xFF222222), Color(0xFF607D8B), false),
    CustomThemePalette("Charcoal Dark", Color(0xFFBB86FC), Color(0xFF121212), Color(0xFF1E1E1E), Color(0xFF2D2D2D), Color(0xFFFFFFFF), Color(0xFFE0E0E0), Color(0xFF888888), true),
    CustomThemePalette("Midnight Forest", Color(0xFF81C784), Color(0xFF1B2E1E), Color(0xFF243D28), Color(0xFF2C4C32), Color(0xFFE8F5E9), Color(0xFFC8E6C9), Color(0xFF94B296), true),
    CustomThemePalette("Slate Minimal", Color(0xFF455A64), Color(0xFFECEFF1), Color(0xFFFFFFFF), Color(0xFFCFD8DC), Color(0xFF263238), Color(0xFF37474F), Color(0xFF78909C), false),
    CustomThemePalette("Luxury Gold", Color(0xFFD4AF37), Color(0xFF1A1A1A), Color(0xFF262626), Color(0xFF333333), Color(0xFFFFFFFF), Color(0xFFF5F5F5), Color(0xFFA6A6A6), true)
)

val LocalCustomColors = staticCompositionLocalOf { LoanDiaryPresetThemes[0] }

@Composable
fun LoanDiaryTheme(
    themeIndex: Int = 0,
    content: @Composable () -> Unit
) {
    val preset = if (themeIndex in LoanDiaryPresetThemes.indices) LoanDiaryPresetThemes[themeIndex] else LoanDiaryPresetThemes[0]
    
    val colorScheme = if (preset.isDark) {
        darkColorScheme(
            primary = preset.primary,
            background = preset.background,
            surface = preset.surface,
            surfaceVariant = preset.card
        )
    } else {
        lightColorScheme(
            primary = preset.primary,
            background = preset.background,
            surface = preset.surface,
            surfaceVariant = preset.card
        )
    }

    CompositionLocalProvider(LocalCustomColors provides preset) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}
