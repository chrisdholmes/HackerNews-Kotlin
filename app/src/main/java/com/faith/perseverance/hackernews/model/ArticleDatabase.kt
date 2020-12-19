package com.faith.perseverance.hackernews.model

import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

@Dao
interface ArticleDAO {

    @Query("Select * from articles")
    fun getBookmarks(): Flow<List<Article>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addBookmark(bookmark: Article)

    @Delete
    suspend fun deleteBookmark(bookmark: Article)

}

@Database(entities = [Article::class], version = 4, exportSchema = false)
abstract class ArticleDatabase: RoomDatabase() {
    abstract fun articleDAO(): ArticleDAO

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: ArticleDatabase? = null

        fun getDatabase(context: Context,
        scope: CoroutineScope
        ): ArticleDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                        context.applicationContext,
                        ArticleDatabase::class.java,
                        "articles_db"
                ).addCallback(ArticleDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }

    private class ArticleDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    populateDatabase(database.articleDAO())
                }
            }
        }

        suspend fun populateDatabase(articleDAO: ArticleDAO) {
            // Delete all content here.

            // Add sample words.
            var article = Article("1234","The Billioniare Next Door", "https://www.medium.com/christopher-holmes")
            articleDAO.addBookmark(article)


            // TODO: Add your own words!
        }
    }



}