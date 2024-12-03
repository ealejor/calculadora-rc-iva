package edg.android.rciva

import android.content.res.Resources
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.PopupProperties
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import edg.android.rciva.ui.theme.AppTheme
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {
                Calculator(
                    logo = { Logo() },
                    title = { Title() },
                    spacing = 16.dp,
                    padding = PaddingValues(16.dp)
                ) { viewModel ->
                    Subtitle(
                        text = "Salario mínimo nacional"
                    )
                    ComboBox(
                        placeHolder = "seleccionar",
                        wages = viewModel.nationalMinimumWages,
                        onIndexChange = { index ->
                            viewModel.updateSmnIndex(index)
                        },
                        selectedIndex = viewModel.index
                    )
                    Subtitle(
                        text = "Colocar el total de ingresos"
                    )
                    Input(
                        hint = "Haber básico + bono de antigüedad + otros",
                        onValueChange = { input, isError ->
                            if (!isError) {
                                viewModel.updateEarnedSalary(input)
                            }
                        }
                    )
                    Card(
                        modifier = Modifier
                            .fillMaxWidth(),
                        roundedCornerShape = RoundedCornerShape(8.dp),
                        badge = {
                            Text(
                                text = " Aporte de AFPs (12.71%) ",
                                style = TextStyle(
                                    fontWeight = FontWeight.Light,
                                    fontSize = 18.sp,
                                    letterSpacing = 0.sp,
                                ),
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier
                            )
                        },
                        paddingStartBadge = 16.dp,
                        strokeSize = 1.dp,
                        paddingValues = PaddingValues(16.dp),
                    ) {
                        CardItem(value = viewModel.afpsContribution, description = "a restar")
                    }
                    Card(
                        modifier = Modifier
                            .fillMaxWidth(),
                        roundedCornerShape = RoundedCornerShape(8.dp),
                        badge = {
                            Text(
                                text = " Aporte nacional solidario ",
                                style = TextStyle(
                                    fontWeight = FontWeight.Light,
                                    fontSize = 18.sp,
                                    letterSpacing = 0.sp,
                                ),
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier
                            )
                        },
                        paddingStartBadge = 16.dp,
                        strokeSize = 1.dp,
                        paddingValues = PaddingValues(16.dp),
                    ) {
                        CardItem(
                            value = viewModel.nationalSolidarityContribution,
                            description = "a restar (si gana mayor a 13mil)"
                        )
                    }
                    Card(
                        modifier = Modifier
                            .fillMaxWidth(),
                        roundedCornerShape = RoundedCornerShape(8.dp),
                        badge = {
                            Text(
                                text = " Salario neto ",
                                style = TextStyle(
                                    fontWeight = FontWeight.Light,
                                    fontSize = 18.sp,
                                    letterSpacing = 0.sp,
                                ),
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier
                            )
                        },
                        paddingStartBadge = 16.dp,
                        strokeSize = 1.dp,
                        paddingValues = PaddingValues(16.dp),
                    ) {
                        CardItem(
                            value = viewModel.netSalary,
                            description = "base para el cálculo de RC IVA"
                        )
                    }
                    Card(
                        modifier = Modifier
                            .fillMaxWidth(),
                        roundedCornerShape = RoundedCornerShape(8.dp),
                        badge = {
                            Text(
                                text = " RC IVA a pagar ",
                                style = TextStyle(
                                    fontWeight = FontWeight.Light,
                                    fontSize = 18.sp,
                                    letterSpacing = 0.sp,
                                ),
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier
                            )
                        },
                        paddingStartBadge = 16.dp,
                        strokeSize = 1.dp,
                        paddingValues = PaddingValues(16.dp),
                    ) {
                        CardItem(value = viewModel.rciva)
                    }
                }
            }
        }
    }
}

