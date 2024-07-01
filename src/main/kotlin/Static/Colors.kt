package Static

import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.graphics.Color

val appColors: Colors = Colors(
    isLight = false,  // Set to false for a dark theme
    primary = Color(0xFF50FA7B),           // Green
    onPrimary = Color(0xFF282A36),         // Background
    primaryVariant = Color(0xFFFF79C6),    // Pink
    secondaryVariant = Color(0xFFF1FA8C),  // Yellow
    background = Color(0xFF282A36),        // Background
    onBackground = Color(0xFFF8F8F2),      // Foreground
    error = Color(0xFFFF5555),             // Red
    onError = Color(0xFF282A36),           // Background
    secondary = Color(0xFF8BE9FD),         // Cyan
    onSecondary = Color(0xFF282A36),       // Background
    surface = Color(0xFF44475A),           // Current Line
    onSurface = Color(0xFFF8F8F2)          // Foreground
)