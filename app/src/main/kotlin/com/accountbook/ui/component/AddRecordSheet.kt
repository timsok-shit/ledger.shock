package com.accountbook.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import com.accountbook.domain.model.Category
import com.accountbook.domain.model.Record
import com.accountbook.ui.theme.Black
import com.accountbook.ui.theme.Peacock
import com.accountbook.ui.theme.SlantLeft
import com.accountbook.ui.theme.SlantRight
import com.accountbook.ui.theme.SpaceGrotesk
import com.accountbook.ui.theme.SurfaceDark
import com.accountbook.ui.theme.White
import com.accountbook.ui.theme.Yellow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun AddRecordSheet(
    categories: List<Category>,
    onDismiss: () -> Unit,
    onConfirm: (type: String, amount: Double, date: String, category: String, note: String) -> Unit,
    onAddCategory: (suspend (String) -> Unit)? = null,
    editRecord: Record? = null
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val isEditMode = editRecord != null

    var recordType by remember { mutableStateOf(editRecord?.getType() ?: "EXPENSE") }
    var amountText by remember { mutableStateOf(editRecord?.getAmount()?.let { if (it == 0.0) "" else it.toString() } ?: "") }
    var selectedDate by remember { mutableStateOf(editRecord?.getDate() ?: SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())) }
    var selectedCategory by remember { mutableStateOf(editRecord?.getCategory() ?: "") }
    var noteText by remember { mutableStateOf(editRecord?.getNote() ?: "") }
    var showDatePicker by remember { mutableStateOf(false) }
    var newCategoryName by remember { mutableStateOf("") }

    val amountError = remember(amountText) {
        if (amountText.isBlank()) null
        else {
            val cleaned = amountText.replace(",", "").replace(" ", "")
            if (cleaned.toDoubleOrNull() == null && cleaned.isNotEmpty()) "请输入有效数字"
            else null
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Black,
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .clip(SlantRight)
                        .background(Yellow)
                        .padding(start = 20.dp, end = 28.dp, top = 6.dp, bottom = 6.dp)
                ) {
                    Text("记账", fontFamily = SpaceGrotesk, fontWeight = FontWeight.Black, fontSize = 18.sp, color = Black)
                }
                Spacer(Modifier.width(12.dp))
                Text(if (isEditMode) "编辑记录" else "新增记录", fontFamily = SpaceGrotesk, fontWeight = FontWeight.Bold, fontSize = 26.sp, color = White)
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier.background(SurfaceDark).clip(SlantLeft)
                ) {
                    Row {
                        Box(
                            modifier = Modifier
                                .clip(SlantRight)
                                .background(if (recordType == "EXPENSE") Yellow else SurfaceDark)
                                .clickable { recordType = "EXPENSE" }
                                .padding(start = 32.dp, end = 40.dp, top = 10.dp, bottom = 10.dp)
                        ) {
                            Text("支出", fontFamily = SpaceGrotesk, fontWeight = FontWeight.Black, fontSize = 15.sp, color = if (recordType == "EXPENSE") Black else White)
                        }
                        Box(
                            modifier = Modifier
                                .clip(SlantRight)
                                .background(if (recordType == "INCOME") Yellow else SurfaceDark)
                                .clickable { recordType = "INCOME" }
                                .padding(start = 32.dp, end = 40.dp, top = 10.dp, bottom = 10.dp)
                        ) {
                            Text("收入", fontFamily = SpaceGrotesk, fontWeight = FontWeight.Black, fontSize = 15.sp, color = if (recordType == "INCOME") Black else White)
                        }
                    }
                }
                Box(
                    modifier = Modifier
                        .clip(SlantRight)
                        .background(Peacock)
                        .clickable { showDatePicker = true }
                        .padding(start = 18.dp, end = 24.dp, top = 10.dp, bottom = 10.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.CalendarMonth, contentDescription = null, tint = White, modifier = Modifier.width(18.dp).height(18.dp))
                        Spacer(Modifier.width(6.dp))
                        Text(selectedDate, fontFamily = SpaceGrotesk, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = White)
                    }
                }
            }

            Column {
                Row(verticalAlignment = Alignment.Bottom) {
                    Text("¥", fontFamily = SpaceGrotesk, fontWeight = FontWeight.Black, fontSize = 38.sp, color = if (amountError != null) Color(0xFFFF4444) else Yellow)
                    QuadInputBox(
                        value = amountText,
                        onValueChange = { input ->
                            val cleaned = input.replace(",", "").replace(" ", "")
                            if (cleaned.isEmpty() || cleaned.toDoubleOrNull() != null || cleaned.matches(Regex("^\\d+\\.?\\d*$"))) {
                                amountText = input
                            }
                        },
                        placeholder = "0.00",
                        backgroundColor = SurfaceDark,
                        borderColor = if (amountError != null) Color(0xFFFF4444) else Yellow,
                        textColor = White,
                        shape = SlantRight,
                        borderWidth = 3.dp,
                        height = 64.dp,
                        textStyle = androidx.compose.ui.text.TextStyle(fontWeight = FontWeight.Black, fontSize = 36.sp, color = White)
                    )
                }
                if (amountError != null) {
                    Text(amountError, fontFamily = SpaceGrotesk, fontWeight = FontWeight.Bold, fontSize = 11.sp, color = Color(0xFFFF4444), modifier = Modifier.padding(start = 32.dp, top = 4.dp))
                }
                Box(modifier = Modifier.fillMaxWidth().height(2.dp).background(if (amountError != null) Color(0xFFFF4444) else Peacock).graphicsLayer { rotationZ = -4f })
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Box(
                    modifier = Modifier
                        .clip(SlantRight)
                        .background(Yellow.copy(alpha = 0.15f))
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Text("分类", fontFamily = SpaceGrotesk, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Yellow)
                }
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    val filteredCategories = categories.filter { it.getType() == recordType || it.getType() == "BOTH" }
                    filteredCategories.forEach { cat ->
                        val isSelected = selectedCategory == cat.getName()
                        Box(
                            modifier = Modifier
                                .clip(SlantLeft)
                                .background(if (isSelected) Yellow else Peacock)
                                .border(2.dp, if (isSelected) Black else White.copy(alpha = 0.3f), SlantLeft)
                                .clickable { selectedCategory = cat.getName() }
                                .padding(start = 20.dp, end = 28.dp, top = 8.dp, bottom = 8.dp)
                        ) {
                            Text(cat.getName(), fontFamily = SpaceGrotesk, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = if (isSelected) Black else White)
                        }
                    }
                }
                QuadInputBox(
                    value = newCategoryName,
                    onValueChange = { newCategoryName = it },
                    placeholder = "自定义分类...",
                    backgroundColor = SurfaceDark,
                    borderColor = White.copy(alpha = 0.15f),
                    textColor = White,
                    shape = SlantLeft,
                    borderWidth = 2.dp,
                    height = 40.dp
                )
                if (newCategoryName.isNotBlank()) {
                    val scope = rememberCoroutineScope()
                    Spacer(Modifier.height(4.dp))
                    Box(
                        modifier = Modifier
                            .clip(SlantRight)
                            .background(Yellow)
                            .clickable {
                                val name = newCategoryName.trim()
                                if (name.isNotBlank() && onAddCategory != null) {
                                    scope.launch {
                                        onAddCategory(name)
                                    }
                                    selectedCategory = name
                                    newCategoryName = ""
                                }
                            }
                            .padding(start = 20.dp, end = 28.dp, top = 8.dp, bottom = 8.dp)
                    ) {
                        Text("+ 添加「$newCategoryName」", fontFamily = SpaceGrotesk, fontWeight = FontWeight.Black, fontSize = 13.sp, color = Black)
                    }
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text("备注", fontFamily = SpaceGrotesk, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Yellow, letterSpacing = 3.sp)
                Box(modifier = Modifier.fillMaxWidth()) {
                    QuadInputBox(
                        value = noteText,
                        onValueChange = { noteText = it },
                        placeholder = "输入备注信息...",
                        backgroundColor = SurfaceDark,
                        borderColor = White.copy(alpha = 0.15f),
                        textColor = White,
                        shape = SlantLeft,
                        borderWidth = 2.dp
                    )
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .drawWithContent {
                                drawRect(Yellow, topLeft = Offset(0f, size.height - 3.dp.toPx()), size = Size(3.dp.toPx(), 3.dp.toPx()))
                                drawContent()
                            }
                    )
                }
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(top = 8.dp, bottom = 24.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(SlantLeft)
                        .background(SurfaceDark)
                        .border(4.dp, White, SlantLeft)
                        .clickable { onDismiss() }
                        .padding(start = 20.dp, end = 28.dp, top = 14.dp, bottom = 14.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("取消", fontFamily = SpaceGrotesk, fontWeight = FontWeight.Black, fontSize = 15.sp, color = White, letterSpacing = 3.sp)
                }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(SlantLeft)
                        .background(Yellow)
                        .clickable {
                            val amount = amountText.toDoubleOrNull() ?: 0.0
                            if (amount > 0.0 && selectedCategory.isNotBlank()) {
                                onConfirm(recordType, amount, selectedDate, selectedCategory, noteText)
                            }
                        }
                        .padding(start = 16.dp, end = 24.dp, top = 14.dp, bottom = 14.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(if (isEditMode) "保存修改" else "确认保存", fontFamily = SpaceGrotesk, fontWeight = FontWeight.Black, fontSize = 15.sp, color = Black, letterSpacing = 3.sp)
                }
            }
        }
    }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                Text(
                    "确定",
                    fontWeight = FontWeight.Bold,
                    color = Yellow,
                    modifier = Modifier.clickable {
                        datePickerState.selectedDateMillis?.let { millis ->
                            selectedDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(millis))
                        }
                        showDatePicker = false
                    }.padding(12.dp)
                )
            },
            dismissButton = {
                Text(
                    "取消",
                    fontWeight = FontWeight.Bold,
                    color = White,
                    modifier = Modifier.clickable { showDatePicker = false }.padding(12.dp)
                )
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}
