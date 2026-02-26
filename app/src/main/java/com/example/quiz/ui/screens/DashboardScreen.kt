package com.example.quiz.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.example.quiz.viewmodel.AuthViewModel
import com.example.quiz.viewmodel.QuizViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun DashboardScreen(
    navController: NavController,
    authViewModel: AuthViewModel,
    quizViewModel: QuizViewModel
) {
    val user = FirebaseAuth.getInstance().currentUser

    LaunchedEffect(Unit) {
        quizViewModel.loadQuestions()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF7C3AED), // violet-600
                        Color(0xFF9333EA), // purple-600
                        Color(0xFFC026D3)  // fuchsia-600
                    )
                )
            )
    ) {
        AnimatedBackgroundBlobs()
        FloatingIcons()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .zIndex(10f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Profile Header
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(
                        color = Color.White.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(32.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Perfil",
                    tint = Color.White,
                    modifier = Modifier.size(50.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Bem-vindo!",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = user?.email ?: "Explorador",
                fontSize = 16.sp,
                color = Color(0xFFE9D5FF)
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Action Buttons
            DashboardButton(
                text = "Iniciar Quiz",
                icon = Icons.Default.PlayArrow,
                gradient = listOf(Color(0xFFFBBF24), Color(0xFFF97316), Color(0xFFEC4899)),
                onClick = { navController.navigate("categories") }
            )

            Spacer(modifier = Modifier.height(16.dp))

            DashboardButton(
                text = "Meu Histórico",
                icon = Icons.Default.History,
                containerColor = Color.White.copy(alpha = 0.1f),
                onClick = { navController.navigate("history") }
            )

            Spacer(modifier = Modifier.height(16.dp))

            DashboardButton(
                text = "Ranking Global",
                icon = Icons.Default.EmojiEvents,
                containerColor = Color.White.copy(alpha = 0.1f),
                onClick = { navController.navigate("ranking") }
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Logout Button
            TextButton(onClick = {
                authViewModel.resetState()
                FirebaseAuth.getInstance().signOut()
                navController.navigate("login") {
                    popUpTo(0) { inclusive = true }
                }
            }) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.ExitToApp,
                        contentDescription = null,
                        tint = Color(0xFFF87171),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = "Sair da Conta",
                        color = Color(0xFFF87171),
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
fun DashboardButton(
    text: String,
    icon: ImageVector,
    gradient: List<Color>? = null,
    containerColor: Color? = null,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent
        ),
        contentPadding = PaddingValues(0.dp),
        shape = RoundedCornerShape(20.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    if (gradient != null) Brush.linearGradient(gradient)
                    else Brush.linearGradient(listOf(containerColor ?: Color.Transparent, containerColor ?: Color.Transparent))
                ),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = text,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
        }
    }
}

@Composable
private fun AnimatedBackgroundBlobs() {
    val infiniteTransition = rememberInfiniteTransition(label = "")
    val scale by infiniteTransition.animateFloat(1f, 1.2f, infiniteRepeatable(tween(8000), RepeatMode.Reverse), label = "")
    Box(modifier = Modifier.fillMaxSize()) {
        Box(Modifier.offset(x = (-100).dp, y = (-100).dp).size(350.dp).scale(scale).background(Color(0xFFFBBF24).copy(0.3f), RoundedCornerShape(50)).blur(70.dp))
        Box(Modifier.align(Alignment.BottomEnd).offset(x = 100.dp, y = 100.dp).size(350.dp).scale(scale).background(Color(0xFFC026D3).copy(0.3f), RoundedCornerShape(50)).blur(70.dp))
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
