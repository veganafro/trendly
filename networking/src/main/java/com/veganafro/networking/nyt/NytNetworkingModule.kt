package com.veganafro.networking.nyt

import com.veganafro.model.NytTopic
import com.veganafro.networking.BuildConfig
import dagger.Module
import dagger.Provides
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
class NytNetworkingModule {

    private val nytBaseURL = "https://api.nytimes.com/"

    @Provides
    @Singleton
    fun provideNytRetrofit(): Retrofit {
        return Retrofit
            .Builder()
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl(nytBaseURL)
            .build()
    }

    @Provides
    fun provideNytMostShared(retrofit: Retrofit): Observable<NytTopic> {
        return retrofit
            .create(NytService::class.java)
            .mostShared(1, BuildConfig.NYT_CONSUMER_KEY)
    }
}
