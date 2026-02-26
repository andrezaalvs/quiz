package com.example.quiz.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.quiz.model.Question

@Dao
interface QuestionDao {
    @Query("SELECT * FROM questions")
    suspend fun getAllQuestions(): List<Question>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestions(questions: List<Question>)

    @Query("DELETE FROM questions")
    suspend fun deleteAll()
}