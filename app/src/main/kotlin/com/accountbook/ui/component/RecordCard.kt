package com.accountbook.ui.component

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.accountbook.domain.model.Record
import com.accountbook.ui.theme.Black
import com.accountbook.ui.theme.Peacock
import com.accountbook.ui.theme.QuadPerspective
import com.accountbook.ui.theme.SlantLeft
import com.accountbook.ui.theme.SpaceGrotesk
import com.accountbook.ui.theme.White
import com.accountbook.ui.theme.Yellow
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

@Composable
fun RecordCard(
    record: Record,
    onDelete: () -> Unit = {},
    onEdit: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val isIncome = record.getType() == "INCOME"
    val sign = if (isIncome) "+" else "-"
    val statusText = if (isIncome) "已入账" else "已完成"
    val accentColor = if (isIncome) Yellow else White
    val charLabel = when (record.getCategory()) {
        "餐饮", "杂货铺购物" -> "食"
        "娱乐", "电影院" -> "乐"
        "住房", "房租支付" -> "住"
        "交通" -> "行"
        "薪资", "主薪资收入" -> "薪"
        else -> record.getCategory().take(1)
    }
    val charBg = if (isIncome) Yellow else Yellow

    val swipeThreshold = with(LocalDensity.current) { 120.dp.toPx() }
    var offsetX by remember { mutableFloatStateOf(0f) }
    var isFlashing by remember { mutableStateOf(false) }
    val flashAlpha by animateFloatAsState(
        targetValue = if (isFlashing) 0.4f else 0f,
        animationSpec = tween(durationMillis = 150),
        label = "flash"
    )

    LaunchedEffect(isFlashing) {
        if (isFlashing) {
            kotlinx.coroutines.delay(150)
            isFlashing = false
        }
    }

    val animatedOffset by animateFloatAsState(
        targetValue = offsetX,
        animationSpec = spring(dampingRatio = 0.7f, stiffness = Spring.StiffnessLow),
        label = "swipe"
    )
    val swipeProgress = min(abs(animatedOffset) / swipeThreshold, 1f)

    Box(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .matchParentSize()
                .clip(QuadPerspective)
                .background(Black),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .graphicsLayer { alpha = swipeProgress }
                    .clip(SlantLeft)
                    .background(Peacock)
                    .clickable {
                        onEdit()
                        offsetX = 0f
                    }
                    .padding(horizontal = 24.dp, vertical = 28.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Edit, contentDescription = "编辑", tint = White, modifier = Modifier.size(26.dp))
            }
            Box(
                modifier = Modifier
                    .graphicsLayer { alpha = swipeProgress }
                    .clip(SlantLeft)
                    .background(Color(0xFFCC0000))
                    .clickable {
                        onDelete()
                        offsetX = 0f
                    }
                    .padding(horizontal = 24.dp, vertical = 28.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Delete, contentDescription = "删除", tint = White, modifier = Modifier.size(26.dp))
            }
        }

        Box(
            modifier = Modifier
                .offset { IntOffset(animatedOffset.roundToInt(), 0) }
                .pointerInput(Unit) {
                    detectHorizontalDragGestures(
                        onDragEnd = {
                            offsetX = if (abs(offsetX) >= swipeThreshold) -swipeThreshold else 0f
                        },
                        onDragCancel = {
                            offsetX = if (abs(offsetX) >= swipeThreshold) -swipeThreshold else 0f
                        }
                    ) { _, dragAmount ->
                        val newOffset = offsetX + dragAmount
                        offsetX = newOffset.coerceIn(-swipeThreshold, 0f)
                    }
                }
                .pointerInput(Unit) {
                    detectTapGestures(
                        onLongPress = {
                            isFlashing = true
                            if (offsetX < 0f) {
                                offsetX = 0f
                            }
                        },
                        onTap = {
                            isFlashing = true
                            if (offsetX < 0f) {
                                offsetX = 0f
                            }
                        }
                    )
                }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(x = 8.dp, y = 8.dp)
                    .graphicsLayer {
                        rotationY = 4f
                        transformOrigin = androidx.compose.ui.graphics.TransformOrigin(0f, 0.5f)
                    }
                    .clip(QuadPerspective)
                    .background(Black)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .graphicsLayer {
                        rotationY = 4f
                        transformOrigin = androidx.compose.ui.graphics.TransformOrigin(0f, 0.5f)
                    }
                    .clip(QuadPerspective)
                    .background(White)
                    .padding(6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(QuadPerspective)
                        .background(Peacock)
                        .padding(20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(52.dp)
                                .clip(SlantLeft)
                                .background(charBg),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(charLabel, fontFamily = SpaceGrotesk, fontWeight = FontWeight.Black, fontSize = 24.sp, color = Black)
                        }
                        Column(modifier = Modifier.padding(start = 16.dp).weight(1f)) {
                            Text(record.getCategory(), fontFamily = SpaceGrotesk, fontWeight = FontWeight.Bold, fontSize = 19.sp, color = White)
                            Spacer(Modifier.height(4.dp))
                            Text(record.getDate(), fontFamily = SpaceGrotesk, fontSize = 13.sp, color = White.copy(alpha = 0.7f))
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text("$sign¥%,.0f".format(record.getAmount()), fontFamily = SpaceGrotesk, fontWeight = FontWeight.Black, fontSize = 26.sp, color = accentColor)
                            Spacer(Modifier.height(6.dp))
                            Box(
                                modifier = Modifier
                                    .graphicsLayer { translationX = 8.dp.toPx(); translationY = (-4).dp.toPx() }
                                    .clip(RoundedCornerShape(0.dp))
                                    .background(White)
                                    .border(2.dp, Black)
                                    .padding(horizontal = 12.dp, vertical = 4.dp)
                            ) {
                                Text(statusText, fontFamily = SpaceGrotesk, fontWeight = FontWeight.Bold, fontSize = 11.sp, color = Black)
                            }
                        }
                    }
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .graphicsLayer { alpha = flashAlpha }
                            .clip(QuadPerspective)
                            .background(White)
                    )
                }
            }
        }
    }
}
