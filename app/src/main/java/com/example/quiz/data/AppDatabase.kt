package com.example.quiz.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.quiz.model.Question
import com.example.quiz.model.QuizResult

// Aumentamos a versão para 2 pois a estrutura do QuizResult mudou
@Database(entities = [Question::class, QuizResult::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun questionDao(): QuestionDao
    abstract fun quizResultDao(): QuizResultDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "quiz_database"
                )
                .fallbackToDestructiveMigration() // Isso evita o crash ao mudar a estrutura
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
