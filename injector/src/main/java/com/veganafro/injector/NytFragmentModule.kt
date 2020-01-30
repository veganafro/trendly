package com.veganafro.injector

import androidx.fragment.app.FragmentFactory
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class NytFragmentModule {

    @Provides
    @Singleton
    fun provideFragmentFactory(fragmentFactory: TrendlyFragmentFactory): FragmentFactory = fragmentFactory
}
