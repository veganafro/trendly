package com.veganafro.networking

import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
class NytNetworkingModule {

    private val nytBaseURL = "https://api.nytimes.com/"

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit
            .Builder()
            .addConverterFactory(MoshiConverterFactory.create())
            .baseUrl(nytBaseURL)
            .build()
    }

    @Provides
    fun provideMostShared(retrofit: Retrofit) {
        retrofit
            .create(NytService::class.java)
            .mostShared(1)
    }

}