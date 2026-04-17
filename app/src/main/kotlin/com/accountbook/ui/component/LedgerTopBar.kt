package com.accountbook.ui.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.accountbook.ui.theme.Peacock
import com.accountbook.ui.theme.SpaceGrotesk
import com.accountbook.ui.theme.TopBarSlant
import com.accountbook.ui.theme.White
import com.accountbook.ui.theme.Yellow

@Composable
fun LedgerTopBar(
    title: String,
    searchActive: Boolean = false,
    searchQuery: String = "",
    onSearchQueryChange: (String) -> Unit = {},
    onSearchActiveChange: (Boolean) -> Unit = {},
    rightContent: @Composable () -> Unit = {},
    visible: Boolean = true
) {
    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(initialOffsetY = { -it }),
        exit = slideOutVertically(targetOffsetY = { -it })
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(TopBarSlant)
                .background(Peacock)
                .drawBehind {
                    drawRect(
                        Yellow,
                        topLeft = Offset.Zero,
                        size = Size(4.dp.toPx(), size.height)
                    )
                    if (!searchActive) {
                        val accentSize = 14.dp.toPx()
                        val accentPath = Path().apply {
                            moveTo(0f, 0f)
                            lineTo(accentSize, 0f)
                            lineTo(0f, accentSize)
                            close()
                        }
                        drawPath(accentPath, Yellow)
                    }
                    drawRect(
                        Yellow,
                        topLeft = Offset(0f, size.height - 2.dp.toPx()),
                        size = Size(size.width * 0.92f, 2.dp.toPx())
                    )
                    if (searchActive) {
                        drawRect(
                            Yellow,
                            topLeft = Offset.Zero,
                            size = Size(size.width, 2.dp.toPx())
                        )
                    }
                }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 16.dp, top = 14.dp, bottom = 18.dp)
            ) {
                if (searchActive) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "返回",
                            tint = Yellow,
                            modifier = Modifier.size(24.dp).clickable {
                                onSearchActiveChange(false)
                                onSearchQueryChange("")
                            }
                        )
                        Spacer(Modifier.width(8.dp))
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = onSearchQueryChange,
                            placeholder = {
                                Text(
                                    "搜索类别或备注...",
                                    fontFamily = SpaceGrotesk,
                                    color = White.copy(alpha = 0.4f)
                                )
                            },
                            textStyle = TextStyle(
                                fontFamily = SpaceGrotesk,
                                fontSize = 15.sp,
                                color = White
                            ),
                            singleLine = true,
                            modifier = Modifier.weight(1f),
                            colors = androidx.compose.material3.TextFieldDefaults.colors(
                                focusedTextColor = White,
                                unfocusedTextColor = White,
                                cursorColor = Yellow,
                                focusedIndicatorColor = Yellow,
                                unfocusedIndicatorColor = White.copy(alpha = 0.3f)
                            )
                        )
                        if (searchQuery.isNotEmpty()) {
                            Icon(
                                Icons.Default.Close,
                                contentDescription = "清除",
                                tint = Yellow,
                                modifier = Modifier.size(20.dp).clickable { onSearchQueryChange("") }
                            )
                        }
                    }
                } else {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            title,
                            fontFamily = SpaceGrotesk,
                            fontWeight = FontWeight.Black,
                            fontSize = 24.sp,
                            color = Yellow,
                            modifier = Modifier.graphicsLayer { rotationZ = -3f }
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            rightContent()
                        }
                    }
                }
            }
        }
    }
}
