package com.veganafro.networking.nyt

import com.veganafro.model.NytTopic
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface NytService {

    @Headers("Accept: application/json")
    @GET("svc/mostpopular/v2/shared/{period}.json")
    fun mostShared(@Path("period") period: Int?, @Query("api-key") key: String): Observable<NytTopic>

    @Headers("Accept: application/json")
    @GET("svc/mostpopular/v2/shared/{period}.json")
    suspend fun coMostShared(@Path("period") period: Int?, @Query("api-key") key: String): NytTopic
}
