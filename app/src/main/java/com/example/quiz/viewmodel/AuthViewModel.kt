package com.example.quiz.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AuthViewModel : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _loginState = MutableStateFlow<Boolean?>(null)
    val loginState: StateFlow<Boolean?> = _loginState

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun login(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _errorMessage.value = "Preencha todos os campos"
            _loginState.value = false
            return
        }

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _loginState.value = true
                    _errorMessage.value = null
                } else {
                    _loginState.value = false
                    val exception = task.exception
                    _errorMessage.value = when (exception) {
                        is FirebaseNetworkException -> "Sem conexão com a internet"
                        else -> "E-mail ou senha incorretos"
                    }
                }
            }
    }

    fun register(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _errorMessage.value = "Preencha todos os campos"
            _loginState.value = false
            return
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _loginState.value = true
                } else {
                    _loginState.value = false
                    _errorMessage.value = "Erro ao criar conta"
                }
            }
    }

    fun resetPassword(email: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        auth.sendPasswordResetEmail(email)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e -> onError(e.message ?: "Erro desconhecido") }
    }

    fun resetState() {
        _loginState.value = null
        _errorMessage.value = null
    }
}
