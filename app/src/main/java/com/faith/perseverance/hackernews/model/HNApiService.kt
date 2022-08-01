package com.faith.perseverance.hackernews.model

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

//Base url for HTTP Request
private const val URL = "https://hn.algolia.com/api/v1/"

//Moshi is a 3rd party library that converts JSON to objects
private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

//Retrofit is a 3rd party library that makes network requests
private val retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .baseUrl(URL)
        .build()


interface HNApiService {
    //getProperties() returns all articles with the front_page tag
    @GET("search?tags=front_page")
    suspend fun getProperties(): Hits
}

//Singleton for ViewModel/Repositories to use to retrieve data
object HNApi {
    val retrofitService: HNApiService by lazy {
        retrofit.create(HNApiService::class.java)
    }
}