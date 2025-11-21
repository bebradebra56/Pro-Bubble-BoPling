package com.bubble.probubblebopling.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.bubble.probubblebopling.navigation.BubbleOrbitNavHost

@Composable
fun BubbleOrbitApp(viewModel: com.bubble.probubblebopling.viewmodel.TaskViewModel) {
    val navController = rememberNavController()

    _root_ide_package_.com.bubble.probubblebopling.ui.theme.BubbleOrbitTheme {
        // Временный вариант без bottom navigation - сначала проверим основную навигацию
        /*BubbleOrbitNavHost(
            navController = navController,
            modifier = Modifier,
            viewModel = viewModel
        )*/


        Column(modifier = Modifier.fillMaxSize()) {
            BubbleOrbitNavHost(
                navController = navController,
                modifier = Modifier.weight(1f),
                viewModel = viewModel
            )
            _root_ide_package_.com.bubble.probubblebopling.ui.components.CosmicBottomNavigation(
                navController = navController,
                viewModel
            )
        }
    }
}