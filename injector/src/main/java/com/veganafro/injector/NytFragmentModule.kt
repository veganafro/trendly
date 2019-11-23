package com.veganafro.injector

import androidx.fragment.app.FragmentFactory
import dagger.Module
import dagger.Provides

@Module
class NytFragmentModule {

    @Provides
    fun provideFragmentFactory(fragmentFactory: TrendlyFragmentFactory): FragmentFactory = fragmentFactory
}
