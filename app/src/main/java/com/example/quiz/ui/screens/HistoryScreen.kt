package com.example.quiz.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quiz.viewmodel.QuizViewModel
import com.example.quiz.model.QuizResult
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HistoryScreen(viewModel: QuizViewModel) {
    val history by viewModel.history.collectAsState()
    val fullHistory by viewModel.fullHistory.collectAsState()
    
    val sdf = SimpleDateFormat("dd MMM yyyy", Locale("pt", "BR"))
    val stf = SimpleDateFormat("HH:mm", Locale("pt", "BR"))

    LaunchedEffect(Unit) {
        viewModel.loadHistory()
    }

    val totalQuizzes = fullHistory.size
    val totalCorrect = fullHistory.sumOf { it.score }
    val totalPossibleQuestions = fullHistory.sumOf { it.totalQuestions }
    val totalIncorrect = totalPossibleQuestions - totalCorrect
    val avgAccuracy = if (totalPossibleQuestions > 0) (totalCorrect.toFloat() / totalPossibleQuestions * 100).toInt() else 0

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(colors = listOf(Color(0xFF6A11CB), Color(0xFF2575FC))))
    ) {
        AnimatedBackgroundBlobs()

        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Spacer(modifier = Modifier.height(40.dp))
                
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.TopEnd) {
                    IconButton(onClick = { viewModel.clearAllHistory() }) {
                        Icon(Icons.Default.DeleteSweep, "Limpar tudo", tint = Color.White.copy(0.7f))
                    }
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(32.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.15f))
                ) {
                    Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(modifier = Modifier.size(64.dp).background(Color(0xFFFBBF24), CircleShape), contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.EmojiEvents, null, tint = Color.White, modifier = Modifier.size(32.dp))
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Estatísticas Gerais 👏", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        Text("Seu progresso acumulado", color = Color.White.copy(0.7f), fontSize = 14.sp)
                        Spacer(modifier = Modifier.height(32.dp))
                        Box(contentAlignment = Alignment.Center) {
                            CircularProgressPainter(avgAccuracy.toFloat() / 100f)
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("$avgAccuracy%", fontSize = 36.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                                Text("Acurácia", fontSize = 12.sp, color = Color.White.copy(0.7f))
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    StatBox(modifier = Modifier.weight(1f), label = "Total Pontos", value = "$totalCorrect", icon = Icons.Default.Adjust, color = Color(0xFFFBBF24))
                    StatBox(modifier = Modifier.weight(1f), label = "Corretas", value = "$totalCorrect", icon = Icons.Default.CheckCircle, color = Color(0xFF4ADE80))
                }
                Spacer(modifier = Modifier.height(12.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    StatBox(modifier = Modifier.weight(1f), label = "Incorretas", value = "$totalIncorrect", icon = Icons.Default.Cancel, color = Color(0xFFF87171))
                    StatBox(modifier = Modifier.weight(1f), label = "Total Quizzes", value = "$totalQuizzes", icon = Icons.Default.History, color = Color(0xFF60A5FA))
                }

                Spacer(modifier = Modifier.height(32.dp))
                Text("Últimas 15 Partidas", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Start, color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))
            }

            items(history) { result ->
                val accuracy = if(result.totalQuestions > 0) (result.score.toFloat() / result.totalQuestions * 100).toInt() else 0
                val timeFormatted = formatTime(result.timeTakenSeconds)
                
                Card(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.1f))
                ) {
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(48.dp).background(Color(0xFFFBBF24).copy(0.2f), RoundedCornerShape(12.dp)), contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.EmojiEvents, null, tint = Color(0xFFFBBF24), modifier = Modifier.size(24.dp))
                        }
                        
                        Spacer(modifier = Modifier.width(16.dp))
                        
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = "Tema: ${result.category}", color = Color(0xFFFDE047), fontWeight = FontWeight.ExtraBold, fontSize = 14.sp)
                            Text(text = "Pontuação: ${result.score}/${result.totalQuestions}", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.CalendarToday, null, tint = Color.White.copy(0.5f), modifier = Modifier.size(12.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(sdf.format(Date(result.timestamp)), color = Color.White.copy(0.5f), fontSize = 11.sp)
                                
                                Spacer(modifier = Modifier.width(8.dp))
                                
                                Icon(Icons.Default.AccessTime, null, tint = Color.White.copy(0.5f), modifier = Modifier.size(12.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(stf.format(Date(result.timestamp)), color = Color.White.copy(0.5f), fontSize = 11.sp)
                                
                                Spacer(modifier = Modifier.width(8.dp))
                                
                                Icon(Icons.Default.Timer, null, tint = Color.White.copy(0.5f), modifier = Modifier.size(12.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(timeFormatted, color = Color.White.copy(0.5f), fontSize = 11.sp)
                            }
                        }
                        
                        Box(modifier = Modifier.size(40.dp).background(Color.White.copy(0.05f), CircleShape), contentAlignment = Alignment.Center) {
                            Text("$accuracy%", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

fun formatTime(seconds: Int): String {
    val m = seconds / 60
    val s = seconds % 60
    return if (m > 0) "${m}m ${s}s" else "${s}s"
}

@Composable
fun StatBox(modifier: Modifier, label: String, value: String, icon: androidx.compose.ui.graphics.vector.ImageVector, color: Color) {
    Card(modifier = modifier.height(120.dp), shape = RoundedCornerShape(24.dp), colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.1f))) {
        Column(modifier = Modifier.padding(16.dp)) {
            Box(modifier = Modifier.size(32.dp).background(color.copy(0.2f), CircleShape), contentAlignment = Alignment.Center) {
                Icon(icon, null, tint = color, modifier = Modifier.size(18.dp))
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(value, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
            Text(label, fontSize = 12.sp, color = Color.White.copy(0.6f))
        }
    }
}

@Composable
fun CircularProgressPainter(progress: Float) {
    Canvas(modifier = Modifier.size(140.dp)) {
        drawArc(color = Color.White.copy(alpha = 0.1f), startAngle = 0f, sweepAngle = 360f, useCenter = false, style = Stroke(width = 12.dp.toPx(), cap = StrokeCap.Round))
        drawArc(brush = Brush.sweepGradient(listOf(Color(0xFFFBBF24), Color(0xFFF59E0B))), startAngle = -90f, sweepAngle = 360f * progress, useCenter = false, style = Stroke(width = 12.dp.toPx(), cap = StrokeCap.Round))
    }
}

@Composable
private fun AnimatedBackgroundBlobs() {
    val infiniteTransition = rememberInfiniteTransition(label = "")
    val scaleValue by infiniteTransition.animateFloat(1f, 1.2f, infiniteRepeatable(tween(8000), RepeatMode.Reverse), label = "")
    Box(modifier = Modifier.fillMaxSize()) {
        Box(Modifier.offset(x = (-100).dp, y = (-100).dp).size(350.dp).scale(scaleValue).background(Color(0xFFFBBF24).copy(0.1f), CircleShape).blur(70.dp))
        Box(Modifier.align(Alignment.BottomEnd).offset(x = 100.dp, y = 100.dp).size(350.dp).scale(scaleValue).background(Color(0xFFC026D3).copy(0.1f), CircleShape).blur(70.dp))
    }
}
