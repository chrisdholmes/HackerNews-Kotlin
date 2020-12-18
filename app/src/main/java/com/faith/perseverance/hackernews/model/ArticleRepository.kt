package com.faith.perseverance.hackernews.model

import androidx.lifecycle.LiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

class ArticleRepository(private val database: ArticleDatabase) {
    val bookMarks: LiveData<List<Article>> = database.articleDao.getBookmarks()

    suspend fun updateArticles()
    {
        withContext(Dispatchers.IO)
        {
            Timber.d("updateArticles called from ArticleRepo")
            var hits = HNApi.retrofitService.getProperties().hits
        }
    }
}