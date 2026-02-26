package com.example.quiz.data

import android.util.Log
import com.example.quiz.model.Question
import com.example.quiz.model.QuizResult
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeout

class QuizRepository(
    private val questionDao: QuestionDao,
    private val quizResultDao: QuizResultDao
) {
    private val firestore = FirebaseFirestore.getInstance()
    private val repositoryScope = CoroutineScope(Dispatchers.IO)

    // REQUISITO 2: Sincronização em tempo real com Firebase
    fun observeQuestions(): Flow<List<Question>> = callbackFlow {
        val listener = firestore.collection("questions")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("REPO_DEBUG", "Erro no Listener do Firebase: ${error.message}")
                    return@addSnapshotListener
                }

                snapshot?.let { querySnapshot ->
                    val updatedQuestions = querySnapshot.documents.mapNotNull { doc ->
                        try {
                            val rawCorrect = doc.get("correctOption")
                            val correctOption = when (rawCorrect) {
                                is Long -> rawCorrect.toInt()
                                is String -> rawCorrect.toIntOrNull() ?: 0
                                else -> 0
                            }
                            Question(
                                id = 0,
                                firestoreId = doc.id,
                                text = doc.getString("text") ?: "",
                                option1 = doc.getString("option1") ?: "",
                                option2 = doc.getString("option2") ?: "",
                                option3 = doc.getString("option3") ?: "",
                                option4 = doc.getString("option4") ?: "",
                                correctOption = correctOption,
                                category = doc.getString("category") ?: "Geral"
                            )
                        } catch (e: Exception) { null }
                    }

                    if (updatedQuestions.isNotEmpty()) {
                        repositoryScope.launch {
                            try {
                                questionDao.deleteAll()
                                questionDao.insertQuestions(updatedQuestions)
                                Log.d("REPO_DEBUG", "Banco local sincronizado!")
                            } catch (e: Exception) {
                                Log.e("REPO_DEBUG", "Erro ao salvar no Room: ${e.message}")
                            }
                        }
                        trySend(updatedQuestions)
                    }
                }
            }
        awaitClose { listener.remove() }
    }

    suspend fun getQuestions(): List<Question> {
        return try {
            val snapshot = withTimeout(5000) {
                firestore.collection("questions").get().await()
            }
            val remote = snapshot.documents.mapNotNull { doc ->
                try {
                    val rawCorrect = doc.get("correctOption")
                    val correctOption = when (rawCorrect) {
                        is Long -> rawCorrect.toInt()
                        is String -> rawCorrect.toIntOrNull() ?: 0
                        else -> 0
                    }
                    Question(
                        firestoreId = doc.id,
                        text = doc.getString("text") ?: "",
                        option1 = doc.getString("option1") ?: "",
                        option2 = doc.getString("option2") ?: "",
                        option3 = doc.getString("option3") ?: "",
                        option4 = doc.getString("option4") ?: "",
                        correctOption = correctOption,
                        category = doc.getString("category") ?: "Geral"
                    )
                } catch (e: Exception) { null }
            }
            
            if (remote.isNotEmpty()) {
                questionDao.deleteAll()
                questionDao.insertQuestions(remote)
            }
            remote
        } catch (e: Exception) {
            questionDao.getAllQuestions()
        }
    }

    suspend fun saveQuizResult(result: QuizResult) {
        quizResultDao.insertResult(result)
        try {
            firestore.collection("results").add(result).await()
        } catch (e: Exception) {
            Log.e("REPO_DEBUG", "Erro ao salvar na nuvem: ${e.message}")
        }
    }

    suspend fun getFullHistory(): List<QuizResult> = quizResultDao.getAllResults()
    suspend fun getRecentHistory(): List<QuizResult> = quizResultDao.getRecentResults()
    suspend fun clearHistory() = quizResultDao.deleteAll()

    // REQUISITO: Ranking Global Profissional
    // Ignora tentativas ruins e mantém apenas o recorde por tema + desempate por tempo.
    suspend fun getGlobalRanking(): List<QuizResult> {
        return try {
            val snapshot = firestore.collection("results").get().await()
            val allResults = snapshot.toObjects(QuizResult::class.java)

            val groupedRanking = allResults.groupBy { it.userId }
                .map { (userId, userResults) ->
                    // Pega o melhor resultado de cada categoria
                    val bestPerCategory = userResults.groupBy { it.category }
                        .mapNotNull { (_, catResults) -> 
                            catResults.maxByOrNull { it.score } 
                        }

                    QuizResult(
                        userId = userId,
                        userName = userResults.firstOrNull()?.userName ?: "Anônimo",
                        // Pontuação total é a soma dos recordes em cada tema
                        score = bestPerCategory.sumOf { it.score },
                        // Mantemos o total de partidas realizadas para exibir na tela
                        totalQuestions = userResults.size, 
                        // Soma do tempo apenas das melhores partidas (critério de desempate)
                        timeTakenSeconds = bestPerCategory.sumOf { it.timeTakenSeconds },
                        timestamp = userResults.maxOfOrNull { it.timestamp } ?: 0L
                    )
                }
                // Ordenação profissional: Mais pontos primeiro, em caso de empate, menor tempo vence.
                .sortedWith(compareByDescending<QuizResult> { it.score }.thenBy { it.timeTakenSeconds })
                .take(10)

            groupedRanking
        } catch (e: Exception) {
            Log.e("REPO_DEBUG", "Erro ao calcular ranking profissional", e)
            emptyList()
        }
    }
}
