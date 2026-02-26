package com.example.quiz.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.quiz.model.QuizResult

@Dao
interface QuizResultDao {
    @Insert
    suspend fun insertResult(result: QuizResult)

    // Busca apenas os 15 mais recentes para a lista da tela
    @Query("SELECT * FROM quiz_results ORDER BY timestamp DESC LIMIT 15")
    suspend fun getRecentResults(): List<QuizResult>

    // Busca TODOS os registros para os indicadores/estatísticas
    @Query("SELECT * FROM quiz_results ORDER BY timestamp DESC")
    suspend fun getAllResults(): List<QuizResult>

    // Opção para excluir todo o histórico local
    @Query("DELETE FROM quiz_results")
    suspend fun deleteAll()
}
