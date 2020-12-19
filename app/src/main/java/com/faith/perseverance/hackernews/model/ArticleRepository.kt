package com.faith.perseverance.hackernews.model

import androidx.annotation.WorkerThread
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import timber.log.Timber

class ArticleRepository(private val articleDAO: ArticleDAO) {

    val bookMarks: Flow<List<Article>> = articleDAO.getBookmarks()

    suspend fun updateArticles()
    {
        withContext(Dispatchers.IO)
        {
            Timber.d("updateArticles called from ArticleRepo")
            var hits = HNApi.retrofitService.getProperties().hits
        }
    }
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

}