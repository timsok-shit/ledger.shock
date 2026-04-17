package com.accountbook.ui.screen

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.accountbook.ui.component.LedgerTopBar
import com.accountbook.ui.theme.Black
import com.accountbook.ui.theme.Peacock
import com.accountbook.ui.theme.SlantLeft
import com.accountbook.ui.theme.SlantRight
import com.accountbook.ui.theme.SpaceGrotesk
import com.accountbook.ui.theme.White
import com.accountbook.ui.theme.Yellow
import com.accountbook.viewmodel.StatsViewModel
import java.util.Calendar

@Composable
fun StatsScreen(statsViewModel: StatsViewModel) {
    val uiState by statsViewModel.uiState.collectAsState()
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("支出分布", "月度对比", "排行")
    val hasData = uiState.records.isNotEmpty()
    val cal = Calendar.getInstance()

    Column(modifier = Modifier.fillMaxSize().background(Black)) {
        LedgerTopBar(
            title = "STATS",
            rightContent = {}
        )

        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            tabs.forEachIndexed { i, title ->
                val isSelected = selectedTab == i
                Box(
                    modifier = Modifier
                        .graphicsLayer { rotationZ = -12f }
                        .clip(SlantRight)
                        .background(if (isSelected) Yellow else Peacock)
                        .border(3.dp, if (isSelected) Black else White.copy(alpha = 0.2f), SlantRight)
                        .clickable { selectedTab = i }
                        .padding(horizontal = 24.dp, vertical = 10.dp)
                ) {
                    Text(title, fontFamily = SpaceGrotesk, fontWeight = FontWeight.Black, fontSize = 14.sp, color = if (isSelected) Black else White)
                }
            }
        }

        AnimatedContent(
            targetState = selectedTab,
            transitionSpec = {
                if (targetState > initialState) {
                    slideInHorizontally(
                        animationSpec = tween(durationMillis = 300),
                        initialOffsetX = { it / 2 }
                    ) + fadeIn(
                        animationSpec = tween(durationMillis = 250)
                    ) togetherWith
                    slideOutHorizontally(
                        animationSpec = tween(durationMillis = 300),
                        targetOffsetX = { -it / 3 }
                    ) + fadeOut(
                        animationSpec = tween(durationMillis = 250)
                    )
                } else {
                    slideInHorizontally(
                        animationSpec = tween(durationMillis = 300),
                        initialOffsetX = { -it / 2 }
                    ) + fadeIn(
                        animationSpec = tween(durationMillis = 250)
                    ) togetherWith
                    slideOutHorizontally(
                        animationSpec = tween(durationMillis = 300),
                        targetOffsetX = { it / 3 }
                    ) + fadeOut(
                        animationSpec = tween(durationMillis = 250)
                    )
                }
            },
            label = "statsTabContent",
            modifier = Modifier.fillMaxSize()
        ) { tab ->
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                when (tab) {
                    0 -> {
                        item {
                            Text(
                                "财务效率指数",
                                fontFamily = SpaceGrotesk,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = Yellow,
                                letterSpacing = 4.sp,
                                modifier = Modifier.clip(SlantRight).background(Yellow.copy(alpha = 0.15f)).padding(horizontal = 14.dp, vertical = 4.dp)
                            )
                            Spacer(Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth().height(280.dp),
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                Box(
                                    modifier = Modifier.weight(1f).fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Canvas(modifier = Modifier.fillMaxSize()) {
                                        val cx = size.width / 2
                                        val cy = size.height / 2
                                        val r = size.minDimension / 2 * 0.85f
                                        if (hasData && uiState.categoryBreakdown.isNotEmpty()) {
                                            val categoryData = uiState.categoryBreakdown.entries.map { it.key to it.value }
                                            var startAngle = 0f
                                            val colors = listOf(Yellow, Peacock, White, Color(0xFFCC0000), Color(0xFF00AA88), Peacock.copy(alpha = 0.6f))
                                            categoryData.forEachIndexed { i, (_, amount) ->
                                                val sweep = (amount / uiState.expenseTotal * 360).toFloat()
                                                drawArc(
                                                    color = colors[i % colors.size],
                                                    startAngle = startAngle,
                                                    sweepAngle = sweep,
                                                    useCenter = true,
                                                    topLeft = Offset(cx - r, cy - r),
                                                    size = Size(r * 2, r * 2),
                                                    style = Stroke(width = 1.dp.toPx())
                                                )
                                                startAngle += sweep
                                            }
                                            drawCircle(Black, r * 0.35f)
                                        } else {
                                            drawCircle(White.copy(alpha = 0.08f), r)
                                            drawCircle(Black, r * 0.35f)
                                        }
                                    }
                                    Box(modifier = Modifier.border(2.dp, Yellow).background(Black).padding(horizontal = 10.dp, vertical = 5.dp)) {
                                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                            Text("总计", fontFamily = SpaceGrotesk, fontWeight = FontWeight.Bold, fontSize = 9.sp, color = Yellow)
                                            Text("¥%,.0f".format(uiState.expenseTotal), fontFamily = SpaceGrotesk, fontWeight = FontWeight.Black, fontSize = 17.sp, color = White)
                                        }
                                    }
                                }

                                Column(
                                modifier = Modifier.weight(1f).fillMaxSize(),
                                verticalArrangement = Arrangement.spacedBy(11.dp)
                            ) {
                                    if (hasData && uiState.categoryBreakdown.isNotEmpty()) {
                                        val colors = listOf(Yellow, White, Color(0xFFCC0000), Color(0xFF00AA88), Color(0xFFFF8800))
                                        uiState.categoryBreakdown.entries.take(5).forEachIndexed { i, entry ->
                                            PerspectiveCategoryRow(name = entry.key, amount = entry.value, accentColor = colors[i % colors.size])
                                        }
                                    } else {
                                        Box(
                                            modifier = Modifier.fillMaxSize().clip(SlantLeft).background(com.accountbook.ui.theme.SurfaceDark).border(2.dp, White.copy(alpha = 0.1f), SlantLeft).padding(horizontal = 16.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text("暂无支出数据", fontFamily = SpaceGrotesk, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = White.copy(alpha = 0.3f))
                                        }
                                    }
                                }
                            }
                        }
                    }
                    1 -> {
                        item {
                            Column {
                                Text("月度对比", fontFamily = SpaceGrotesk, fontWeight = FontWeight.Black, fontSize = 18.sp, color = Black, modifier = Modifier.graphicsLayer { rotationZ = -6f }.clip(SlantRight).background(White).padding(horizontal = 24.dp, vertical = 7.dp))
                                Spacer(Modifier.height(12.dp))

                                if (!hasData) {
                                    Box(
                                        modifier = Modifier.fillMaxWidth().height(200.dp).clip(SlantRight).background(com.accountbook.ui.theme.SurfaceDark).border(2.dp, White.copy(alpha = 0.1f), SlantRight).graphicsLayer { rotationZ = -2f },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text("暂无月度数据，请先记账", fontFamily = SpaceGrotesk, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = White.copy(alpha = 0.3f))
                                    }
                                } else {
                                    val year = cal.get(Calendar.YEAR)
                                    val month = cal.get(Calendar.MONTH) + 1
                                    val incomeByMonth = uiState.records.filter { it.getType() == "INCOME" }.groupBy {
                                        it.getDate().substring(0, 7)
                                    }.mapValues { entry -> entry.value.sumOf { it.getAmount() } }
                                    val expenseByMonth = uiState.records.filter { it.getType() == "EXPENSE" }.groupBy {
                                        it.getDate().substring(0, 7)
                                    }.mapValues { entry -> entry.value.sumOf { it.getAmount() } }

                                    val months = (1..month).map { m -> String.format("%d-%02d", year, m) }
                                    val maxVal = (incomeByMonth.values + expenseByMonth.values).maxOrNull()?.coerceAtLeast(1.0) ?: 1.0

                                    Box(
                                        modifier = Modifier.fillMaxWidth().height(240.dp)
                                            .background(com.accountbook.ui.theme.SurfaceDark)
                                            .border(4.dp, Peacock)
                                            .graphicsLayer { rotationZ = -3f }
                                            .padding(top = 12.dp, bottom = 16.dp, start = 20.dp, end = 20.dp)
                                    ) {
                                        Row(
                                            modifier = Modifier.fillMaxSize(),
                                            horizontalArrangement = Arrangement.SpaceEvenly,
                                            verticalAlignment = Alignment.Bottom
                                        ) {
                                            months.takeLast(6).forEach { monthKey ->
                                                val inc = incomeByMonth[monthKey] ?: 0.0
                                                val exp = expenseByMonth[monthKey] ?: 0.0
                                                val incomeH = (inc / maxVal).coerceIn(0.05, 1.0)
                                                val expenseH = (exp / maxVal).coerceIn(0.05, 1.0)
                                                val label = monthKey.substring(5) + "月"

                                                Column(
                                                    horizontalAlignment = Alignment.CenterHorizontally,
                                                    verticalArrangement = Arrangement.Bottom,
                                                    modifier = Modifier.weight(1f)
                                                ) {
                                                    if (inc > 0) {
                                                        Text("+¥${String.format("%,.0f", inc)}", fontFamily = SpaceGrotesk, fontWeight = FontWeight.Bold, fontSize = 9.sp, color = Yellow)
                                                        Spacer(Modifier.height(2.dp))
                                                    }
                                                    if (inc > 0) {
                                                        Box(modifier = Modifier.fillMaxWidth().height((incomeH * 110).dp).border(2.dp, Black).background(Yellow))
                                                        Spacer(Modifier.height(3.dp))
                                                    }
                                                    if (exp > 0) {
                                                        Text("-¥${String.format("%,.0f", exp)}", fontFamily = SpaceGrotesk, fontWeight = FontWeight.Bold, fontSize = 9.sp, color = White)
                                                        Spacer(Modifier.height(2.dp))
                                                    }
                                                    if (exp > 0) {
                                                        Box(modifier = Modifier.fillMaxWidth().height((expenseH * 70).dp).border(2.dp, Black).background(White))
                                                    }
                                                    Spacer(Modifier.height(4.dp))
                                                    Text(label, fontFamily = SpaceGrotesk, fontWeight = FontWeight.Bold, fontSize = 10.sp, color = Yellow)
                                                }
                                            }
                                        }
                                    }

                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                                        modifier = Modifier.padding(start = 8.dp, top = 8.dp)
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) { Box(modifier = Modifier.size(10.dp, 10.dp).background(Yellow)); Spacer(Modifier.width(4.dp)); Text("${year}年收入", fontFamily = SpaceGrotesk, fontSize = 11.sp, color = White) }
                                        Row(verticalAlignment = Alignment.CenterVertically) { Box(modifier = Modifier.size(10.dp, 10.dp).background(White)); Spacer(Modifier.width(4.dp)); Text("${year}年支出", fontFamily = SpaceGrotesk, fontSize = 11.sp, color = White) }
                                    }
                                }
                            }
                        }
                    }
                    2 -> {
                        item {
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text("最高支出项", fontFamily = SpaceGrotesk, fontWeight = FontWeight.Black, fontSize = 18.sp, color = Black, modifier = Modifier.graphicsLayer { rotationZ = -6f }.clip(SlantRight).background(White).padding(horizontal = 24.dp, vertical = 7.dp))
                                if (hasData && uiState.categoryBreakdown.isNotEmpty()) {
                                    val ranked = uiState.categoryBreakdown.entries.map { it.key to it.value }.sortedByDescending { it.second }.take(5)
                                    ranked.forEachIndexed { i, (name, amount) ->
                                        val bg = if (i == 0) Yellow else if (i == 1) White else com.accountbook.ui.theme.SurfaceDark
                                        val fgNum = if (i == 0) Black else if (i == 1) Black else Yellow
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .graphicsLayer { rotationY = (-6 + i * 2).toFloat() }
                                                .offset(x = ((-i * 4).dp))
                                                .clip(SlantLeft)
                                                .background(bg)
                                                .border(3.dp, Black, SlantLeft)
                                                .padding(start = 18.dp, end = 22.dp, top = 14.dp, bottom = 14.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Text("${String.format("%02d", i + 1)}", fontFamily = SpaceGrotesk, fontWeight = FontWeight.Black, fontSize = when(i) { 0 -> 28.sp; 1 -> 24.sp; else -> 20.sp }, color = fgNum)
                                                Spacer(Modifier.width(14.dp))
                                                Text(name, fontFamily = SpaceGrotesk, fontWeight = FontWeight.Black, fontSize = 16.sp, color = if (bg == Yellow || bg == White) Black else White)
                                            }
                                            Text("¥%,.0f".format(amount), fontFamily = SpaceGrotesk, fontWeight = FontWeight.Black, fontSize = 16.sp, color = if (bg == Yellow) Black else if (bg == White) Black else Yellow)
                                        }
                                    }
                                } else {
                                    Box(
                                        modifier = Modifier.fillMaxWidth().height(120.dp).clip(SlantLeft).background(com.accountbook.ui.theme.SurfaceDark).border(2.dp, White.copy(alpha = 0.1f), SlantLeft).padding(horizontal = 16.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text("暂无排行数据", fontFamily = SpaceGrotesk, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = White.copy(alpha = 0.3f))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PerspectiveCategoryRow(name: String, amount: Double, accentColor: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer { rotationY = -15f }
            .offset(x = 6.dp)
            .clip(SlantLeft)
            .background(Peacock)
            .border(3.dp, Yellow, SlantLeft)
            .padding(start = 14.dp, end = 18.dp, top = 10.dp, bottom = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(name, fontFamily = SpaceGrotesk, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = White)
        Text("¥%,.0f".format(amount), fontFamily = SpaceGrotesk, fontWeight = FontWeight.Black, fontSize = 14.sp, color = accentColor)
    }
}
