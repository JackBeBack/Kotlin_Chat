package Static

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import com.mikepenz.markdown.model.MarkdownTypography

val scale = 2

class AppTypography(): MarkdownTypography {
    override val bullet: TextStyle
        get() = TextStyle()
    override val code: TextStyle
        get() = TextStyle()
    override val h1: TextStyle
        get() = TextStyle(fontSize = TextUnit(32f* scale, TextUnitType.Sp))
    override val h2: TextStyle
        get() = TextStyle(fontSize = TextUnit(28f* scale, TextUnitType.Sp))
    override val h3: TextStyle
        get() = TextStyle(fontSize = TextUnit(24f* scale, TextUnitType.Sp))
    override val h4: TextStyle
        get() = TextStyle(fontSize = TextUnit(20f* scale, TextUnitType.Sp))
    override val h5: TextStyle
        get() = TextStyle(fontSize = TextUnit(16f* scale, TextUnitType.Sp))
    override val h6: TextStyle
        get() = TextStyle(fontSize = TextUnit(12f* scale, TextUnitType.Sp))
    override val list: TextStyle
        get() = TextStyle()
    override val ordered: TextStyle
        get() = TextStyle()
    override val paragraph: TextStyle
        get() = TextStyle()
    override val quote: TextStyle
        get() = TextStyle()
    override val text: TextStyle
        get() = TextStyle()
}