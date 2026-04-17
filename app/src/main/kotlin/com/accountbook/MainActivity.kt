package com.accountbook

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.accountbook.di.AppModule
import com.accountbook.ui.navigation.AppNavGraph
import com.accountbook.ui.theme.AccountBookTheme
import com.accountbook.ui.theme.Black
import com.accountbook.viewmodel.CategoryViewModel
import com.accountbook.viewmodel.RecordViewModel
import com.accountbook.viewmodel.StatsViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val appModule = (application as AccountApp).appModule

        val recordViewModel = RecordViewModel(appModule.recordRepository, appModule.queryEngine)
        val categoryViewModel = CategoryViewModel(appModule.categoryRepository, appModule.categoryMgr)
        val statsViewModel = StatsViewModel(appModule.recordRepository, appModule.queryEngine)

        setContent {
            AccountBookTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = Black) {
                    AppNavGraph(
                        recordViewModel = recordViewModel,
                        categoryViewModel = categoryViewModel,
                        statsViewModel = statsViewModel
                    )
                }
            }
        }
    }
}
