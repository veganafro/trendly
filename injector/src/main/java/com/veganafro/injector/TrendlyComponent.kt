package com.veganafro.injector

import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.ViewModelProvider
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [
    NytFragmentModule::class,
    NytViewModelModule::class,
    NytNetworkingModule::class,
    RxModule::class
])
interface TrendlyComponent {

    fun fragmentFactory(): FragmentFactory
}