@Composable
fun Calculator(
    logo: @Composable () -> Unit,
    title: @Composable () -> Unit,
    spacing: Dp = 16.dp,
    padding: PaddingValues,
    viewModel: RCIVAViewModel = viewModel(),
    content: @Composable ColumnScope.(RCIVAViewModel) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                //.systemBarsPadding()
                .windowInsetsPadding(WindowInsets.systemBars)
                .verticalScroll(state = rememberScrollState())
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                logo()
                title()
                Column(
                    modifier = Modifier
                        .sizeIn(maxWidth = 450.dp)
                        .padding(padding),
                    verticalArrangement = Arrangement.spacedBy(spacing)
                ) {
                    content(viewModel)
                }
            }
        }
    }
}

@Composable
fun Logo() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(top = 16.dp, bottom = 16.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_impuestos_bolivia),
            contentDescription = "Logo",
            modifier = Modifier
                .align(Alignment.Center)
                .height(35.dp)
                .aspectRatio(6f)
        )
    }
}

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

@Composable
fun Subtitle(
    modifier: Modifier = Modifier,
    text: String,
) {
    Box(modifier = modifier) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Medium,
                fontSize = 18.sp,
                letterSpacing = 0.sp,
            ),
            color = MaterialTheme.colorScheme.onSurface,
            fontFamily = MaterialTheme.typography.bodySmall.fontFamily
        )
    }
}

@Composable
fun ComboBox(
    placeHolder: String,
    selectedIndex: Int,
    onIndexChange: (index: Int) -> Unit,
    wages: List<Float>,
) {
    val density = LocalDensity.current.density

    var isDropDownExpanded by remember {
        mutableStateOf(false)
    }

    var width by remember { mutableStateOf(0.dp) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .measure(density) { dpSize ->
                width = dpSize.width
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clickable {
                        isDropDownExpanded = true
                    }
                    .fillMaxWidth()
                    .height(56.dp)
                    .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                    .background(
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                    )
            ) {
                Text(
                    text = if (selectedIndex == -1) {
                        placeHolder
                    } else {
                        String.format(Locale.ENGLISH, "%.2f", wages[selectedIndex])
                    },
                    modifier = Modifier
                        .padding(start = 16.dp),
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = if (selectedIndex != -1) {
                            MaterialTheme.colorScheme.onSurface
                        } else {
                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        },
                        fontWeight = if (selectedIndex != -1) {
                            FontWeight.Bold
                        } else {
                            FontWeight.Light
                        },
                        letterSpacing = 0.sp,
                    )
                )
                val rotation by animateFloatAsState(
                    targetValue = if (isDropDownExpanded) 180f else 0f,
                    animationSpec = tween(500),
                    label = ""
                )

                Image(
                    painter = painterResource(id = R.drawable.arw),
                    contentDescription = "DropDown Icon",
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .width(14.dp)
                        .rotate(rotation),
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
                    alignment = Alignment.Center
                )

            }
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.2.dp)
                    .background(
                        MaterialTheme.colorScheme.primary
                    )
            )
        }
        DropdownMenu(
            modifier = Modifier
                .width(width)
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
            expanded = isDropDownExpanded,
            onDismissRequest = {
                isDropDownExpanded = false
            },
            properties = PopupProperties(
                focusable = true,
                clippingEnabled = false,
                /*usePlatformDefaultWidth = false*/
            )
        ) {
            wages.forEachIndexed { index, wage ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            isDropDownExpanded = false
                            onIndexChange(index)
                        }
                        .background(
                            if (index == selectedIndex) {
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                            } else {
                                Color.Transparent
                            }
                        )
                        .padding(8.dp)
                        .padding(start = 8.dp, end = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = String.format(Locale.ENGLISH, "%.2f", wage),
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = if (index == selectedIndex) {
                                MaterialTheme.colorScheme.primary.copy(
                                    alpha = 1f
                                )
                            } else {
                                MaterialTheme.colorScheme.onSurface.copy(
                                    alpha = 0.8f
                                )
                            },
                            fontWeight = if (index == selectedIndex) {
                                FontWeight.Bold
                            } else {
                                FontWeight.Light
                            },
                            letterSpacing = 0.sp,
                        ),
                    )
                    if (index == selectedIndex) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.CenterVertically),
                            contentAlignment = Alignment.CenterEnd
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.check_24),
                                contentDescription = "Check Icon",
                                modifier = Modifier,
                                //.padding(end = 8.dp),
                                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
                                alignment = Alignment.CenterEnd
                            )
                        }
                    }
                }
            }
        }
    }
}

