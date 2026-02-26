package com.example.quiz.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.firestore.Exclude

@Entity(tableName = "questions")
data class Question(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var firestoreId: String = "",
    var text: String = "",
    var option1: String = "",
    var option2: String = "",
    var option3: String = "",
    var option4: String = "",
    var correctOption: Int = 0,
    var category: String = "Geral"
) {
    // Construtor vazio para o Firebase (essencial)
    constructor() : this(0, "", "", "", "", "", "", 0, "Geral")

    @get:Exclude
    val options: List<String>
        get() = listOf(option1, option2, option3, option4)
}
