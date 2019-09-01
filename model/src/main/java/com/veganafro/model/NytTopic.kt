package com.veganafro.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NytTopic(
    @Json(name = "")
    val tbd: Any
)