@Stable
fun Modifier.offsetAbsolute(
    on: (offset: Offset) -> Unit
): Modifier {
    return this.then(
        Modifier.onGloballyPositioned { layoutCoordinates ->
            val offset = layoutCoordinates.localToWindow(Offset.Zero)
            on(offset)
        }
    )
}

@Stable
fun Modifier.offsetAbsolute(
    density: Float,
    on: (offset: DpOffset) -> Unit
): Modifier {
    return this.then(
        Modifier.onGloballyPositioned { layoutCoordinates ->
            val size = layoutCoordinates.size
            val offset =
                layoutCoordinates.localToWindow(Offset(0f, 0f))
            if (size.width > 0 && size.height > 0) {
                val c = (offset.y/* + size.height.toFloat()*/)
                val dpOffset = DpOffset(
                    x = (offset.x / density).dp,
                    y = (c / density).dp
                )
                on(dpOffset)
            }
        }
    )
}

@Stable
fun Modifier.measure(density: Float, onMeasure: (DpSize) -> Unit): Modifier {
    return this.then(
        Modifier.onGloballyPositioned { layoutCoordinates ->
            val dpSize = DpSize(
                width = (layoutCoordinates.size.width / density).dp,
                height = (layoutCoordinates.size.height / density).dp
            )
            onMeasure(dpSize)
        }
    )
}

@Stable
fun Modifier.measure(on: (size: IntSize) -> Unit): Modifier {
    return this.then(
        Modifier.onGloballyPositioned { layoutCoordinates ->
            on(layoutCoordinates.size)
        }
    )
}


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

@Composable
fun Input(
    modifier: Modifier = Modifier,
    hint: String? = null,
    onValueChange: (Float, Boolean) -> Unit
) {
    var total by remember {
        mutableStateOf("")
    }
    var isError by remember {
        mutableStateOf(false)
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
    ) {
        TextField(
            value = total,
            onValueChange = { input ->
                // Filtra solo dígitos y el punto decimal
                val filteredInput = input.filter { it.isDigit() || it == '.' }

                val validInput = when {
                    filteredInput.isNotEmpty() && filteredInput[0] == '.' -> {
                        // Si el primer carácter es un punto
                        isError = false
                        total
                    }

                    filteredInput.count { it == '.' } > 1 -> {
                        // Si ya hay un punto, no permitir más
                        isError = false
                        total
                    }

                    filteredInput.contains('.') && filteredInput.indexOf('.') == filteredInput.length - 1 -> {
                        // Si el punto es el último carácter, marcar error
                        isError = true
                        filteredInput
                    }

                    else -> {
                        isError = false
                        filteredInput
                    }
                }

                // Si el valor es válido, actualizamos el estado
                total = validInput

                // Llamamos al callback de cambio de valor (transformamos el string a Float)
                onValueChange(validInput.toFloatOrNull() ?: 0f, isError)
            },
            label = {
                if (hint != null) {
                    Row(
                        modifier = Modifier
                            .horizontalScroll(rememberScrollState())
                    ) {
                        Text(
                            text = hint,
                            color = MaterialTheme.colorScheme.onSurface.copy(
                                alpha = 0.5f
                            ),
                        )
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Decimal,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions {
                // Ocultar el teclado al presionar "Done"
                // LocalSoftwareKeyboardController.current?.hide()
            },
            isError = isError,
            colors = TextFieldDefaults.colors().copy(
                unfocusedContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                focusedContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                /*focusedIndicatorColor = MaterialTheme.colorScheme.secondary,
                unfocusedIndicatorColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f),*/
            ),
            shape = RoundedCornerShape(
                topStart = 8.dp,
                topEnd = 8.dp,
            )
        )
    }
}
