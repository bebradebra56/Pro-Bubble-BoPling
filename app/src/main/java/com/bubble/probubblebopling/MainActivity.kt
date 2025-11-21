package com.bubble.probubblebopling

import android.content.res.Resources
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import com.bubble.probubblebopling.navigation.NavGraph
import com.bubble.probubblebopling.ui.BubbleOrbitApp
import com.bubble.probubblebopling.ui.components.CosmicBottomNavigation
import com.bubble.probubblebopling.ui.theme.ChickenTheme
import com.bubble.probubblebopling.viewmodel.TaskViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: com.bubble.probubblebopling.viewmodel.TaskViewModel by viewModels()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Enable edge-to-edge display
        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, false)
        
        setContent {
            //ChickenApp(viewModel = viewModel)
            _root_ide_package_.com.bubble.probubblebopling.ui.BubbleOrbitApp(viewModel)
        }
    }
}

@Composable
fun ChickenApp(viewModel: com.bubble.probubblebopling.viewmodel.TaskViewModel) {
    val navController = rememberNavController()
    //val currentTheme by viewModel.theme.collectAsState()

    _root_ide_package_.com.bubble.probubblebopling.ui.theme.ChickenTheme(darkTheme = true) {
        _root_ide_package_.com.bubble.probubblebopling.ui.components.CosmicBottomNavigation(
            navController = navController,
            viewModel = viewModel
        )
    }
}
