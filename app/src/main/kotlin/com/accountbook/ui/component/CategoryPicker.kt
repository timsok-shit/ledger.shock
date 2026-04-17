package com.accountbook.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.accountbook.domain.model.Category
import com.accountbook.ui.theme.Black
import com.accountbook.ui.theme.QuadShapeNarrow
import com.accountbook.ui.theme.SurfaceMid
import com.accountbook.ui.theme.White
import com.accountbook.ui.theme.Yellow
import kotlinx.coroutines.launch

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CategoryPicker(
    categories: List<Category>,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit,
    onNewCategory: (String) -> Unit,
    onNewCategorySuspend: (suspend (String) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    var newCategoryName by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    Column(modifier = modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .clip(QuadShapeNarrow)
                .background(Yellow.copy(alpha = 0.15f))
                .padding(horizontal = 12.dp, vertical = 4.dp)
        ) {
            Text(text = "分类", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Black, color = Yellow)
        }
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
        ) {
            categories.forEach { category ->
                val isSelected = selectedCategory == category.getName()
                QuadBorderBox(
                    borderColor = if (isSelected) Yellow else White.copy(alpha = 0.2f),
                    backgroundColor = if (isSelected) Yellow else SurfaceMid,
                    shape = QuadShapeNarrow,
                    borderWidth = 3.dp
                ) {
                    Box(
                        modifier = Modifier
                            .clickable { onCategorySelected(category.getName()) }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(category.getName(), fontWeight = FontWeight.Black, fontSize = 12.sp, color = if (isSelected) Black else White)
                    }
                }
            }
        }
        QuadInputBox(
            value = newCategoryName,
            onValueChange = { newCategoryName = it },
            placeholder = "自定义分类",
            backgroundColor = SurfaceMid,
            textColor = White,
            shape = QuadShapeNarrow,
            borderWidth = 3.dp,
            height = 44.dp
        )
        if (newCategoryName.isNotBlank()) {
            QuadButton(
                text = "添加",
                onClick = {
                    val name = newCategoryName.trim()
                    if (onNewCategorySuspend != null) {
                        scope.launch {
                            onNewCategorySuspend(name)
                            onNewCategory(name)
                        }
                    } else {
                        onNewCategory(name)
                    }
                    newCategoryName = ""
                },
                height = 40.dp,
                fontSize = 12.sp,
                shape = QuadShapeNarrow,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}
