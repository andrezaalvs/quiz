package com.example.quiz.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.example.quiz.viewmodel.QuizViewModel

@Composable
fun CategoryScreen(navController: NavController, viewModel: QuizViewModel) {
    val allQuestions by viewModel.allQuestions.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    
    val categories = remember(allQuestions) {
        allQuestions.map { it.category }.distinct().filter { it.isNotBlank() }
    }

    LaunchedEffect(Unit) {
        viewModel.loadQuestions()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(Color(0xFF7C3AED), Color(0xFF9333EA), Color(0xFFC026D3))
                )
            )
    ) {
        AnimatedBackgroundBlobs()
        FloatingIcons()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .zIndex(10f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))
            
            Text(
                text = "Escolha um Tema",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = "Selecione uma categoria para começar",
                fontSize = 16.sp,
                color = Color(0xFFE9D5FF)
            )

            Spacer(modifier = Modifier.height(32.dp))

            when {
                isLoading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = Color.White)
                    }
                }
                categories.isEmpty() -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Nenhum tema encontrado.", color = Color.White)
                    }
                }
                else -> {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(8.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(categories) { category ->
                            val icon = getCategoryIcon(category)
                            Card(
                                onClick = { 
                                    viewModel.selectCategory(category)
                                    navController.navigate("quiz") 
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(150.dp),
                                shape = RoundedCornerShape(24.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.15f)),
                                elevation = CardDefaults.cardElevation(8.dp)
                            ) {
                                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Icon(
                                            imageVector = icon,
                                            contentDescription = null,
                                            tint = Color(0xFFFBBF24),
                                            modifier = Modifier.size(48.dp)
                                        )
                                        Spacer(Modifier.height(12.dp))
                                        Text(
                                            text = category,
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight.Bold,
                                            textAlign = TextAlign.Center,
                                            color = Color.White
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

fun getCategoryIcon(category: String): ImageVector {
    val cat = category.lowercase().trim()
    return when {
        cat.contains("geografia") -> Icons.Default.Public
        cat.contains("cinema") -> Icons.Default.Movie
        cat.contains("olimpi") || cat.contains("olímpia") -> Icons.Default.EmojiEvents
        cat.contains("historia") || cat.contains("história") -> Icons.Default.HistoryEdu
        cat.contains("esporte") -> Icons.Default.SportsBasketball
        else -> Icons.Default.Category
    }
}

@Composable
private fun AnimatedBackgroundBlobs() {
    val infiniteTransition = rememberInfiniteTransition(label = "")
    val scale by infiniteTransition.animateFloat(1f, 1.2f, infiniteRepeatable(tween(8000), RepeatMode.Reverse), label = "")
    Box(modifier = Modifier.fillMaxSize()) {
        Box(Modifier.offset(x = (-100).dp, y = (-100).dp).size(350.dp).scale(scale).background(Color(0xFFFBBF24).copy(0.3f), CircleShape).blur(70.dp))
        Box(Modifier.align(Alignment.BottomEnd).offset(x = 100.dp, y = 100.dp).size(350.dp).scale(scale).background(Color(0xFFC026D3).copy(0.3f), CircleShape).blur(70.dp))
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
