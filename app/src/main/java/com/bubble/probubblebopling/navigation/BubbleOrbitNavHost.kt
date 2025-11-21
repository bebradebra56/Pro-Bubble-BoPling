package com.bubble.probubblebopling.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.bubble.probubblebopling.ui.screens.home.GameScreen
import com.bubble.probubblebopling.ui.screens.home.GoalsScreen
import com.bubble.probubblebopling.ui.screens.home.HomeScreen
import com.bubble.probubblebopling.ui.screens.home.StatisticsScreen
import com.bubble.probubblebopling.ui.screens.home.VideoAnalysisScreen
import com.bubble.probubblebopling.viewmodel.TaskViewModel

@Composable
fun BubbleOrbitNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    viewModel: TaskViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Destination.Home.route,
        modifier = modifier
    ) {
        composable(Destination.Home.route) {
            HomeScreen(viewModel)
            //GameScreen({}, viewModel)
            //StatisticsScreen({}, viewModel)
            //VideoAnalysisScreen({}, viewModel)
            //GoalsScreen({}, viewModel)
        }
        composable(Destination.Goals.route) {
            GameScreen(viewModel)
        }
        composable(Destination.Statistics.route) {
            StatisticsScreen(viewModel)
        }
        composable(Destination.Profile.route) {
            GoalsScreen(viewModel)
        }
        composable(
            route = "task_detail/{taskId}",
            arguments = listOf(
                navArgument("taskId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val taskId = backStackEntry.arguments?.getString("taskId")
            //val task = viewModel.tasks.find { it.id == taskId }
            /*task?.let {

            }*/
        }
    }
}

sealed class Destination(val route: String) {
    object Home : Destination("home")
    object Goals : Destination("goals")
    object Statistics : Destination("statistics")
    object Profile : Destination("profile")
}