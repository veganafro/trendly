package com.veganafro.injector

import androidx.appcompat.app.AppCompatActivity
import com.veganafro.controller.implementation.MainActivityPresenter
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [
    NytNetworkingModule::class,
    RxModule::class
])
interface TrendlyComponent {

    fun mainActivityPresenter(): MainActivityPresenter

    fun inject(app: AppCompatActivity)
}
