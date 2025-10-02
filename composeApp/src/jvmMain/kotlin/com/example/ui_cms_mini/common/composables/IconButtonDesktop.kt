import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun IconButtonDesktop(
    resource: DrawableResource,
    iconColor: Color = Color.Black,
    text: String? = null,
    textColor: Color = Color.Black,
    rippleRounded: Dp = 32.dp,
    expand: Boolean = false,
    iconSize: Dp = 20.dp,
    onClick: () -> Unit
) {
    val shape = when (rippleRounded) {
        0.dp -> RoundedCornerShape(0.dp)
        50.dp -> CircleShape
        else -> RoundedCornerShape(rippleRounded)
    }

    val interactionSource = remember { MutableInteractionSource() }

    Column(
        modifier = Modifier
            .clip(shape)
            .clickable(
                interactionSource = interactionSource,
                indication = LocalIndication.current,
                onClick = onClick
            )
            .padding(6.dp)
            .then(
                if (expand) Modifier.fillMaxWidth() else Modifier
            ),
        verticalArrangement = Arrangement.spacedBy(2.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(resource),
            contentDescription = null,
            colorFilter = ColorFilter.tint(iconColor),
            modifier = Modifier
                .height(iconSize)
                .width(iconSize)
                .clip(RoundedCornerShape(16.dp))
        )
        if (text != null) {
            Text(
                text = text,
                style = MaterialTheme.typography.bodySmall.copy(color = textColor, fontSize = 10.sp)
            )
        }
    }
}
