package com.bubble.probubblebopling.ui.screens.home

// GameScreen.kt
// GameScreen.kt
import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bubble.probubblebopling.viewmodel.Frame
import com.bubble.probubblebopling.viewmodel.Game
import com.bubble.probubblebopling.viewmodel.TaskViewModel
import androidx.compose.ui.platform.LocalConfiguration

@Composable
fun GameScreen(
    viewModel: TaskViewModel = viewModel()
) {
    val currentGame by viewModel.currentGame.observeAsState(Game())
    val errorMessage by viewModel.errorMessage.observeAsState()
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    var selectedFrame by remember { mutableStateOf<Int?>(null) }

    LaunchedEffect(errorMessage) {
        errorMessage?.let {
            // Handle errors
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
    ) {
        if (isLandscape) {

            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                ) {
                    // Header
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Bowling Game",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Total score
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color(0x1AFFFFFF))
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceAround,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(8.dp)
                        ) {
                            StatItem("Total", currentGame.totalScore.toString())
                            Spacer(modifier = Modifier.weight(0.09f))
                            StatItem("Strikes", currentGame.strikeCount.toString())
                            Spacer(modifier = Modifier.weight(0.12f))
                            StatItem("Spares", currentGame.spareCount.toString())
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))


                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(if (isLandscape) 8 else 5),
                            verticalArrangement = Arrangement.spacedBy(4.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            items(currentGame.frames) { frame ->
                                FrameItem(
                                    frame = frame,
                                    isSelected = selectedFrame == frame.number,
                                    onClick = { selectedFrame = frame.number },
                                    modifier = Modifier.size(if (isLandscape) 60.dp else 80.dp)
                                )
                            }
                        }
                    }
                }


                Column(
                    modifier = Modifier
                        .weight(0.8f)
                        .fillMaxHeight()
                        .padding(start = 16.dp)
                ) {
                    // Control buttons
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(
                            onClick = { viewModel.clearCurrentGame() },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF44336)),
                            modifier = Modifier.padding(horizontal = 4.dp)
                        ) {
                            Icon(Icons.Default.Clear, "Clear", modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Clear", fontSize = 12.sp)
                        }

                        Button(
                            onClick = { viewModel.clearCurrentGame() },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF4081)),
                            modifier = Modifier.padding(horizontal = 4.dp)
                        ) {
                            Icon(Icons.Default.Save, "Save", modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Save", fontSize = 12.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Input panel
                    if (selectedFrame != null) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            RollInputPanel(
                                frameNumber = selectedFrame!!,
                                currentFrame = currentGame.frames[selectedFrame!! - 1],
                                onRollSelected = { pins ->
                                    val frameIndex = selectedFrame!! - 1
                                    val frame = currentGame.frames[frameIndex]

                                    val rollNumber = when {
                                        frame.roll1 == 0 -> 1
                                        frame.roll2 == 0 && frame.number < 10 && !frame.isStrike -> 2
                                        frame.number == 10 && frame.roll2 == 0 -> 2
                                        frame.number == 10 && frame.roll3 == 0 -> 3
                                        else -> null
                                    }

                                    rollNumber?.let {
                                        viewModel.addRoll(frameIndex, pins, it)
                                    }

                                    if (pins == 10 && frame.number < 10 && rollNumber == 1) {
                                        selectedFrame = (selectedFrame!! % 10) + 1
                                    }
                                },
                                isLandscape = isLandscape
                            )
                        }
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = Color(0x1AFFFFFF))
                            ) {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(60.dp)
                                ) {
                                    Text(
                                        text = "Select frame to enter roll",
                                        color = Color.White.copy(alpha = 0.7f),
                                        fontSize = 14.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        } else {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Header
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Bowling Game",
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Total score
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0x1AFFFFFF))
                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        StatItem("Total Score", currentGame.totalScore.toString())
                        Spacer(modifier = Modifier.weight(0.09f))
                        StatItem("Strikes", currentGame.strikeCount.toString())
                        Spacer(modifier = Modifier.weight(0.12f))
                        StatItem("Spares", currentGame.spareCount.toString())
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Frames grid
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(5),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(20.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(currentGame.frames) { frame ->
                            FrameItem(
                                frame = frame,
                                isSelected = selectedFrame == frame.number,
                                onClick = { selectedFrame = frame.number }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Control buttons
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = { viewModel.clearCurrentGame() },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF44336)),
                        modifier = Modifier.padding(horizontal = 8.dp)
                    ) {
                        Icon(Icons.Default.Clear, "Clear")
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Clear")
                    }

                    Button(
                        onClick = { viewModel.saveCurrentGame() },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF4081)),
                        modifier = Modifier.padding(horizontal = 8.dp)
                    ) {
                        Icon(Icons.Default.Save, "Save")
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Save")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Input panel
                if (selectedFrame != null) {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        RollInputPanel(
                            frameNumber = selectedFrame!!,
                            currentFrame = currentGame.frames[selectedFrame!! - 1],
                            onRollSelected = { pins ->
                                val frameIndex = selectedFrame!! - 1
                                val frame = currentGame.frames[frameIndex]

                                val rollNumber = when {
                                    frame.roll1 == 0 -> 1
                                    frame.roll2 == 0 && frame.number < 10 && !frame.isStrike -> 2
                                    frame.number == 10 && frame.roll2 == 0 -> 2
                                    frame.number == 10 && frame.roll3 == 0 -> 3
                                    else -> null
                                }

                                rollNumber?.let {
                                    viewModel.addRoll(frameIndex, pins, it)
                                }

                                if (pins == 10 && frame.number < 10 && rollNumber == 1) {
                                    selectedFrame = (selectedFrame!! % 10) + 1
                                }
                            }
                        )
                    }
                } else {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Card(
                            modifier = Modifier.fillMaxWidth(0.8f),
                            colors = CardDefaults.cardColors(containerColor = Color(0x1AFFFFFF))
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(80.dp)
                            ) {
                                Text(
                                    text = "Select frame to enter roll",
                                    color = Color.White.copy(alpha = 0.7f),
                                    fontSize = 16.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// Rest of the GameScreen composables remain the same...
@Composable
fun FrameItem(
    frame: Frame,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (isSelected) Color(0xFFFF4081) else Color(0x33FFFFFF)

    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        modifier = modifier
            .aspectRatio(1f)
            .padding(4.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(8.dp)
        ) {
            Text(
                text = frame.number.toString(),
                color = Color.White,
                fontSize = 12.sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Display rolls
            Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                RollDisplay(
                    pins = frame.roll1,
                    isStrike = frame.isStrike,
                    isFirstRoll = true
                )
                if (frame.number < 10 || !frame.isStrike) {
                    RollDisplay(
                        pins = frame.roll2,
                        isSpare = frame.isSpare && !frame.isStrike,
                        isFirstRoll = false
                    )
                }
                if (frame.number == 10) {
                    RollDisplay(
                        pins = frame.roll3,
                        isStrike = false,
                        isFirstRoll = false
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = if (frame.frameScore > 0) frame.frameScore.toString() else "-",
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }

    }
}

@Composable
fun RollInputPanel(
    frameNumber: Int,
    currentFrame: Frame,
    onRollSelected: (Int) -> Unit,
    isLandscape: Boolean = false
) {

    val maxPins = when {
        frameNumber < 10 -> {
            when {
                currentFrame.roll1 == 0 -> 10 // First roll - up to 10 pins
                currentFrame.isStrike -> 10 // After strike in 10th frame
                else -> 10 - currentFrame.roll1 // Second roll - remaining pins
            }
        }
        else -> { // 10th frame
            when {
                currentFrame.roll1 == 0 -> 10
                currentFrame.roll2 == 0 -> {
                    if (currentFrame.isStrike) 10 else 10 - currentFrame.roll1
                }
                else -> 10 // Third roll in 10th frame always up to 10 pins
            }
        }
    }

    Card(
        modifier = Modifier.fillMaxWidth(if (isLandscape) 0.9f else 0.9f),
        colors = CardDefaults.cardColors(containerColor = Color(0x1AFFFFFF))
    ) {
        Column(
            modifier = Modifier.padding(if (isLandscape) 8.dp else 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Frame $frameNumber - Select pins",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = if (isLandscape) 14.sp else 16.sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Current frame state
            Text(
                text = when {
                    currentFrame.roll1 == 0 -> "First roll"
                    currentFrame.roll2 == 0 && frameNumber < 10 -> "Second roll"
                    frameNumber == 10 && currentFrame.roll2 == 0 -> "Second roll"
                    frameNumber == 10 && currentFrame.roll3 == 0 -> "Third roll"
                    else -> "Frame completed"
                },
                color = Color.White.copy(alpha = 0.7f),
                fontSize = if (isLandscape) 12.sp else 14.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Pin selection buttons
            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.fillMaxWidth(if (isLandscape) 0.9f else 0.8f)
            ) {
                items((0..maxPins).toList()) { pins ->
                    Button(
                        onClick = { onRollSelected(pins) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (pins == 10) Color.Yellow else Color(0xFFFF4081)
                        ),
                        modifier = Modifier.height(if (isLandscape) 40.dp else 48.dp)
                    ) {
                        Text(
                            text = when {
                                pins == 10 -> "X"
                                pins == 0 -> "-"
                                else -> pins.toString()
                            },
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = if (isLandscape) 12.sp else 14.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun RollDisplay(pins: Int, isStrike: Boolean = false, isSpare: Boolean = false, isFirstRoll: Boolean = true) {
    val text = when {
        isStrike && isFirstRoll -> "X"
        isSpare -> "/"
        pins == 0 -> "-"
        else -> pins.toString()
    }

    val color = when {
        isStrike -> Color.Yellow
        isSpare -> Color.Green
        else -> Color.White
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(20.dp)
            .background(Color(0x33FFFFFF))
            .clip(CircleShape)
    ) {
        Text(
            text = text,
            color = color,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold
        )
    }
}


@Composable
fun StatItem(label: String, value: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = label,
            color = Color.White.copy(alpha = 0.7f),
            fontSize = 12.sp
        )
        Text(
            text = value,
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }
}