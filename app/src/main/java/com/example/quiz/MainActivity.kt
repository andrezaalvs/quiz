package com.example.quiz

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.quiz.data.AppDatabase
import com.example.quiz.data.QuizRepository
import com.example.quiz.ui.screens.*
import com.example.quiz.viewmodel.AuthViewModel
import com.example.quiz.viewmodel.QuizViewModel
import com.example.quiz.viewmodel.QuizViewModelFactory
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database by lazy { AppDatabase.getDatabase(this) }
        val repository by lazy { QuizRepository(database.questionDao(), database.quizResultDao()) }

        setContent {
            MaterialTheme {
                val navController = rememberNavController()
                val authViewModel: AuthViewModel = viewModel()
                val quizViewModel: QuizViewModel = viewModel(
                    factory = QuizViewModelFactory(repository)
                )

                // LÓGICA DE PERSISTÊNCIA OFFLINE: 
                // Verifica se já existe um usuário logado no dispositivo
                val currentUser = FirebaseAuth.getInstance().currentUser
                val startRoute = if (currentUser != null) "dashboard" else "login"

                NavHost(navController = navController, startDestination = startRoute) {

                    composable("login") {
                        LoginScreen(
                            authViewModel = authViewModel,
                            navController = navController,
                            onRegisterClick = { navController.navigate("register") }
                        )
                    }

                    composable("register") {
                        RegisterScreen(
                            authViewModel = authViewModel,
                            navController = navController
                        )
                    }

                    composable("dashboard") {
                        DashboardScreen(navController, authViewModel, quizViewModel)
                    }

                    composable("categories") {
                        CategoryScreen(navController, quizViewModel)
                    }

                    composable("quiz") {
                        QuizScreen(viewModel = quizViewModel, navController = navController)
                    }

                    composable("history") {
                        HistoryScreen(viewModel = quizViewModel)
                    }

                    composable("ranking") {
                        RankingScreen(viewModel = quizViewModel)
                    }
                }
            }
        }
    }
}
