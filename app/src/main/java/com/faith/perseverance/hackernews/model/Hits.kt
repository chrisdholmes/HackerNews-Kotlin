package com.faith.perseverance.hackernews.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Hits is a class that contains a list of Articles.
 * Mimics the JSON values from network calls to
 * hn.algolia.com API
 *
 * @param hits List<Article>
 *
 */
data class Hits(val hits: List<Article>)

/**
 * Article represents objects returned form the
 * hn.algolia.com API - these are news articles,
 * blog posts, and a variety of other
 * pieces of information.
 *
 * @param objectID - API returned an objectID
 * @param title
 * @param url
 * @param points - (number of times the article was up voted)
 */
@Entity(tableName = "articles")
data class Article(
    @PrimaryKey val objectID: String = "",
    val title: String = "",
    val url: String?,
    val points: Int = 0
)