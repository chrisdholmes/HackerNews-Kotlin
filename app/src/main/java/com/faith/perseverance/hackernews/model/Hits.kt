package com.faith.perseverance.hackernews.model

data class Hits(val hits: List<Article>)

data class Article(val objectID: String = "",
                   val title: String = "",
                   val url: String?,
                   val points: Int = 0)