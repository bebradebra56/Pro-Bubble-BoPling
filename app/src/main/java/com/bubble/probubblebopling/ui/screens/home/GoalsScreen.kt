package com.bubble.probubblebopling.ui.screens.home

// GoalsScreen.kt
// GoalsScreen.kt
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
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
import com.bubble.probubblebopling.viewmodel.Goal
import com.bubble.probubblebopling.viewmodel.GoalType
import com.bubble.probubblebopling.viewmodel.TaskViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun GoalsScreen(
    viewModel: TaskViewModel = viewModel()
) {
    val goals by viewModel.goals.observeAsState(emptyList())
    var showAddGoalDialog by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color.Black, Color(0xFF4A148C))
                )
            )
            //.verticalScroll(rememberScrollState())
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Header - CENTERED
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "My Goals",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (goals.isEmpty()) {
                // Empty state - CENTERED
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "No goals",
                            color = Color.White.copy(alpha = 0.7f),
                            fontSize = 18.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Add your first goal to track progress",
                            color = Color.White.copy(alpha = 0.5f),
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = { showAddGoalDialog = true },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF4081))
                        ) {
                            Icon(Icons.Default.Add, "Add goal")
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Add First Goal")
                        }
                    }
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    items(goals) { goal ->
                        GoalItem(
                            goal = goal,
                            onComplete = { viewModel.completeGoal(goal.id) },
                            onDelete = { viewModel.deleteGoal(goal.id) },
                            modifier = Modifier.fillMaxWidth(0.9f)
                        )
                    }
                }
            }
        }

        // FAB for adding goal - CENTERED
        if (goals.isNotEmpty()) {
            FloatingActionButton(
                onClick = { showAddGoalDialog = true },
                containerColor = Color(0xFFFF4081),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add goal")
            }
        }
    }

    // Add goal dialog
    if (showAddGoalDialog) {
        AddGoalDialog(
            onDismiss = { showAddGoalDialog = false },
            onAddGoal = { title, target, type ->
                viewModel.addGoal(title, target, type)
                showAddGoalDialog = false
            }
        )
    }
}

@Composable
fun GoalItem(
    goal: Goal,
    onComplete: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val progress = (goal.current / goal.target).coerceIn(0.0, 1.0)
    val progressPercent = (progress * 100).toInt()

    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color(0x1AFFFFFF))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Title and actions - CENTERED
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = goal.title,
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )

                Row {
                    if (!goal.isCompleted) {
                        IconButton(
                            onClick = onComplete,
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                Icons.Default.Check,
                                "Complete",
                                tint = Color.Green
                            )
                        }
                    }
                    IconButton(
                        onClick = onDelete,
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            Icons.Default.Delete,
                            "Delete",
                            tint = Color.Red
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Progress - CENTERED
            Column {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Progress: $progressPercent%",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 14.sp
                    )
                    Text(
                        text = "${String.format("%.1f", goal.current)}/${String.format("%.1f", goal.target)}",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Progress bar
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .background(Color(0x33FFFFFF))
                        .clip(CircleShape)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(progress.toFloat())
                            .height(8.dp)
                            .background(Color(0xFFFF4081))
                            .clip(CircleShape)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Goal details - CENTERED
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = when (goal.type) {
                        GoalType.STRIKE_RATE -> "Strike Rate"
                        GoalType.AVERAGE_SCORE -> "Average Score"
                        GoalType.SPARE_RATE -> "Spare Rate"
                        GoalType.CONSECUTIVE_GAMES -> "Consecutive Games"
                    },
                    color = Color.White.copy(alpha = 0.6f),
                    fontSize = 12.sp
                )

                if (goal.isCompleted) {
                    Text(
                        text = "✓ Completed",
                        color = Color.Green,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                } else {
                    Text(
                        text = "In Progress",
                        color = Color.Yellow,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}

// AddGoalDialog remains the same but centered...
@Composable
fun AddGoalDialog(
    onDismiss: () -> Unit,
    onAddGoal: (String, Double, GoalType) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var target by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf(GoalType.AVERAGE_SCORE) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "New Goal",
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Goal title", color = Color.White.copy(alpha = 0.7f)) },
                    colors = TextFieldDefaults.colors(/*
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        cursorColor = Color.White,
                        focusedLabelColor = Color.White.copy(alpha = 0.7f),
                        unfocusedLabelColor = Color.White.copy(alpha = 0.5f),
                        focusedBorderColor = Color(0xFFFF4081),
                        unfocusedBorderColor = Color.White.copy(alpha = 0.3f)*/
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = target,
                    onValueChange = { target = it },
                    label = { Text("Target value", color = Color.White.copy(alpha = 0.7f)) },
                    colors = TextFieldDefaults.colors(/*
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        cursorColor = Color.White,
                        focusedLabelColor = Color.White.copy(alpha = 0.7f),
                        unfocusedLabelColor = Color.White.copy(alpha = 0.5f),
                        focusedBorderColor = Color(0xFFFF4081),
                        unfocusedBorderColor = Color.White.copy(alpha = 0.3f)*/
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                // Goal type selection - CENTERED
                Text(
                    text = "Goal type:",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    GoalType.entries.forEach { type ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedType = type }
                        ) {
                            RadioButton(
                                selected = selectedType == type,
                                onClick = { selectedType = type },
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = Color(0xFFFF4081),
                                    unselectedColor = Color.White
                                )
                            )
                            Text(
                                text = when (type) {
                                    GoalType.STRIKE_RATE -> "Strike Rate"
                                    GoalType.AVERAGE_SCORE -> "Average Score"
                                    GoalType.SPARE_RATE -> "Spare Rate"
                                    GoalType.CONSECUTIVE_GAMES -> "Consecutive Games >200"
                                },
                                color = Color.White,
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val targetValue = target.toDoubleOrNull() ?: 0.0
                    if (title.isNotBlank() && targetValue > 0) {
                        onAddGoal(title, targetValue, selectedType)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF4081)),
                enabled = title.isNotBlank() && target.toDoubleOrNull() ?: 0.0 > 0
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = Color.White)
            }
        },
        containerColor = Color(0xFF2D2D2D)
    )
}