package com.accountbook.ui.screen

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.accountbook.domain.model.Record
import com.accountbook.ui.component.AddRecordSheet
import com.accountbook.ui.component.LedgerTopBar
import com.accountbook.ui.component.MonthSelector
import com.accountbook.ui.component.RecordCard
import com.accountbook.ui.component.SummaryCard
import com.accountbook.ui.theme.Black
import com.accountbook.ui.theme.QuadFab
import com.accountbook.ui.theme.SlantLeft
import com.accountbook.ui.theme.SlantRight
import com.accountbook.ui.theme.SpaceGrotesk
import com.accountbook.ui.theme.SurfaceDark
import com.accountbook.ui.theme.White
import com.accountbook.ui.theme.Yellow
import com.accountbook.ui.component.P5rStaggeredItem
import com.accountbook.ui.component.p5rFabEnter
import com.accountbook.ui.component.p5rFabExit
import com.accountbook.viewmodel.CategoryViewModel
import com.accountbook.viewmodel.RecordViewModel

@Composable
fun HomeScreen(
    recordViewModel: RecordViewModel,
    categoryViewModel: CategoryViewModel
) {
    val uiState by recordViewModel.uiState.collectAsState()
    val categoryState by categoryViewModel.uiState.collectAsState()
    var showAddSheet by remember { mutableStateOf(false) }
    var editRecord by remember { mutableStateOf<Record?>(null) }
    var searchActive by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    val filteredRecords = if (searchQuery.isBlank()) {
        uiState.records
    } else {
        val q = searchQuery.lowercase()
        uiState.records.filter {
            it.getCategory().lowercase().contains(q) || it.getNote().lowercase().contains(q)
        }
    }

    val listState = rememberLazyListState()
    val isScrollingUp by remember {
        derivedStateOf {
            val firstVisibleIndex = listState.firstVisibleItemIndex
            val firstVisibleScrollOffset = listState.firstVisibleItemScrollOffset
            if (firstVisibleIndex == 0) {
                firstVisibleScrollOffset == 0 || firstVisibleScrollOffset < listState.layoutInfo.visibleItemsInfo.getOrNull(0)?.size?.toFloat()?.toInt() ?: 0
            } else {
                listState.layoutInfo.visibleItemsInfo.getOrNull(1)?.index?.let { it <= firstVisibleIndex + 1 } ?: false
            }
        }
    }
    val shouldShowBar by remember {
        derivedStateOf {
            val index = listState.firstVisibleItemIndex
            val offset = listState.firstVisibleItemScrollOffset
            if (index == 0 && offset == 0) true
            else if (index == 0) offset < 50
            else listState.layoutInfo.totalItemsCount <= 3
        }
    }
    val isScrolling by remember {
        derivedStateOf { listState.isScrollInProgress }
    }

    Scaffold(
        floatingActionButton = {
            AnimatedVisibility(
                visible = !isScrolling,
                enter = p5rFabEnter(),
                exit = p5rFabExit()
            ) {
                FloatingActionButton(
                    onClick = { showAddSheet = true },
                    containerColor = Yellow,
                    contentColor = Black,
                    shape = QuadFab
                ) {
                    Icon(Icons.Default.Add, contentDescription = "记一笔")
                }
            }
        },
        containerColor = Black,
        topBar = {
            LedgerTopBar(
                title = "LEDGER.SHOCK",
                searchActive = searchActive,
                searchQuery = searchQuery,
                onSearchQueryChange = { searchQuery = it },
                onSearchActiveChange = { searchActive = it },
                visible = shouldShowBar,
                rightContent = {
                    Text(
                        if (uiState.records.isEmpty()) "" else "${uiState.records.size}",
                        fontFamily = SpaceGrotesk,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = White.copy(alpha = 0.5f)
                    )
                    Spacer(Modifier.width(8.dp))
                    Icon(
                        Icons.Default.Search,
                        contentDescription = "搜索",
                        tint = White,
                        modifier = Modifier.size(24.dp).clickable { searchActive = true }
                    )
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .background(Black)
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            item {
                Spacer(Modifier.height(8.dp))

                MonthSelector(
                    month = uiState.selectedMonth,
                    onPrevious = { recordViewModel.previousMonth() },
                    onNext = { recordViewModel.nextMonth() }
                )
            }

            item {
                Spacer(Modifier.height(16.dp))

                SummaryCard(
                    incomeTotal = uiState.incomeTotal,
                    expenseTotal = uiState.expenseTotal,
                    balance = uiState.monthlyTotal
                )
            }

            item {
                Spacer(Modifier.height(16.dp))

                Text(
                    if (searchActive && searchQuery.isNotBlank()) "搜索结果 (${filteredRecords.size})" else "交易记录",
                    fontFamily = SpaceGrotesk,
                    fontWeight = FontWeight.Black,
                    fontSize = 17.sp,
                    color = Black,
                    modifier = Modifier
                        .graphicsLayer { rotationZ = -2f }
                        .clip(SlantRight)
                        .background(White)
                        .padding(start = 20.dp, end = 28.dp, top = 5.dp, bottom = 5.dp)
                )
            }

            item { Spacer(Modifier.height(12.dp)) }

            if (filteredRecords.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(160.dp)
                            .graphicsLayer { rotationZ = -2f }
                            .clip(SlantRight)
                            .background(SurfaceDark)
                            .border(2.dp, White.copy(alpha = 0.3f), SlantRight),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                if (searchActive && searchQuery.isNotBlank()) "//" else "Ø",
                                fontFamily = SpaceGrotesk,
                                fontWeight = FontWeight.Black,
                                fontSize = 32.sp,
                                color = Yellow.copy(alpha = 0.3f)
                            )
                            Text(
                                if (searchActive && searchQuery.isNotBlank()) "未找到「$searchQuery」相关记录" else "暂无记录",
                                fontFamily = SpaceGrotesk,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = White.copy(alpha = 0.4f)
                            )
                            if (searchActive && searchQuery.isNotBlank()) {
                                Text(
                                    "尝试其他关键词搜索",
                                    fontFamily = SpaceGrotesk,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp,
                                    color = White.copy(alpha = 0.25f)
                                )
                            }
                        }
                    }
                }
            } else {
                items(filteredRecords, key = { it.getId() }) { record ->
                    P5rStaggeredItem(
                        index = filteredRecords.indexOf(record),
                        delayPerItem = 40,
                        maxDelay = 250
                    ) { animModifier ->
                        RecordCard(
                            record = record,
                            onDelete = { recordViewModel.deleteRecord(record) },
                            onEdit = { editRecord = record },
                            modifier = animModifier.padding(bottom = 12.dp)
                        )
                    }
                }
            }

            item { Spacer(Modifier.height(80.dp)) }
        }
    }

    if (showAddSheet) {
        AddRecordSheet(
            categories = categoryState.categories,
            onDismiss = { showAddSheet = false },
            onConfirm = { type, amount, date, category, note ->
                recordViewModel.addRecord(type, amount, date, category, note)
                showAddSheet = false
            },
            onAddCategory = { name ->
                categoryViewModel.addCategory(name, "EXPENSE")
            }
        )
    }

    if (editRecord != null) {
        AddRecordSheet(
            categories = categoryState.categories,
            onDismiss = { editRecord = null },
            onConfirm = { type, amount, date, category, note ->
                recordViewModel.updateRecord(editRecord!!, type, amount, date, category, note)
                editRecord = null
            },
            onAddCategory = { name ->
                categoryViewModel.addCategory(name, "EXPENSE")
            },
            editRecord = editRecord
        )
    }
}
