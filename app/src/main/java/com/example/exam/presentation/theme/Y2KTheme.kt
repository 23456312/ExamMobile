package com.example.exam.presentation.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val NeonPink = Color(0xFFFF3EB5)
private val HotPurple = Color(0xFFB026FF)
private val CyberBlue = Color(0xFF4DDCFF)
private val AcidGreen = Color(0xFFB6FF3E)
private val SoftBlack = Color(0xFF121212)
private val DeepBlack = Color(0xFF000000)
private val BarbiePink = Color(0xFFFF78C4)
private val PastelPurple = Color(0xFFE5CCFF)
private val emoGray = Color(0xFF2A2A2A)

private val DarkY2KColorScheme = darkColorScheme(
    primary = NeonPink,
    onPrimary = Color.Black,
    primaryContainer = SoftBlack,
    onPrimaryContainer = NeonPink,
    secondary = HotPurple,
    tertiary = CyberBlue,
    background = DeepBlack,
    onBackground = Color.White,
    surface = emoGray,
    onSurface = Color.White,
    surfaceVariant = Color(0xFF1E1E1E),
    onSurfaceVariant = BarbiePink
)

