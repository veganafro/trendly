package com.veganafro.networking

import com.veganafro.model.NytTopic
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface NytService {

    @GET("svc/mostpopular/v2/shared/{period}.json")
    fun mostShared(@Path("period") period: Int?): Call<List<NytTopic>>
}