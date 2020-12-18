package com.faith.perseverance.hackernews.model

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

interface ArticleDAO {
    @Query("select * from database")
    fun getBookmarks(): LiveData<List<Article>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addBookmark( bookmark: Article)

    @Delete
    fun deleteBookmark(bookmark: Article)

}

@Database(entities = [Article::class], version = 1)
abstract class ArticleDatabase: RoomDatabase() {
    abstract val articleDao: ArticleDAO
}

private lateinit var INSTANCE: ArticleDatabase

fun getDatabase(context: Context): ArticleDatabase{
    synchronized(ArticleDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(context.applicationContext,
                ArticleDatabase::class.java,
                "videos").build()
        }
    }
    return INSTANCE
}