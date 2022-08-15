package com.faith.perseverance.hackernews.model

import android.content.Context
import androidx.room.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

@Dao
interface ArticleDAO {

    @Query("Select * from articles")
    fun getBookmarks(): Flow<List<Article>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addBookmark(bookmark: Article)

    @Delete
    suspend fun deleteBookmark(bookmark: Article)

    @Query("DELETE FROM articles")
    suspend fun deleteAll()

}

@Database(entities = [Article::class], version = 4, exportSchema = false)
abstract class ArticleDatabase : RoomDatabase() {
    abstract fun articleDAO(): ArticleDAO

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: ArticleDatabase? = null

        fun getDatabase(
            context: Context
        ): ArticleDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ArticleDatabase::class.java,
                    "articles_db"
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }

}