package com.accountbook.ui.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

/**
 * P5R NavHost 页面转场动画
 * 撕裂式滑入 + 速度感曲线
 */
fun p5rSlideIntoContent(
    direction: Int = 1,
    durationMillis: Int = 350
): androidx.compose.animation.ContentTransform {
    val enter = slideInHorizontally(
        animationSpec = tween(durationMillis = durationMillis),
        initialOffsetX = { it * direction }
    ) + fadeIn(
        animationSpec = tween(durationMillis = durationMillis, delayMillis = 50)
    )

    val exit = slideOutHorizontally(
        animationSpec = tween(durationMillis = durationMillis),
        targetOffsetX = { -it * direction / 3 }
    ) + fadeOut(
        animationSpec = tween(durationMillis = durationMillis)
    )

    return enter togetherWith exit
}

/**
 * P5R 底部向上滑入转场（用于 AddRecordSheet 等面板）
 */
fun p5rSlideUpEnter(
    durationMillis: Int = 400
): EnterTransition {
    return slideInVertically(
        animationSpec = tween(durationMillis = durationMillis),
        initialOffsetY = { it / 2 }
    ) + scaleIn(
        animationSpec = tween(durationMillis = durationMillis),
        initialScale = 0.92f
    ) + fadeIn(
        animationSpec = tween(durationMillis = durationMillis)
    )
}

fun p5rSlideDownExit(
    durationMillis: Int = 300
): ExitTransition {
    return slideOutVertically(
        animationSpec = tween(durationMillis = durationMillis),
        targetOffsetY = { it / 3 }
    ) + fadeOut(
        animationSpec = tween(durationMillis = durationMillis)
    )
}

/**
 * FAB P5R 风格出现动画：缩小滑入 + 淡入
 */
fun p5rFabEnter(): EnterTransition {
    return slideInVertically(
        animationSpec = tween(durationMillis = 300),
        initialOffsetY = { it / 4 }
    ) + scaleIn(
        animationSpec = tween(durationMillis = 300),
        initialScale = 0.7f
    ) + fadeIn(
        animationSpec = tween(durationMillis = 250)
    )
}

fun p5rFabExit(): ExitTransition {
    return slideOutVertically(
        animationSpec = tween(durationMillis = 200),
        targetOffsetY = { it / 4 }
    ) + scaleOut(
        animationSpec = tween(durationMillis = 200),
        targetScale = 0.7f
    ) + fadeOut(
        animationSpec = tween(durationMillis = 200)
    )
}

/**
 * 列表项依次滑入动画（P5R 错位延迟效果）
 * 每个 item 带延迟的向上滑入 + 淡入
 */
@Composable
fun P5rStaggeredItem(
    index: Int,
    delayPerItem: Int = 50,
    maxDelay: Int = 300,
    content: @Composable (Modifier) -> Unit
) {
    var visible by remember { mutableStateOf(false) }
    val delayMs = (index * delayPerItem).coerceAtMost(maxDelay)

    LaunchedEffect(Unit) {
        visible = true
    }

    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(
            animationSpec = tween(durationMillis = 350, delayMillis = delayMs),
            initialOffsetY = { 30 }
        ) + fadeIn(
            animationSpec = tween(durationMillis = 300, delayMillis = delayMs)
        )
    ) {
        content(Modifier)
    }
}

/**
 * 简单的数字计数动画版本（使用 animateFloatAsState）
 */
@Composable
fun SimpleAnimatedNumber(
    targetValue: Float,
    durationMillis: Int = 600,
    content: @Composable (Float) -> Unit
) {
    val animatedValue by animateFloatAsState(
        targetValue = targetValue,
        animationSpec = tween(durationMillis = durationMillis),
        label = "simpleCountAnim"
    )
    content(animatedValue)
}

/**
 * P5R 按钮点击闪变效果（颜色快速闪变反馈）
 * 配合 QuadButton 使用，增强点击反馈
 */
@Composable
fun rememberFlashState(): FlashState {
    var isFlashing by remember { mutableStateOf(false) }
    return FlashState(
        isFlashing = isFlashing,
        trigger = { isFlashing = true },
        reset = { isFlashing = false }
    )
}

data class FlashState(
    val isFlashing: Boolean,
    val trigger: () -> Unit,
    val reset: () -> Unit
)

/**
 * P5R Tab 内容切换动画
 * 左右滑动切换 + 淡入淡出
 */
@Composable
fun P5rTabContent(
    targetState: Int,
    modifier: Modifier = Modifier,
    content: @Composable (Int) -> Unit
) {
    AnimatedContent(
        targetState = targetState,
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
            }.using(
                androidx.compose.animation.SizeTransform(clip = false)
            )
        },
        label = "p5rTabContent",
        modifier = modifier
    ) { state ->
        content(state)
    }
}
