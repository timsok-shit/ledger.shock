package com.accountbook.ui.screen

import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.accountbook.ui.component.P5rStaggeredItem
import com.accountbook.ui.component.LedgerTopBar
import com.accountbook.ui.theme.Black
import com.accountbook.ui.theme.Peacock
import com.accountbook.ui.theme.SlantRight
import com.accountbook.ui.theme.SpaceGrotesk
import com.accountbook.ui.theme.White
import com.accountbook.ui.theme.Yellow
import com.accountbook.viewmodel.RecordViewModel
import com.accountbook.data.export.CsvExporter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun SettingsScreen(recordViewModel: RecordViewModel) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val uiState by recordViewModel.uiState.collectAsState()
    var showExportToast by remember { mutableStateOf(false) }
    var exportedFileName by remember { mutableStateOf("") } 
    var exportError by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Black)
    ) {
        LedgerTopBar(
            title = "SETTINGS",
            rightContent = {}
        )

        Column(
            modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp, vertical = 32.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            P5rStaggeredItem(index = 0, delayPerItem = 60, maxDelay = 300) { mod ->
                Text(
                    "系统设置",
                    fontFamily = SpaceGrotesk,
                    fontWeight = FontWeight.Black,
                    fontSize = 36.sp,
                    color = Black,
                    modifier = mod
                        .graphicsLayer { rotationZ = -12f }
                        .clip(SlantRight)
                        .background(Yellow)
                        .padding(start = 36.dp, end = 44.dp, top = 18.dp, bottom = 18.dp)
                )
            }

            P5rStaggeredItem(index = 1, delayPerItem = 60, maxDelay = 300) { mod ->
                Box(modifier = mod.fillMaxWidth()) {
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .offset(x = (-6).dp, y = 6.dp)
                            .clip(SlantRight)
                            .background(Peacock)
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .graphicsLayer { rotationZ = -4f }
                            .clip(SlantRight)
                            .background(White)
                            .border(4.dp, Black, SlantRight)
                            .padding(start = 24.dp, end = 32.dp, top = 20.dp, bottom = 20.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier.size(48.dp).background(Black),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.CloudDownload, contentDescription = null, tint = Yellow)
                                }
                                Spacer(Modifier.width(16.dp))
                                Column {
                                    Text("数据导出", fontWeight = FontWeight.ExtraBold, fontSize = 20.sp, color = Black)
                                    Text("导出财务数据至 CSV", fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = Black.copy(alpha = 0.5f))
                                }
                            }
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = null,
                                tint = Black,
                                modifier = Modifier.clickable {
                                    scope.launch {
                                        val result = exportCsv(context, uiState.records)
                                        if (result != null) {
                                            exportedFileName = result
                                            exportError = false
                                            showExportToast = true
                                        } else {
                                            exportError = true
                                            showExportToast = true
                                        }
                                    }
                                }
                            )
                        }
                    }
                }
            }

            P5rStaggeredItem(index = 2, delayPerItem = 60, maxDelay = 300) { mod ->
                Box(modifier = mod.fillMaxWidth()) {
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .offset(x = 6.dp, y = 6.dp)
                            .clip(SlantRight)
                            .background(Peacock)
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .graphicsLayer { rotationZ = 4f }
                            .clip(SlantRight)
                            .background(White)
                            .border(4.dp, Black, SlantRight)
                            .padding(start = 24.dp, end = 32.dp, top = 20.dp, bottom = 20.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier.size(48.dp).background(Black),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.Info, contentDescription = null, tint = Yellow)
                                }
                                Spacer(Modifier.width(16.dp))
                                Column {
                                    Text("关于", fontWeight = FontWeight.ExtraBold, fontSize = 20.sp, color = Black)
                                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                        Box(
                                            modifier = Modifier.background(Peacock).padding(horizontal = 6.dp, vertical = 2.dp)
                                        ) {
                                            Text("版本 1.1.10-SHOCK", fontWeight = FontWeight.Black, fontSize = 10.sp, color = White)
                                        }
                                        Text("隐私政策", fontWeight = FontWeight.Black, fontSize = 11.sp, color = Black.copy(alpha = 0.7f))
                                    }
                                }
                            }
                            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, tint = Black)
                        }
                    }
                }
            }

            AnimatedVisibility(
                visible = showExportToast,
                enter = slideInVertically(
                    animationSpec = tween(durationMillis = 300),
                    initialOffsetY = { it / 3 }
                ) + fadeIn(animationSpec = tween(250)),
                exit = slideOutVertically(
                    animationSpec = tween(durationMillis = 200),
                    targetOffsetY = { it / 3 }
                ) + fadeOut(animationSpec = tween(150))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .graphicsLayer { rotationZ = -2f }
                        .clip(SlantRight)
                        .background(if (exportError) White else Yellow)
                        .border(4.dp, Black, SlantRight)
                        .padding(start = 20.dp, end = 28.dp, top = 16.dp, bottom = 16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.CloudDownload, contentDescription = null, tint = Black)
                            Spacer(Modifier.width(12.dp))
                            Column {
                                Text(
                                    if (exportError) "导出失败" else "导出成功",
                                    fontFamily = SpaceGrotesk, fontWeight = FontWeight.Black, fontSize = 16.sp, color = Black
                                )
                                Text(
                                    exportedFileName,
                                    fontFamily = SpaceGrotesk, fontWeight = FontWeight.Bold, fontSize = 11.sp, color = Black.copy(alpha = 0.6f)
                                )
                            }
                        }
                        Icon(Icons.Default.Close, contentDescription = "关闭", tint = Black, modifier = Modifier.clickable { showExportToast = false })
                    }
                }
            }

            Spacer(Modifier.height(80.dp))
        }
    }
}

