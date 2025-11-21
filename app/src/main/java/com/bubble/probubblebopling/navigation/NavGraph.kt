package com.bubble.probubblebopling.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.bubble.probubblebopling.ui.components.CosmicBottomNavigation
import com.bubble.probubblebopling.ui.screens.home.HomeScreen
import com.bubble.probubblebopling.viewmodel.TaskViewModel
import com.bubble.probubblebopling.ui.components.CosmicBottomNavigation

@Composable
fun NavGraph(
    navController: NavHostController,
    viewModel: TaskViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            /*HomeScreen(
                viewModel = viewModel,
                onNavigateToAddTask = {
                    navController.navigate(Screen.AddTask.route)
                },
                onNavigateToTaskDetail = { taskId ->
                    navController.navigate(Screen.TaskDetail.createRoute(taskId))
                },
                onNavigateToStatistics = {
                    navController.navigate(Screen.Statistics.route)
                },
                onNavigateToProfile = {
                    navController.navigate(Screen.Profile.route)
                }
            )*/
            var currentScreen by remember { mutableStateOf(ScreenEnum.DASHBOARD) }

            Scaffold(
                bottomBar = {
                    CosmicBottomNavigation(navController = navController, viewModel)
                }
            ) { paddingValues ->
                BubbleOrbitNavHost(
                    navController = navController,
                    modifier = Modifier.padding(paddingValues),
                    viewModel = viewModel
                )
            }
        }
        
        composable(Screen.AddTask.route) {

        }
        
        composable(
            route = Screen.EditTask.route,
            arguments = listOf(
                navArgument("taskId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val taskId = backStackEntry.arguments?.getInt("taskId") ?: return@composable

        }
        
        composable(
            route = Screen.TaskDetail.route,
            arguments = listOf(
                navArgument("taskId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val taskId = backStackEntry.arguments?.getInt("taskId") ?: return@composable

        }
        
        composable(Screen.Statistics.route) {

        }
        
        composable(Screen.Profile.route) {

        }
        
        composable(Screen.About.route) {

        }
    }
}

