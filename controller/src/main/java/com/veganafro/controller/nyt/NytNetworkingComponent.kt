package com.veganafro.controller.nyt

import com.veganafro.controller.presenter.MainActivityPresenter
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ NytNetworkingModule::class ])
interface NytNetworkingComponent {
    fun inject(mainActivityPresenter: MainActivityPresenter)
}
