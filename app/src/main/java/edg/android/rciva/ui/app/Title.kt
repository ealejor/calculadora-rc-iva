package edg.android.rciva.ui.app

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun Title(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                Color(
                    alpha = 1f,
                    red = 52f / 255f,
                    green = 152f / 255f,
                    blue = 219f / 255f
                )
            )
            .padding(top = 18.dp, bottom = 18.dp)
    ) {
        Text(
            text = "Calculadora Tributaria RC IVA",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .align(Alignment.Center),
            color = Color.White
        )
    }
}