private suspend fun exportCsv(context: Context, records: List<com.accountbook.domain.model.Record>): String? {
    return withContext(Dispatchers.IO) {
        try {
            if (records.isEmpty()) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "没有可导出的数据", Toast.LENGTH_SHORT).show()
                }
                return@withContext null
            }
            
            val sortedRecords = records.sortedBy { it.getDate() }
            val firstDate = sortedRecords.first().getDate()
            val lastDate = sortedRecords.last().getDate()
            val safeFirst = firstDate.replace("-", "").substring(0, 6)
            val safeLast = lastDate.replace("-", "").substring(0, 6)
            val fileName = "${safeFirst}_to_${safeLast}.csv"

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val contentValues = ContentValues().apply {
                    put(MediaStore.Downloads.DISPLAY_NAME, fileName)
                    put(MediaStore.Downloads.MIME_TYPE, "text/csv")
                    put(MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS + "/ledger.shock")
                    put(MediaStore.Downloads.IS_PENDING, 1)
                }
                val resolver = context.contentResolver
                val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
                if (uri == null) {
                    return@withContext null
                }

                resolver.openOutputStream(uri)?.use { outputStream ->
                    val writer = outputStream.writer()
                    CsvExporter().export(sortedRecords, writer)
                    writer.flush()
                }

                contentValues.clear()
                contentValues.put(MediaStore.Downloads.IS_PENDING, 0)
                resolver.update(uri, contentValues, null, null)

                "ledger.shock/$fileName"
            } else {
                val dir = File(Environment.getExternalStorageDirectory(), "ledger.shock")
                if (!dir.exists()) dir.mkdirs()
                val file = File(dir, fileName)
                val writer = FileWriter(file)
                CsvExporter().export(sortedRecords, writer)
                writer.close()

                if (file.exists() && file.length() > 0) {
                    file.absolutePath
                } else {
                    null
                }
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "导出失败: ${e.message}", Toast.LENGTH_LONG).show()
            }
            null
        }
    }
}
