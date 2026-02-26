package com.example.quiz.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.quiz.data.QuizRepository

// Definição da Classe
class QuizViewModelFactory(private val repository: QuizRepository) : ViewModelProvider.Factory {

    // Este método 'create' é o que o Android chama internamente
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // Verifica se a ViewModel que o Android quer criar é a QuizViewModel
        if (modelClass.isAssignableFrom(QuizViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return QuizViewModel(repository) as T
        }
        throw IllegalArgumentException("Classe ViewModel desconhecida")
    }
}