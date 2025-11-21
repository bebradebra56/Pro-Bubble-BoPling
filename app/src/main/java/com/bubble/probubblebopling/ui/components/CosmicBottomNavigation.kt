package com.bubble.probubblebopling.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.bubble.probubblebopling.navigation.BubbleOrbitNavHost
import com.bubble.probubblebopling.navigation.Destination
import com.bubble.probubblebopling.ui.theme.CosmicColors
import com.bubble.probubblebopling.viewmodel.TaskViewModel

@Composable
fun CosmicBottomNavigation(navController: NavHostController, viewModel: com.bubble.probubblebopling.viewmodel.TaskViewModel) {
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route

    val items = listOf(
        NavItem("Home", Icons.Default.Home, Destination.Home.route),
        NavItem("Game", Icons.Default.SportsHandball, Destination.Goals.route),
        NavItem("Stats", Icons.Default.Assessment, Destination.Statistics.route),
        NavItem("Goals", Icons.Default.GolfCourse, Destination.Profile.route)
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                items.forEach { item ->
                    NavigationBarItem(
                        icon = {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.label
                            )
                        },
                        label = {
                            Text(text = item.label)
                        },
                        selected = currentRoute == item.route,
                        onClick = {
                            navController.navigate(item.route) {
                                launchSingleTop = true
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                restoreState = true
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = _root_ide_package_.com.bubble.probubblebopling.ui.theme.CosmicColors.NeonPink,
                            selectedTextColor = _root_ide_package_.com.bubble.probubblebopling.ui.theme.CosmicColors.NeonPink,
                            unselectedIconColor = _root_ide_package_.com.bubble.probubblebopling.ui.theme.CosmicColors.LightGray,
                            unselectedTextColor = _root_ide_package_.com.bubble.probubblebopling.ui.theme.CosmicColors.LightGray,
                            indicatorColor = _root_ide_package_.com.bubble.probubblebopling.ui.theme.CosmicColors.SpaceMiddle.copy(alpha = 0.9f)
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        BubbleOrbitNavHost(
            navController = navController,
            modifier = Modifier.padding(innerPadding),
            viewModel = viewModel
        )
    }

    /*Scaffold(
        bottomBar = {
            Surface(
                color = CosmicColors.SpaceMiddle.copy(alpha = 0.9f)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(70.dp)
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    items.forEach { item ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .weight(1f)
                                .clickable {
                                    navController.navigate(item.route) {
                                        launchSingleTop = true
                                    }
                                }
                                .padding(8.dp)
                        ) {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.label,
                                tint = if (currentRoute == item.route) CosmicColors.NeonPink else CosmicColors.LightGray
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = item.label,
                                color = if (currentRoute == item.route) CosmicColors.NeonPink else CosmicColors.LightGray,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        // Просто используем ваш NavHost с отступами от Scaffold
        BubbleOrbitNavHost(
            navController = navController as NavHostController,
            modifier = Modifier.padding(innerPadding),
            viewModel = viewModel
        )
    }*/

    /*Surface(
        color = CosmicColors.SpaceMiddle.copy(alpha = 0.9f)
        //elevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEach { item ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .weight(1f)
                        .clickable {
                            navController.navigate(item.route) {
                                launchSingleTop = true
                            }
                        }
                        .padding(8.dp)
                ) {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label,
                        tint = if (currentRoute == item.route) CosmicColors.NeonPink else CosmicColors.LightGray
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = item.label,
                        color = if (currentRoute == item.route) CosmicColors.NeonPink else CosmicColors.LightGray,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }*/
}

data class NavItem(
    val label: String,
    val icon: ImageVector,
    val route: String
)