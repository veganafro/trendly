package com.veganafro.injector

import com.veganafro.networking.nyt.NytService
import dagger.Module
import dagger.Provides
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
    fun provideNytMostShared(retrofit: Retrofit): NytService {
        return retrofit
            .create(NytService::class.java)
    }
}
