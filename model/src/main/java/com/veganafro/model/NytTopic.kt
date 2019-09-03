package com.veganafro.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NytTopic(
    @Json(name = "results")
    val results: List<Article>
) {
    @JsonClass(generateAdapter = true)
    data class Article(
        @Json(name = "url")
        val url: String,
        @Json(name = "title")
        val title: String
    )
}
