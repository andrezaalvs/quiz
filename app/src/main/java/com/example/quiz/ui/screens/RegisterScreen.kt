package com.example.quiz.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.example.quiz.viewmodel.AuthViewModel

@Composable
fun RegisterScreen(
    authViewModel: AuthViewModel,
    navController: NavController
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    
    val loginState by authViewModel.loginState.collectAsState()

    LaunchedEffect(loginState) {
        if (loginState == true) {
            navController.navigate("dashboard") {
                popUpTo("login") { inclusive = true }
            }
        }
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
        
        Box(
            modifier = Modifier.fillMaxSize().padding(16.dp).zIndex(10f),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                // Reaproveitando a LogoSection para manter o branding
                LogoSection()
                
                Spacer(modifier = Modifier.height(32.dp))
                
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.1f)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Column(modifier = Modifier.padding(32.dp)) {
                        Text("Criar Conta", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                        Text("Junte-se à comunidade QuizMaster!", color = Color(0xFFE9D5FF), fontSize = 14.sp)
                        
                        Spacer(modifier = Modifier.height(24.dp))

                        // E-mail
                        Text("E-mail", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                        OutlinedTextField(
                            value = email, onValueChange = { email = it },
                            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                            placeholder = { Text("seu@email.com", color = Color(0xFFE9D5FF)) },
                            leadingIcon = { Icon(Icons.Default.Email, null, tint = Color(0xFFD8B4FE)) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFFFBBF24),
                                unfocusedBorderColor = Color.White.copy(alpha = 0.3f),
                                focusedTextColor = Color.White, unfocusedTextColor = Color.White
                            ),
                            shape = RoundedCornerShape(16.dp), singleLine = true
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        // Senha
                        Text("Senha", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                        OutlinedTextField(
                            value = password, onValueChange = { password = it },
                            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            leadingIcon = { Icon(Icons.Default.Lock, null, tint = Color(0xFFD8B4FE)) },
                            trailingIcon = {
                                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                    Icon(if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff, null, tint = Color.White.copy(alpha = 0.5f))
                                }
                            },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFFFBBF24),
                                unfocusedBorderColor = Color.White.copy(alpha = 0.3f),
                                focusedTextColor = Color.White, unfocusedTextColor = Color.White
                            ),
                            shape = RoundedCornerShape(16.dp), singleLine = true
                        )

                        if (loginState == false) {
                            Text("Erro ao criar conta. Tente outro e-mail.", color = Color(0xFFF87171), fontSize = 12.sp, modifier = Modifier.padding(top = 8.dp))
                        }

                        Spacer(modifier = Modifier.height(32.dp))

                        // Botão de Cadastrar
                        Button(
                            onClick = { authViewModel.register(email, password) },
                            modifier = Modifier.fillMaxWidth().height(56.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                            contentPadding = PaddingValues(0.dp),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Box(modifier = Modifier.fillMaxSize().background(Brush.linearGradient(listOf(Color(0xFF22D3EE), Color(0xFF0EA5E9), Color(0xFF2563EB)))), contentAlignment = Alignment.Center) {
                                Text("Criar Conta", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))
                        
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                            Text("Já tem uma conta? ", color = Color(0xFFE9D5FF), fontSize = 14.sp)
                            Text("Faça Login", color = Color(0xFFFDE047), fontWeight = FontWeight.SemiBold, modifier = Modifier.clickable { navController.popBackStack() })
                        }
                    }
                }
            }
        }
    }
}

// Funções auxiliares mantidas para consistência (privadas para este arquivo)
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
