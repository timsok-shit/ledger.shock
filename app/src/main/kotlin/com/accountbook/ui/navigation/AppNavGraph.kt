package com.accountbook.ui.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.accountbook.ui.screen.HomeScreen
import com.accountbook.ui.screen.SettingsScreen
import com.accountbook.ui.screen.StatsScreen
import com.accountbook.ui.theme.Black
import com.accountbook.ui.theme.White
import com.accountbook.ui.theme.Yellow
import com.accountbook.viewmodel.CategoryViewModel
import com.accountbook.viewmodel.RecordViewModel
import com.accountbook.viewmodel.StatsViewModel

private val routeOrder = listOf("home", "stats", "settings")

@Composable
fun AppNavGraph(
    recordViewModel: RecordViewModel,
    categoryViewModel: CategoryViewModel,
    statsViewModel: StatsViewModel
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    var previousRoute by remember { mutableStateOf("home") }

    val currentRoute = currentDestination?.route ?: "home"
    val direction = if (routeOrder.indexOf(currentRoute) > routeOrder.indexOf(previousRoute)) 1 else -1

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Black,
        bottomBar = {
            NavigationBar(
                containerColor = Black,
                contentColor = Yellow
            ) {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Book, contentDescription = null) },
                    label = { Text("账本", fontWeight = FontWeight.Black, fontSize = 10.sp) },
                    selected = currentDestination?.hierarchy?.any { it.route == "home" } == true,
                    onClick = {
                        previousRoute = currentRoute
                        navController.navigate("home") {
                            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Black,
                        selectedTextColor = Black,
                        indicatorColor = Yellow,
                        unselectedIconColor = White.copy(alpha = 0.5f),
                        unselectedTextColor = White.copy(alpha = 0.5f)
                    )
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.BarChart, contentDescription = null) },
                    label = { Text("统计", fontWeight = FontWeight.Black, fontSize = 10.sp) },
                    selected = currentDestination?.hierarchy?.any { it.route == "stats" } == true,
                    onClick = {
                        previousRoute = currentRoute
                        navController.navigate("stats") {
                            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Black,
                        selectedTextColor = Black,
                        indicatorColor = Yellow,
                        unselectedIconColor = White.copy(alpha = 0.5f),
                        unselectedTextColor = White.copy(alpha = 0.5f)
                    )
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Settings, contentDescription = null) },
                    label = { Text("设置", fontWeight = FontWeight.Black, fontSize = 10.sp) },
                    selected = currentDestination?.hierarchy?.any { it.route == "settings" } == true,
                    onClick = {
                        previousRoute = currentRoute
                        navController.navigate("settings") {
                            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Black,
                        selectedTextColor = Black,
                        indicatorColor = Yellow,
                        unselectedIconColor = White.copy(alpha = 0.5f),
                        unselectedTextColor = White.copy(alpha = 0.5f)
                    )
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier
                .fillMaxSize()
                .background(Black)
                .padding(innerPadding),
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { it * direction },
                    animationSpec = tween(350)
                ) + fadeIn(animationSpec = tween(350, delayMillis = 50))
            },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { -it * direction / 3 },
                    animationSpec = tween(350)
                ) + fadeOut(animationSpec = tween(350))
            },
            popEnterTransition = {
                slideInHorizontally(
                    initialOffsetX = { -it * direction / 2 },
                    animationSpec = tween(300)
                ) + fadeIn(animationSpec = tween(300))
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { it * direction },
                    animationSpec = tween(300)
                ) + fadeOut(animationSpec = tween(300))
            }
        ) {
            composable("home") {
                HomeScreen(
                    recordViewModel = recordViewModel,
                    categoryViewModel = categoryViewModel
                )
            }
            composable("stats") {
                StatsScreen(statsViewModel = statsViewModel)
            }
            composable("settings") {
                SettingsScreen(recordViewModel = recordViewModel)
            }
        }
    }
}
