package com.bubble.probubblebopling.ui.screens.home

// VideoAnalysisScreen.kt
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
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
import com.bubble.probubblebopling.viewmodel.MarkerType
import com.bubble.probubblebopling.viewmodel.TaskViewModel
import com.bubble.probubblebopling.viewmodel.VideoAnalysis
import com.bubble.probubblebopling.viewmodel.VideoMarker

@Composable
fun VideoAnalysisScreen(
    onBack: () -> Unit,
    viewModel: TaskViewModel = viewModel()
) {
    val videoAnalyses by viewModel.videoAnalyses.observeAsState(emptyList())
    var showAddVideoDialog by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color.Black, Color(0xFF4A148C))
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Заголовок
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, "Назад", tint = Color.White)
                }

                Text(
                    text = "Видео анализ",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )

                IconButton(onClick = { showAddVideoDialog = true }) {
                    Icon(Icons.Default.Add, "Добавить видео", tint = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (videoAnalyses.isEmpty()) {
                // Состояние пустого списка
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Нет видео анализов",
                            color = Color.White.copy(alpha = 0.7f),
                            fontSize = 18.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Добавьте первое видео для анализа техники",
                            color = Color.White.copy(alpha = 0.5f),
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(videoAnalyses) { video ->
                        VideoAnalysisItem(
                            video = video,
                            onPlay = {
                                // В реальном приложении здесь будет открытие видео
                            },
                            onDelete = { viewModel.deleteVideoAnalysis(video.id) },
                            onAddMarker = { time, type, note ->
                                viewModel.addVideoMarker(video.id, time, type, note)
                            }
                        )
                    }
                }
            }
        }

        // FAB для добавления видео
        FloatingActionButton(
            onClick = { showAddVideoDialog = true },
            containerColor = Color(0xFFFF4081),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Добавить видео")
        }
    }

    // Диалог добавления видео
    if (showAddVideoDialog) {
        AddVideoDialog(
            onDismiss = { showAddVideoDialog = false },
            onAddVideo = { title, uri ->
                viewModel.addVideoAnalysis(title, uri)
                showAddVideoDialog = false
            }
        )
    }
}

@Composable
fun VideoAnalysisItem(
    video: VideoAnalysis,
    onPlay: () -> Unit,
    onDelete: () -> Unit,
    onAddMarker: (Float, MarkerType, String) -> Unit
) {
    var showAddMarkerDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0x1AFFFFFF))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Заголовок и действия
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = video.title,
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )

                Row {
                    IconButton(
                        onClick = onPlay,
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            Icons.Default.PlayArrow,
                            "Воспроизвести",
                            tint = Color.Green
                        )
                    }
                    IconButton(
                        onClick = { showAddMarkerDialog = true },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            Icons.Default.Add,
                            "Добавить маркер",
                            tint = Color(0xFFFF4081)
                        )
                    }
                    IconButton(
                        onClick = onDelete,
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            Icons.Default.Delete,
                            "Удалить",
                            tint = Color.Red
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Дата создания
            Text(
                text = "Создано: ${video.date}",
                color = Color.White.copy(alpha = 0.6f),
                fontSize = 12.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Маркеры
            if (video.markers.isNotEmpty()) {
                Text(
                    text = "Маркеры:",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))

                video.markers.forEach { marker ->
                    MarkerItem(marker = marker)
                    Spacer(modifier = Modifier.height(4.dp))
                }
            } else {
                Text(
                    text = "Нет маркеров",
                    color = Color.White.copy(alpha = 0.5f),
                    fontSize = 14.sp
                )
            }
        }
    }

    // Диалог добавления маркера
    if (showAddMarkerDialog) {
        AddMarkerDialog(
            onDismiss = { showAddMarkerDialog = false },
            onAddMarker = { time, type, note ->
                onAddMarker(time, type, note)
                showAddMarkerDialog = false
            }
        )
    }
}

@Composable
fun MarkerItem(marker: VideoMarker) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0x33FFFFFF))
            .padding(8.dp)
    ) {
        // Цветной индикатор типа маркера
        Box(
            modifier = Modifier
                .size(12.dp)
                .background(
                    when (marker.type) {
                        MarkerType.STRIKE -> Color.Yellow
                        MarkerType.SPARE -> Color.Green
                        MarkerType.ERROR -> Color.Red
                        MarkerType.TECHNIQUE -> Color.Blue
                    }
                )
                .clip(CircleShape)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "${marker.timeSeconds}сек - ${marker.type.name}",
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = marker.note,
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 12.sp
            )
        }
    }
}

@Composable
fun AddVideoDialog(
    onDismiss: () -> Unit,
    onAddVideo: (String, String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var uri by remember { mutableStateOf("content://demo/video") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Новый видео анализ",
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Название видео", color = Color.White.copy(alpha = 0.7f)) },
                    colors = TextFieldDefaults.colors(
                        /*focusedContainerColor = Color.Transparent,
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

                // В реальном приложении здесь будет выбор видео файла
                Text(
                    text = "Видео будет выбрано из галереи устройства",
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 12.sp
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank()) {
                        onAddVideo(title, uri)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF4081)),
                enabled = title.isNotBlank()
            ) {
                Text("Добавить")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Отмена", color = Color.White)
            }
        },
        containerColor = Color(0xFF2D2D2D)
    )
}

@Composable
fun AddMarkerDialog(
    onDismiss: () -> Unit,
    onAddMarker: (Float, MarkerType, String) -> Unit
) {
    var time by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf(MarkerType.STRIKE) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Добавить маркер",
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                OutlinedTextField(
                    value = time,
                    onValueChange = { time = it },
                    label = { Text("Время (секунды)", color = Color.White.copy(alpha = 0.7f)) },
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
                    value = note,
                    onValueChange = { note = it },
                    label = { Text("Заметка", color = Color.White.copy(alpha = 0.7f)) },
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

                // Выбор типа маркера
                Text(
                    text = "Тип маркера:",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    MarkerType.entries.forEach { type ->
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
                                    MarkerType.STRIKE -> "Страйк"
                                    MarkerType.SPARE -> "Спэр"
                                    MarkerType.ERROR -> "Ошибка"
                                    MarkerType.TECHNIQUE -> "Техника"
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
                    val timeValue = time.toFloatOrNull() ?: 0f
                    if (timeValue > 0 && note.isNotBlank()) {
                        onAddMarker(timeValue, selectedType, note)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF4081)),
                enabled = time.toFloatOrNull() ?: 0f > 0 && note.isNotBlank()
            ) {
                Text("Добавить")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Отмена", color = Color.White)
            }
        },
        containerColor = Color(0xFF2D2D2D)
    )
}