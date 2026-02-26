package com.example.quiz.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.quiz.viewmodel.QuizViewModel

@Composable
fun RankingScreen(viewModel: QuizViewModel) {
    val ranking by viewModel.ranking.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadRanking()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.linearGradient(colors = listOf(Color(0xFF7C3AED), Color(0xFF9333EA), Color(0xFFC026D3))))
    ) {
        AnimatedBackgroundBlobs()
        FloatingIcons()

        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp).zIndex(10f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))
            Text("Ranking Global", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = Color.White)
            Text("Os mestres do Quiz!", fontSize = 16.sp, color = Color(0xFFE9D5FF))

            Spacer(modifier = Modifier.height(32.dp))

            if (ranking.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Color.White)
                }
            } else {
                // PÓDIO 3D REFINADO
                Row(
                    modifier = Modifier.fillMaxWidth().height(240.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.Bottom
                ) {
                    if (ranking.size > 1) {
                        PodiumItem(ranking[1].userName, "${ranking[1].score} pts", "🥈", Color(0xFFC0C0C0), 120.dp)
                    }
                    Spacer(Modifier.width(8.dp))
                    if (ranking.isNotEmpty()) {
                        PodiumItem(ranking[0].userName, "${ranking[0].score} pts", "👑", Color(0xFFFFD700), 160.dp, isWinner = true)
                    }
                    Spacer(Modifier.width(8.dp))
                    if (ranking.size > 2) {
                        PodiumItem(ranking[2].userName, "${ranking[2].score} pts", "🥉", Color(0xFFCD7F32), 100.dp)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Lista Simplificada
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(bottom = 24.dp)
                ) {
                    itemsIndexed(ranking) { index, result ->
                        if (index > 2) {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.1f))
                            ) {
                                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                                    Text("${index + 1}º", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White, modifier = Modifier.width(40.dp))
                                    Text(result.userName, color = Color.White, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                                    Text("${result.score} pts", fontWeight = FontWeight.ExtraBold, color = Color(0xFFFBBF24))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PodiumItem(name: String, score: String, emoji: String, color: Color, height: androidx.compose.ui.unit.Dp, isWinner: Boolean = false) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Bottom) {
        Text(emoji, fontSize = if (isWinner) 44.sp else 32.sp)
        Spacer(Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .width(105.dp)
                .height(height)
                .background(color.copy(alpha = 0.3f), RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                .padding(8.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(name.take(10), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp, maxLines = 1)
                Text(score, color = Color(0xFFFDE047), fontWeight = FontWeight.ExtraBold, fontSize = 13.sp)
            }
        }
    }
}

@Composable
private fun AnimatedBackgroundBlobs() {
    val infiniteTransition = rememberInfiniteTransition(label = "")
    val scaleVal by infiniteTransition.animateFloat(1f, 1.2f, infiniteRepeatable(tween(8000), RepeatMode.Reverse), label = "")
    Box(modifier = Modifier.fillMaxSize()) {
        Box(Modifier.offset(x = (-100).dp, y = (-100).dp).size(350.dp).scale(scaleVal).background(Color(0xFFFBBF24).copy(0.1f), CircleShape).blur(70.dp))
        Box(Modifier.align(Alignment.BottomEnd).offset(x = 100.dp, y = 100.dp).size(350.dp).scale(scaleVal).background(Color(0xFFC026D3).copy(0.1f), CircleShape).blur(70.dp))
    }
}

@Composable
private fun FloatingIcons() {
    val icons = listOf(Icons.Default.Psychology, Icons.Default.EmojiObjects, Icons.Default.Bolt)
    Box(modifier = Modifier.fillMaxSize()) {
        icons.forEachIndexed { i, icon ->
            val infiniteTransition = rememberInfiniteTransition(label = "")
            val yOffset by infiniteTransition.animateFloat(0f, -40f, infiniteRepeatable(tween(3000 + i * 1000), RepeatMode.Reverse), label = "")
            Icon(icon, null, tint = Color.White.copy(0.1f), modifier = Modifier.offset(x = (50 + i * 120).dp, y = (150 + i * 200 + yOffset).dp).size(60.dp))
        }
    }
}
