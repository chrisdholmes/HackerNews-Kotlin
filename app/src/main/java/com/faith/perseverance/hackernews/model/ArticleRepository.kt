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
    suspend fun addBookMark(bookMark: Article)
    {
        articleDAO.addBookmark(bookMark)
    }

    @WorkerThread
    suspend fun deleteBookMark(bookMark: Article)
    {
        articleDAO.deleteBookmark(bookMark)
    }

    @WorkerThread
     suspend fun deleteAllBookMarks()
    {
        articleDAO.deleteAll()
    }

    //TODO("Update viewmodel to use this method")
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun getArticles()
    {
        Log.v(TAG, "get articles ()")

       coroutineScope {
           launch {
               try {
                   Log.v(TAG, "coroutine launched - ${now()}")
                   val hits = HNApi.retrofitService.getProperties().hits
                   articles.postValue(hits)
                   Log.v(TAG, "coroutine completed - ${now()}")


               } catch (e: Exception) {

                   Log.v(TAG, e.toString())
                   Log.v(TAG, "coroutine failed - ${now()}")

               }
           }
        }

    }

}