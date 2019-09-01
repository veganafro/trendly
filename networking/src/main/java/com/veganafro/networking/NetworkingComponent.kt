package com.veganafro.networking

import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ NytNetworkingModule::class ])
interface NetworkingComponent {
    fun inject() {
    }
}