package com.veganafro.injector

import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides

@Module
class NytViewModelModule {

    @Provides
    fun provideViewModelFactory(viewModelFactory: TrendlyViewModelFactory): ViewModelProvider.Factory = viewModelFactory
}