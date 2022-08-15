package com.faith.perseverance.hackernews.model

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import kotlinx.coroutines.launch



class ArticleViewModel(private val repository: ArticleRepository) : ViewModel() {

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

    fun setArticleSelected(article: Article) {
        articleSelected.value = article
    }

    fun getArticleSelected(): Article? {
        return articleSelected.value
    }

    fun bookMarkSelectedArticle() {
        if (articleSelected.value != null) {
            addBookMark(articleSelected.value!!)
        }
    }

    fun getSearchQuery(searchQuery: String) {
        viewModelScope.launch {

            try {
                repository.getSearchQuery(searchQuery)
                articles.postValue(repository.articles.value)
            } catch (e: Exception) {
                Log.v(TAG, e.toString())
            }
        }
    }

    //getArticles() retrieves all articles from the front_page of hn.algolia.com
    fun getArticles() {
        viewModelScope.launch {

            try {
                repository.getArticles()
                articles.postValue(repository.articles.value)
            } catch (e: Exception) {
                Log.v(TAG, e.toString())
            }
        }
    }

    fun addBookMark(article: Article) = viewModelScope.launch {
        repository.addBookMark(article)
    }

    fun deleteBookMark(article: Article) = viewModelScope.launch {
        repository.deleteBookMark(article)
    }

    fun deleteAll() {
        viewModelScope.launch {
            repository.deleteAllBookMarks()
        }
    }

}

class ArticleViewModelFactory(private val repository: ArticleRepository) :
    ViewModelProvider.Factory {
    
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ArticleViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ArticleViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}