package com.accountbook.ui.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.accountbook.ui.theme.Peacock
import com.accountbook.ui.theme.SlantRight
import com.accountbook.ui.theme.SpaceGrotesk
import com.accountbook.ui.theme.White
import com.accountbook.ui.theme.Black

@Composable
fun MonthSelector(
    month: String,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
    modifier: Modifier = Modifier
) {
    val parts = month.split("-")
    val year = parts.getOrNull(0) ?: ""
    val mon = parts.getOrNull(1) ?: ""

    var slideDirection by remember { mutableIntStateOf(1) }

    Row(
        modifier = modifier.fillMaxWidth().graphicsLayer { rotationZ = -2f },
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(40.dp).background(Peacock).clickable {
                slideDirection = -1
                onPrevious()
            },
            contentAlignment = Alignment.Center
        ) {
            Text("‹", fontFamily = SpaceGrotesk, fontWeight = FontWeight.Black, fontSize = 22.sp, color = White)
        }
        Spacer(Modifier.width(12.dp))
        Box(
            modifier = Modifier.clip(SlantRight).background(White).padding(start = 28.dp, end = 36.dp, top = 8.dp, bottom = 8.dp)
        ) {
            AnimatedContent(
                targetState = month,
                transitionSpec = {
                    if (slideDirection > 0) {
                        slideInHorizontally(
                            animationSpec = tween(250),
                            initialOffsetX = { it / 2 }
                        ) + fadeIn(animationSpec = tween(200)) togetherWith
                        slideOutHorizontally(
                            animationSpec = tween(250),
                            targetOffsetX = { -it / 2 }
                        ) + fadeOut(animationSpec = tween(150))
                    } else {
                        slideInHorizontally(
                            animationSpec = tween(250),
                            initialOffsetX = { -it / 2 }
                        ) + fadeIn(animationSpec = tween(200)) togetherWith
                        slideOutHorizontally(
                            animationSpec = tween(250),
                            targetOffsetX = { it / 2 }
                        ) + fadeOut(animationSpec = tween(150))
                    }
                },
                label = "monthSlide"
            ) { targetMonth ->
                val p = targetMonth.split("-")
                val y = p.getOrNull(0) ?: ""
                val m = p.getOrNull(1) ?: ""
                Text("${y}年${m}月", fontFamily = SpaceGrotesk, fontWeight = FontWeight.Black, fontSize = 20.sp, color = Black)
            }
        }
        Spacer(Modifier.width(12.dp))
        Box(
            modifier = Modifier.size(40.dp).background(Peacock).clickable {
                slideDirection = 1
                onNext()
            },
            contentAlignment = Alignment.Center
        ) {
            Text("›", fontFamily = SpaceGrotesk, fontWeight = FontWeight.Black, fontSize = 22.sp, color = White)
        }
    }
}
