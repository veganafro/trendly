package com.veganafro.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NytTopic(
    @Json(name = "results")
    val results: MutableList<Article>
) {

    @JsonClass(generateAdapter = true)
    data class Article(
        @Json(name = "url")
        val url: String,
        @Json(name = "title")
        val title: String,
        @Json(name = "section")
        val section: String,
        @Json(name = "byline")
        val byline: String,
        @Json(name = "media")
        val photos: MutableList<Photos>
    ) {

        @JsonClass(generateAdapter = true)
        data class Photos(
            @Json(name = "media-metadata")
            val photos: MutableList<Photo>
        ) {

            @JsonClass(generateAdapter = true)
            data class Photo(
                @Json(name = "url")
                val url: String
            )
        }
    }
}
