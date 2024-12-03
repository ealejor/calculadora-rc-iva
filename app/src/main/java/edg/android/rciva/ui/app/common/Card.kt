package edg.android.rciva.ui.app.common

import android.content.res.Resources
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex

@Composable
fun Card(
    modifier: Modifier = Modifier,
    strokeSize: Dp = 1.dp,
    strokeColor: Color = MaterialTheme.colorScheme.primary,
    fillColor: Color = MaterialTheme.colorScheme.surface,
    roundedCornerShape: Shape = RoundedCornerShape(5.dp),
    paddingStartBadge: Dp = 16.dp,
    paddingValues: PaddingValues = PaddingValues(),
    badge: @Composable () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    val density = Resources.getSystem().displayMetrics.density
    var heightBadge by remember {
        mutableStateOf(0.dp)
    }
    var widthBadge by remember {
        mutableStateOf(0.dp)
    }
    Box(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = (heightBadge / 2))
        ) {
            // Content
            Box(
                modifier = Modifier
                    .zIndex(0f)
                    .fillMaxSize()
                    .wrapContentHeight()
                    .border(
                        strokeSize, strokeColor.copy(
                            alpha = 0.3f
                        ), roundedCornerShape
                    )
                    .background(fillColor)
            ) {
                Column(
                    modifier = Modifier
                        .padding(
                            PaddingValues(
                                top = paddingValues.calculateTopPadding(),/* + heightBadge*/
                                bottom = paddingValues.calculateBottomPadding(),
                                start = paddingValues.calculateStartPadding(LayoutDirection.Ltr),
                                end = paddingValues.calculateEndPadding(LayoutDirection.Ltr),
                            )
                        )
                ) {
                    content()
                }
            }

            // Badge
            Box(
                modifier = Modifier
                    .zIndex(2f)
                    .offset(
                        x = paddingStartBadge,
                        y = (-heightBadge)
                    )
                    .onGloballyPositioned {
                        val middle = it.size.height / 2
                        heightBadge = (middle / density).dp
                        widthBadge = (it.size.width / density).dp
                    }
            ) {
                Row(
                    modifier = Modifier
                        .horizontalScroll(rememberScrollState())
                ) {
                    badge()
                }
            }
            // Line
            Box(
                modifier = Modifier
                    .zIndex(1f)
                    .offset(x = paddingStartBadge, y = 0.dp)
                    .width(widthBadge)
                    .height(strokeSize)
                    .background(MaterialTheme.colorScheme.surface)
            )
        }
    }
}
