package com.veganafro.networking.nyt

import com.veganafro.networking.presenter.MainActivityPresenter
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ NytNetworkingModule::class ])
interface NytNetworkingComponent {
    fun inject(mainActivityPresenter: MainActivityPresenter)
}