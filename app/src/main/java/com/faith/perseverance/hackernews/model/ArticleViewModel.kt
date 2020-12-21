package com.faith.perseverance.hackernews.model

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import java.time.Instant.now


@RequiresApi(Build.VERSION_CODES.O)
class ArticleViewModel(private val repository: ArticleRepository): ViewModel() {


    val articles: MutableLiveData<List<Article>> by lazy {
        MutableLiveData<List<Article>>()
    }

    init {
        getArticles()
    }

    val bookMarks: LiveData<List<Article>> = repository.bookMarks.asLiveData()

    private var TAG: String = "ArticleViewModel"

    val articleSelected: MutableLiveData<Article> by lazy {
        MutableLiveData<Article>()
   }


    fun setArticleSelected(article: Article)
    {
        articleSelected.value = article
    }

    fun getArticleSelected(): Article?
    {
        return articleSelected.value
    }

    fun bookMarkSelectedArticle()
    {
        if(articleSelected.value != null)
        {
            addBookMark(articleSelected.value!!)
        }
    }
    /*
    TODO("update getArticles to use repository")
    fun getArticles()
    {
        viewModelScope.launch {
            repository.getArticles()
            articles.postValue(repository.articles.value)
        }
    }
    */


    @RequiresApi(Build.VERSION_CODES.O)
    fun getArticles()
    {
        Log.v(TAG, "get articles ()")

            viewModelScope.launch {
                try {
                    Log.v(TAG, "coroutine launched - ${now()}")
                    val hits = HNApi.retrofitService.getProperties().hits
                    articles.postValue(hits)
                    Log.v(TAG, "coroutine completed - ${now()}")
                    Log.v(TAG, "test: ${articles.value}")

                } catch (e: Exception) {

                    Log.v(TAG, e.toString())
                    Log.v(TAG, "coroutine failed - ${now()}")

                }
            }

    }


    fun addBookMark(article: Article) = viewModelScope.launch {
        repository.addBookMark(article)
    }

    fun deleteBookMark(article: Article) = viewModelScope.launch {
        repository.deleteBookMark(article)
    }

     fun deleteAll()
    {
        viewModelScope.launch {
            repository.deleteAllBookMarks()
        }
    }

}

class ArticleViewModelFactory(private val repository: ArticleRepository) : ViewModelProvider.Factory {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ArticleViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ArticleViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}