package com.bubble.probubblebopling.ui.screens.home

// HomeScreen.kt
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Adjust
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bubble.probubblebopling.viewmodel.Statistics
import com.bubble.probubblebopling.viewmodel.TaskViewModel
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.ui.platform.LocalContext
import com.bubble.probubblebopling.navigation.Screen
import com.bubble.probubblebopling.viewmodel.Game

@Composable
fun HomeScreen(
    viewModel: TaskViewModel = viewModel()
) {
    val statistics by viewModel.statistics.observeAsState(Statistics())
    val currentGame by viewModel.currentGame.observeAsState(Game())
    val errorMessage by viewModel.errorMessage.observeAsState()

    // States for dialogs
    var showQuickGameDialog by remember { mutableStateOf(false) }

    // Error handling
    LaunchedEffect(errorMessage) {
        errorMessage?.let { message ->
            println("HomeScreen Error: $message")
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color.Black, Color(0xFF4A148C))
                )
            )
            .verticalScroll(rememberScrollState())
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // App Title
            Text(
                text = "Pro Bubble BoPling",
                style = MaterialTheme.typography.headlineLarge,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 40.dp)
            )

            // Main ball with statistics - CENTERED
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(200.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFFF4081))
                    .padding(16.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Average Score",
                        color = Color.White,
                        fontSize = 16.sp
                    )
                    Text(
                        text = String.format("%.1f", statistics.averageScore),
                        color = Color.White,
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${statistics.gamesPlayed} games",
                        color = Color.White,
                        fontSize = 14.sp
                    )

                    // Progress bar inside the ball
                    if (statistics.gamesPlayed > 0) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.6f)
                                .height(4.dp)
                                .background(Color.White.copy(alpha = 0.3f))
                                .clip(CircleShape)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth((statistics.averageScore / 300).toFloat())
                                    .height(4.dp)
                                    .background(Color.Yellow)
                                    .clip(CircleShape)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Quick statistics - CENTERED
            QuickStatsRow(statistics = statistics)

            Spacer(modifier = Modifier.height(32.dp))

            // Current active game - CENTERED
            if (currentGame.frames.any { it.roll1 > 0 || it.roll2 > 0 }) {
                CurrentGameCard(
                    game = currentGame,
                    onContinue = { /* No navigation - only demo */ },
                    onNewGame = { viewModel.clearCurrentGame() }
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            PrivacyPolicyButton()
            Spacer(modifier = Modifier.height(50.dp))
        }
        // FAB for DEMO games only - FIXED CLICK
        FloatingActionButton(
            onClick = {
                if (currentGame.frames.any { it.roll1 > 0 || it.roll2 > 0 }) {
                    // Continue current demo game - just add more rolls
                    addDemoRollsToCurrentGame(viewModel, currentGame)
                } else {
                    showQuickGameDialog = true // Start new demo game
                }
            },
            containerColor = Color(0xFFFF4081),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(8.dp)
        ) {
            Icon(
                if (currentGame.frames.any { it.roll1 > 0 || it.roll2 > 0 })
                    Icons.Default.PlayArrow
                else
                    Icons.Default.PlayArrow,
                contentDescription = "Demo Game"
            )
        }
    }

    // QUICK GAME DIALOG - DEMO ONLY
    if (showQuickGameDialog) {
        AlertDialog(
            onDismissRequest = { showQuickGameDialog = false },
            title = {
                Text(
                    "Demo Game",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "Start a demo bowling game:",
                        color = Color.White.copy(alpha = 0.8f),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    // Only demo options - no real game
                    Button(
                        onClick = {
                            viewModel.addDemoGame()
                            showQuickGameDialog = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF4081)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Add Demo Game")
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = {
                            // Add multiple demo games
                            repeat(3) { viewModel.addDemoGame() }
                            showQuickGameDialog = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Add 3 Demo Games")
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = { showQuickGameDialog = false }
                ) {
                    Text("Cancel", color = Color.White)
                }
            },
            containerColor = Color(0xFF2D2D2D)
        )
    }
}
@Composable
fun PrivacyPolicyButton() {
    val context = LocalContext.current

    Button(
        onClick = {
            val privacyPolicyUrl = "https://probubblebopling.com/privacy-policy.html"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(privacyPolicyUrl))
            context.startActivity(intent)
        },
        modifier = Modifier.fillMaxWidth(0.8f).padding(16.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Icon(
            imageVector = Icons.Default.PrivacyTip,
            contentDescription = "Privacy Policy",
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.size(2.dp))
        Text("Privacy Policy")
    }
}
// Helper function to add demo rolls to current game
private fun addDemoRollsToCurrentGame(viewModel: TaskViewModel, currentGame: Game) {
    val frames = currentGame.frames.toMutableList()
    val unfinishedFrameIndex = frames.indexOfFirst { !it.isComplete }

    if (unfinishedFrameIndex != -1) {
        val frame = frames[unfinishedFrameIndex]
        val pins = if (frame.roll1 == 0) {
            (6..10).random() // First roll
        } else {
            (0..(10 - frame.roll1)).random() // Second roll
        }

        val rollNumber = if (frame.roll1 == 0) 1 else 2
        viewModel.addRoll(unfinishedFrameIndex, pins, rollNumber)
    }
}

@Composable
fun QuickStatsRow(statistics: Statistics) {
    Card(
        modifier = Modifier.fillMaxWidth(0.8f),
        colors = CardDefaults.cardColors(containerColor = Color(0x1AFFFFFF))
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Statistics",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = Modifier.fillMaxWidth()
            ) {
                QuickStatItem("Best", statistics.bestGame.toString())
                QuickStatItem("Strikes", String.format("%.1f%%", statistics.strikePercentage))
                QuickStatItem("Spares", String.format("%.1f%%", statistics.sparePercentage))
            }
        }
    }
}

@Composable
fun QuickStatItem(label: String, value: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = label,
            color = Color.White.copy(alpha = 0.7f),
            fontSize = 12.sp
        )
    }
}

@Composable
fun CurrentGameCard(
    game: Game,
    onContinue: () -> Unit,
    onNewGame: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(0.8f),
        colors = CardDefaults.cardColors(containerColor = Color(0x33FF4081))
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Demo Game in Progress",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Score: ${game.totalScore}",
                color = Color.White,
                fontSize = 14.sp
            )
            Text(
                text = "Frames: ${game.frames.count { it.roll1 > 0 }}/10",
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 12.sp
            )
        }
    }
}