package com.bubble.probubblebopling.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Egg
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector
import com.bubble.probubblebopling.R

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object AddTask : Screen("add_task")
    object EditTask : Screen("edit_task/{taskId}") {
        fun createRoute(taskId: Int) = "edit_task/$taskId"
    }
    object TaskDetail : Screen("task_detail/{taskId}") {
        fun createRoute(taskId: Int) = "task_detail/$taskId"
    }
    object Statistics : Screen("statistics")
    object Profile : Screen("profile")
    object About : Screen("about")
}

enum class ScreenEnum(
    val rout: String,
    @StringRes val titleRes: Int,
val icon: ImageVector
){
    DASHBOARD("dashboard", R.string.dashboard, Icons.Default.Home),
    EXPENSES("expenses", R.string.expenses , Icons.Default.ShoppingCart),
    EGGS("eggs", R.string.eggs,Icons.Default.Egg),
    STATISTICS("statistics", R.string.statistics, Icons.Default.BarChart)
}
