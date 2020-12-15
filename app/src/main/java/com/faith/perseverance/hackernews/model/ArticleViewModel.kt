package com.faith.perseverance.hackernews.model

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
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


    private var TAG: String = "ArticleViewModel"

    init{

        getArticles()
    }


    fun getMockData(): MutableList<List<Article>>
    {
        /**
       var mockData: MutableList<Article> = mutableListOf(Article("Harry Potter","Chamber", Date()),
           Article("Jon Favreau","Mandalorian", Date()),
           Article("Kobe Bryant","Black Mamba", Date()),
           Article("Lebron James","King James", Date()),
           Article("Christopher Holmes","Faith Passion", Date()),
           Article("Christopher Holmes","Passion Perseverance", Date()),
           Article("Christopher Holmes","Perseverance $100K", Date()),
           Article("Christopher Holmes","$200K", Date()),
           Article("Christopher Holmes","$300K", Date()))

        return mockData
       */

        return mutableListOf()

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