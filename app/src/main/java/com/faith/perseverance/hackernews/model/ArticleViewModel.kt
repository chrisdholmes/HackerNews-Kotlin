package com.faith.perseverance.hackernews.model

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.time.LocalDateTime.now

/**
 * Retrieves articles from the HNApi retro fit service
 * and stores them in to a list of articles.
 *
 * @constructor retrieves articles
 * @property articles MutableLiveData<List<Article>>
 */


@RequiresApi(Build.VERSION_CODES.O)
class ArticleViewModel(): ViewModel() {

    val articles: MutableLiveData<List<Article>> by lazy {
        MutableLiveData<List<Article>>()
    }

    //TAG for logging
    private var TAG: String = "ArticleViewModel"

    init{
        // retrieve articles when class is initialized
        getArticles()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getArticles()
    {
            //launch CoRoutine that uses HNApi to retrieve articles
            viewModelScope.launch {
                try {
                    Log.v(TAG, "coroutine launched - ${now()}")
                    var hits = HNApi.retrofitService.getProperties().hits
                    articles.postValue(hits)
                    Log.v(TAG, "coroutine completed - ${now()}")
                } catch (e: Exception) {
                    Log.v(TAG, e.toString())
                    Log.v(TAG, "coroutine failed - ${now()}")
                }
            }

    }








}