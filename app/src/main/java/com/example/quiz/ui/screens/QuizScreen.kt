package com.example.quiz.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.example.quiz.viewmodel.QuizViewModel

@Composable
fun QuizScreen(viewModel: QuizViewModel, navController: NavController) {
    val questions by viewModel.questions.collectAsState()
    val currentIndex by viewModel.currentIndex.collectAsState()
    val timer by viewModel.timer.collectAsState()
    val score by viewModel.score.collectAsState()
    val isFinished by viewModel.isFinished.collectAsState()
    val feedbackMessage by viewModel.feedbackMessage.collectAsState()
    val isCorrect by viewModel.isCorrect.collectAsState()
    
    // Observa qual botão o usuário clicou
    val userSelection by viewModel.userSelection.collectAsState()

    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.linearGradient(colors = listOf(Color(0xFF7C3AED), Color(0xFF9333EA), Color(0xFFC026D3))))
    ) {
        AnimatedBackgroundBlobs()
        FloatingIcons()

        if (isFinished) {
            ResultContent(score, questions.size, navController)
        } else if (questions.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color.White)
            }
        } else {
            val currentQuestion = questions[currentIndex]
            
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp)
                    .zIndex(10f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(40.dp))
                
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Pergunta ${currentIndex + 1}/${questions.size}", color = Color.White, fontWeight = FontWeight.Bold)
                    Text("Tempo: $timer", color = if (timer < 10) Color(0xFFFF5252) else Color.White, fontWeight = FontWeight.ExtraBold)
                }

                LinearProgressIndicator(
                    progress = { (currentIndex + 1).toFloat() / questions.size },
                    modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp).height(8.dp),
                    color = Color(0xFFFBBF24),
                    trackColor = Color.White.copy(0.2f)
                )

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.1f))
                ) {
                    Column(
                        modifier = Modifier
                            .padding(24.dp)
                            .fillMaxSize()
                            .verticalScroll(scrollState),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = currentQuestion.text,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )
                        
                        Spacer(Modifier.height(32.dp))

                        currentQuestion.options.forEachIndexed { index, option ->
                            val isSelectedByThisButton = userSelection == index + 1
                            val isFeedbackActive = feedbackMessage != null
                            
                            Button(
                                onClick = { if (!isFeedbackActive) viewModel.submitAnswer(index + 1) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                                    .heightIn(min = 56.dp),
                                shape = RoundedCornerShape(16.dp),
                                enabled = !isFeedbackActive,
                                colors = ButtonDefaults.buttonColors(
                                    // DESTAQUE: O botão que o usuário CLICOU fica Ciano Elétrico
                                    containerColor = when {
                                        isSelectedByThisButton -> Color(0xFF22D3EE) 
                                        else -> Color.White.copy(0.1f)
                                    },
                                    contentColor = when {
                                        isSelectedByThisButton -> Color.Black
                                        else -> Color.White
                                    },
                                    disabledContainerColor = when {
                                        isSelectedByThisButton -> Color(0xFF22D3EE)
                                        else -> Color.White.copy(0.05f)
                                    },
                                    disabledContentColor = when {
                                        isSelectedByThisButton -> Color.Black
                                        else -> Color.White.copy(0.5f)
                                    }
                                )
                            ) {
                                Text(text = option, fontSize = 16.sp, textAlign = TextAlign.Center)
                            }
                        }
                    }
                }

                feedbackMessage?.let { message ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isCorrect == true) Color(0xFF4CAF50) else Color(0xFFE53935)
                        ),
                        elevation = CardDefaults.cardElevation(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp).fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = if (isCorrect == true) "✅ $message" else "😟 $message",
                                textAlign = TextAlign.Center,
                                color = Color.White,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 16.sp
                            )
                        }
                    }
                } ?: Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun ResultContent(score: Int, total: Int, navController: NavController) {
    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Icon(Icons.Default.Stars, null, tint = Color(0xFFFBBF24), modifier = Modifier.size(100.dp))
        Text("Quiz Terminado!", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = Color.White)
        Text("Sua pontuação: $score / $total", fontSize = 24.sp, color = Color(0xFFE9D5FF))
        Spacer(Modifier.height(32.dp))
        Button(onClick = { navController.navigate("dashboard") { popUpTo(0) } }, shape = RoundedCornerShape(16.dp)) {
            Text("Voltar ao Início")
        }
    }
}

@Composable
private fun AnimatedBackgroundBlobs() {
    val infiniteTransition = rememberInfiniteTransition(label = "")
    val scaleVal by infiniteTransition.animateFloat(1f, 1.2f, infiniteRepeatable(tween(8000), RepeatMode.Reverse), label = "")
    Box(modifier = Modifier.fillMaxSize()) {
        Box(Modifier.offset(x = (-100).dp, y = (-100).dp).size(350.dp).scale(scaleVal).background(Color(0xFFFBBF24).copy(0.1f), RoundedCornerShape(50)).blur(70.dp))
        Box(Modifier.align(Alignment.BottomEnd).offset(x = 100.dp, y = 100.dp).size(350.dp).scale(scaleVal).background(Color(0xFFC026D3).copy(0.1f), RoundedCornerShape(50)).blur(70.dp))
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
