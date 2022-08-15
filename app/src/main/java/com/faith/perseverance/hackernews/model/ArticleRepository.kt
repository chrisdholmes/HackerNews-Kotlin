package com.faith.perseverance.hackernews.model

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.annotation.WorkerThread
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.time.Instant.now

class ArticleRepository(private val articleDAO: ArticleDAO) {

    val bookMarks: Flow<List<Article>> = articleDAO.getBookmarks()
    val articles: MutableLiveData<List<Article>> by lazy {
        MutableLiveData<List<Article>>()
    }

    private val TAG: String = "ArticleRepository: "

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun addBookMark(bookMark: Article) {
        articleDAO.addBookmark(bookMark)
    }

    @WorkerThread
    suspend fun deleteBookMark(bookMark: Article) {
        articleDAO.deleteBookmark(bookMark)
    }

    @WorkerThread
    suspend fun deleteAllBookMarks() {
        articleDAO.deleteAll()
    }

    
    suspend fun getArticles() {
        var hits = listOf<Article>()
        coroutineScope {
            launch {
                try {
                    Log.v(TAG, "coroutine launched - ${now()}")
                    hits = HNApi.retrofitService.getProperties().hits
                } catch (e: Exception) {
                    Log.v(TAG, e.toString())
                    Log.v(TAG, "coroutine failed - ${now()}")
                }
            }
        }
        articles.value = hits
        Log.v(TAG, "coroutine completed - ${now()} ${articles.value}")

    }

    
    suspend fun getSearchQuery(searchQuery: String) {
        var hits = listOf<Article>()
        coroutineScope {
            launch {
                try {
                    Log.v(TAG, "coroutine launched - ${now()}")
                    hits = HNApi.retrofitService.getSearchQuery(searchQuery).hits
                } catch (e: Exception) {
                    Log.v(TAG, e.toString())
                    Log.v(TAG, "coroutine failed - ${now()}")
                }
            }
        }
        articles.value = hits
        Log.v(TAG, "coroutine completed - ${now()} ${articles.value}")
    }

}