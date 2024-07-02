package Static

import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.graphics.Color

val appColors: Colors = Colors(
    isLight = false,  // Set to false for a dark theme
    primary = Color(0xFF50FA7B),           // Green
    onPrimary = Color(0xFF282A36),         // Background
    primaryVariant = Color(0xFFF4CE14),    // Yellow
    secondaryVariant = Color(0xFFF1FA8C),  // Yellow
    background = Color(0xFF282A36),        // Background
    onBackground = Color(0xFFF8F8F2),      // Foreground
    error = Color(0xFFFF5555),             // Red
    onError = Color(0xFF282A36),           // Background
    secondary = Color(0xFFF8F8F2),         // Green
    onSecondary = Color(0xFF282A36),       // Background
    surface = Color(0xFF44475A),           // Current Line
    onSurface = Color(0xFFF8F8F2)          // Foreground
)