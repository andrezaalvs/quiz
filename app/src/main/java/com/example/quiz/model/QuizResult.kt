package com.example.quiz.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quiz_results")
data class QuizResult(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userId: String = "",
    val userName: String = "Anônimo",
    val category: String = "Geral",
    val score: Int = 0,
    val totalQuestions: Int = 0,
    val timeTakenSeconds: Int = 0,
    val timestamp: Long = System.currentTimeMillis()
) {
    // Construtor vazio para o Firebase
    constructor() : this(0, "", "Anônimo", "Geral", 0, 0, 0, 0L)
}
