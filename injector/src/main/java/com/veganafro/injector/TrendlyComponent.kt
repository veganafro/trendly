package com.veganafro.injector

import androidx.fragment.app.FragmentFactory
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [
    NytFragmentModule::class,
    NytNetworkingModule::class,
    RxModule::class
])
interface TrendlyComponent {

    fun fragmentFactory(): FragmentFactory
}
