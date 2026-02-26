package com.example.quiz.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quiz.data.QuizRepository
import com.example.quiz.model.Question
import com.example.quiz.model.QuizResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class QuizViewModel(private val repository: QuizRepository) : ViewModel() {

    private val _allQuestions = MutableStateFlow<List<Question>>(emptyList())
    val allQuestions: StateFlow<List<Question>> = _allQuestions.asStateFlow()

    private val _questions = MutableStateFlow<List<Question>>(emptyList())
    val questions: StateFlow<List<Question>> = _questions.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _currentIndex = MutableStateFlow(0)
    val currentIndex: StateFlow<Int> = _currentIndex.asStateFlow()

    private val _score = MutableStateFlow(0)
    val score: StateFlow<Int> = _score.asStateFlow()

    private val _timer = MutableStateFlow(30)
    val timer: StateFlow<Int> = _timer.asStateFlow()

    private val _isFinished = MutableStateFlow(false)
    val isFinished: StateFlow<Boolean> = _isFinished.asStateFlow()

    private val _feedbackMessage = MutableStateFlow<String?>(null)
    val feedbackMessage: StateFlow<String?> = _feedbackMessage.asStateFlow()

    private val _isCorrect = MutableStateFlow<Boolean?>(null)
    val isCorrect: StateFlow<Boolean?> = _isCorrect.asStateFlow()

    // NOVO ESTADO: Armazena o índice do botão clicado pelo usuário (1 a 4)
    private val _userSelection = MutableStateFlow<Int?>(null)
    val userSelection: StateFlow<Int?> = _userSelection.asStateFlow()

    private val _history = MutableStateFlow<List<QuizResult>>(emptyList())
    val history: StateFlow<List<QuizResult>> = _history.asStateFlow()

    private val _fullHistory = MutableStateFlow<List<QuizResult>>(emptyList())
    val fullHistory: StateFlow<List<QuizResult>> = _fullHistory.asStateFlow()

    private val _ranking = MutableStateFlow<List<QuizResult>>(emptyList())
    val ranking: StateFlow<List<QuizResult>> = _ranking.asStateFlow()

    private var timerJob: Job? = null
    private var quizStartTime: Long = 0
    private var currentSelectedCategory: String = "Geral"

    fun loadQuestions() {
        if (_allQuestions.value.isNotEmpty()) return
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _allQuestions.value = repository.getQuestions()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun selectCategory(category: String) {
        currentSelectedCategory = category
        val filtered = _allQuestions.value.filter { it.category == category }
        _questions.value = filtered.shuffled()
        quizStartTime = System.currentTimeMillis()
        resetQuiz()
    }

    private fun resetQuiz() {
        _currentIndex.value = 0
        _score.value = 0
        _isFinished.value = false
        _feedbackMessage.value = null
        _isCorrect.value = null
        _userSelection.value = null
        if (_questions.value.isNotEmpty()) {
            startTimer()
        }
    }

    private fun startTimer() {
        timerJob?.cancel()
        _timer.value = 30
        timerJob = viewModelScope.launch {
            while (_timer.value > 0) {
                delay(1000)
                _timer.value -= 1
            }
            if (_feedbackMessage.value == null) submitAnswer(-1)
        }
    }

    fun submitAnswer(selectedOption: Int) {
        if (_feedbackMessage.value != null) return
        
        timerJob?.cancel()
        _userSelection.value = selectedOption // GRAVA A ESCOLHA DO USUÁRIO
        
        val currentQuestion = _questions.value.getOrNull(_currentIndex.value) ?: return
        val correct = currentQuestion.correctOption

        if (correct == selectedOption) {
            _isCorrect.value = true
            _score.value += 1
            _feedbackMessage.value = "Correto!"
        } else {
            _isCorrect.value = false
            val correctText = when(correct) {
                1 -> currentQuestion.option1
                2 -> currentQuestion.option2
                3 -> currentQuestion.option3
                4 -> currentQuestion.option4
                else -> "Desconhecida"
            }
            _feedbackMessage.value = "Errado! Resposta: $correctText"
        }

        viewModelScope.launch {
            delay(1500)
            _feedbackMessage.value = null
            _isCorrect.value = null
            _userSelection.value = null // LIMPA A SELEÇÃO PARA A PRÓXIMA
            nextQuestion()
        }
    }

    private fun nextQuestion() {
        if (_currentIndex.value < _questions.value.size - 1) {
            _currentIndex.value += 1
            startTimer()
        } else {
            finishQuiz()
        }
    }

    private fun finishQuiz() {
        timerJob?.cancel()
        _isFinished.value = true
        val user = FirebaseAuth.getInstance().currentUser
        val totalTimeSeconds = ((System.currentTimeMillis() - quizStartTime) / 1000).toInt()
        
        viewModelScope.launch {
            val result = QuizResult(
                userId = user?.uid ?: "anon",
                userName = user?.email?.split("@")?.get(0) ?: "Jogador",
                category = currentSelectedCategory,
                score = _score.value,
                totalQuestions = _questions.value.size,
                timeTakenSeconds = totalTimeSeconds,
                timestamp = System.currentTimeMillis()
            )
            repository.saveQuizResult(result)
            loadHistory()
        }
    }

    fun loadHistory() {
        viewModelScope.launch {
            _history.value = repository.getRecentHistory()
            _fullHistory.value = repository.getFullHistory()
        }
    }

    fun clearAllHistory() {
        viewModelScope.launch {
            repository.clearHistory()
            loadHistory()
        }
    }

    fun loadRanking() {
        viewModelScope.launch {
            _ranking.value = repository.getGlobalRanking()
        }
    }
}
