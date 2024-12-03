package edg.android.rciva.ui.app.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun CardItem(
    value: Float,
    description: String? = null,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            text = String.format("%.2f Bs.", value),
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.sp
            ),
            modifier = Modifier,
            color = MaterialTheme.colorScheme.onSurface,
            fontFamily = MaterialTheme.typography.bodySmall.fontFamily
        )
        if (description != null) {
            Text(
                text = description,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Light,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(
                        alpha = 0.5f
                    ),
                    lineHeight = 16.sp
                ),
                modifier = Modifier,
                fontFamily = MaterialTheme.typography.bodySmall.fontFamily
            )
        }
    }
}