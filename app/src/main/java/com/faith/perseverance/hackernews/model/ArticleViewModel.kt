package com.faith.perseverance.hackernews.model

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.time.LocalDateTime.now

@RequiresApi(Build.VERSION_CODES.O)
class ArticleViewModel(): ViewModel() {

    val articles: MutableLiveData<List<Article>> by lazy {
        MutableLiveData<List<Article>>()
    }

    private val _navigateToWebFragment = MutableLiveData<Boolean?>()

    val navigativeToWebFragment: LiveData<Boolean?> get() = _navigateToWebFragment


    private var TAG: String = "ArticleViewModel"

    init{

        getArticles()
    }

    fun doneNavigating()
    {
        _navigateToWebFragment.value = null
    }

    fun startNavigating()
    {
        _navigateToWebFragment.value = true
    }



    @RequiresApi(Build.VERSION_CODES.O)
    fun getArticles()
    {
        Log.v(TAG, "get articles ()")

            viewModelScope.launch {
                try {
                    Log.v(TAG, "coroutine launched - ${now()}")
                    var hits = HNApi.retrofitService.getProperties().hits
                    articles.postValue(hits)
                    Log.v(TAG, "coroutine completed - ${now()}")
                    Log.v(TAG, "test: ${articles.value}")

                } catch (e: Exception) {

                    Log.v(TAG, e.toString())
                    Log.v(TAG, "coroutine failed - ${now()}")

                }
            }

    }








}