package com.accountbook.ui.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.accountbook.ui.theme.Black
import com.accountbook.ui.theme.Peacock
import com.accountbook.ui.theme.PeacockDark
import com.accountbook.ui.theme.QuadShapeA
import com.accountbook.ui.theme.SlantRight
import com.accountbook.ui.theme.SpaceGrotesk
import com.accountbook.ui.theme.SurfaceDark
import com.accountbook.ui.theme.White
import com.accountbook.ui.theme.Yellow

@Composable
fun SummaryCard(
    incomeTotal: Double,
    expenseTotal: Double,
    balance: Double,
    modifier: Modifier = Modifier
) {
    val animatedBalance by animateFloatAsState(
        targetValue = balance.toFloat(),
        animationSpec = tween(durationMillis = 800),
        label = "balanceAnim"
    )
    val animatedIncome by animateFloatAsState(
        targetValue = incomeTotal.toFloat(),
        animationSpec = tween(durationMillis = 700, delayMillis = 100),
        label = "incomeAnim"
    )
    val animatedExpense by animateFloatAsState(
        targetValue = expenseTotal.toFloat(),
        animationSpec = tween(durationMillis = 700, delayMillis = 200),
        label = "expenseAnim"
    )

    Box(modifier = modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .offset(x = 8.dp, y = 8.dp)
                .graphicsLayer { rotationZ = -3f }
                .background(Peacock)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .graphicsLayer { rotationZ = -3f }
                .background(Yellow)
                .border(4.dp, Black)
                .padding(24.dp)
        ) {
            Column {
                Text("当前结余", fontFamily = SpaceGrotesk, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Black, letterSpacing = 3.sp)
                Text("¥%,.0f".format(animatedBalance), fontFamily = SpaceGrotesk, fontWeight = FontWeight.Black, fontSize = 48.sp, color = Black)
                Spacer(Modifier.height(12.dp))
                Row {
                    Column(modifier = Modifier
                        .drawWithContent {
                            drawRect(Black, topLeft = Offset.Zero, size = Size(4.dp.toPx(), size.height))
                            drawContent()
                        }
                        .padding(start = 24.dp)
                    ) {
                        Text("收入", fontFamily = SpaceGrotesk, fontWeight = FontWeight.Bold, fontSize = 11.sp, color = Black, letterSpacing = 2.sp)
                        Text("+¥%,.0f".format(animatedIncome), fontFamily = SpaceGrotesk, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = PeacockDark)
                    }
                    Spacer(Modifier.width(32.dp))
                    Column {
                        Text("支出", fontFamily = SpaceGrotesk, fontWeight = FontWeight.Bold, fontSize = 11.sp, color = Black, letterSpacing = 2.sp)
                        Text("-¥%,.0f".format(animatedExpense), fontFamily = SpaceGrotesk, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color(0xFFCC0000))
                    }
                }
            }
        }
    }
}
