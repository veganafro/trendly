package com.veganafro.injector

import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class NytViewModelModule {

    @Provides
    @Singleton
    fun provideViewModelFactory(viewModelFactory: TrendlyViewModelFactory): ViewModelProvider.Factory = viewModelFactory
}