package com.faith.perseverance.hackernews.model

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class ArticlesApplication : Application() {

    val applicationScope = CoroutineScope(SupervisorJob())

    val database by lazy { ArticleDatabase.getDatabase(this) }

    val repository by lazy { ArticleRepository(database.articleDAO()) }
}