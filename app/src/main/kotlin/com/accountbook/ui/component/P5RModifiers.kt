package com.accountbook.ui.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.keyframes
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.accountbook.ui.theme.Black
import com.accountbook.ui.theme.QuadShapeNarrow
import com.accountbook.ui.theme.White
import com.accountbook.ui.theme.Yellow

@Composable
fun QuadBorderBox(
    borderColor: Color = White,
    backgroundColor: Color = Black,
    shape: Shape = QuadShapeNarrow,
    borderWidth: Dp = 4.dp,
    accentColor: Color = Black,
    accentShape: Shape? = null,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .drawBehind {
                val borderPaint = Paint().apply { this.color = borderColor }
                val bgPaint = Paint().apply { this.color = backgroundColor }
                drawIntoCanvas { canvas ->
                    canvas.drawOutline(
                        outline = shape.createOutline(size, layoutDirection, this),
                        paint = borderPaint
                    )
                }
                if (accentShape != null) {
                    val accentPaint = Paint().apply { this.color = accentColor }
                    drawIntoCanvas { canvas ->
                        canvas.drawOutline(
                            outline = accentShape.createOutline(size, layoutDirection, this),
                            paint = accentPaint
                        )
                    }
                }
                val inset = borderWidth.toPx()
                val innerSize = Size(size.width - inset * 2, size.height - inset * 2)
                if (innerSize.width > 0f && innerSize.height > 0f) {
                    drawIntoCanvas { canvas ->
                        canvas.save()
                        canvas.translate(inset, inset)
                        canvas.drawOutline(
                            outline = shape.createOutline(innerSize, layoutDirection, this),
                            paint = bgPaint
                        )
                        canvas.restore()
                    }
                }
            }
            .padding(borderWidth)
    ) {
        content()
    }
}

@Composable
fun QuadButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Yellow,
    textColor: Color = Black,
    borderColor: Color = Black,
    shape: Shape = QuadShapeNarrow,
    borderWidth: Dp = 4.dp,
    height: Dp = 52.dp,
    fontWeight: FontWeight = FontWeight.Black,
    fontSize: androidx.compose.ui.unit.TextUnit = 14.sp,
    letterSpacing: androidx.compose.ui.unit.TextUnit = 1.sp,
) {
    var pressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.95f else 1f,
        animationSpec = keyframes {
            durationMillis = 120
            1f at 0
            0.95f at 60
            1f at 120
        },
        label = "btnScale"
    )
    QuadBorderBox(
        borderColor = borderColor,
        backgroundColor = backgroundColor,
        shape = shape,
        borderWidth = borderWidth
    ) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(height - borderWidth * 2)
                .graphicsLayer { scaleX = scale; scaleY = scale }
                .pointerInput(Unit) {
                    detectTapGestures(
                        onPress = {
                            pressed = true
                            tryAwaitRelease()
                            pressed = false
                            onClick()
                        }
                    )
                },
            contentAlignment = Alignment.Center
        ) {
            Text(text = text, color = textColor, fontWeight = fontWeight, fontSize = fontSize, letterSpacing = letterSpacing)
        }
    }
}

@Composable
fun QuadInputBox(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Black,
    borderColor: Color = White.copy(alpha = 0.3f),
    textColor: Color = White,
    placeholderColor: Color = White.copy(alpha = 0.4f),
    shape: Shape = QuadShapeNarrow,
    borderWidth: Dp = 3.dp,
    height: Dp = 52.dp,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    textStyle: TextStyle = TextStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp, color = White),
) {
    QuadBorderBox(
        borderColor = borderColor,
        backgroundColor = backgroundColor,
        shape = shape,
        borderWidth = borderWidth
    ) {
        Box(
            modifier = modifier.fillMaxWidth().height(height - borderWidth * 2),
            contentAlignment = Alignment.CenterStart
        ) {
            if (value.isEmpty()) {
                Text(text = placeholder, color = placeholderColor, modifier = Modifier.padding(start = 16.dp), fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                textStyle = textStyle.copy(color = textColor),
                keyboardOptions = keyboardOptions,
                visualTransformation = visualTransformation,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
            )
        }
    }
}
