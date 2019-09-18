package com.veganafro.injector

import androidx.appcompat.app.AppCompatActivity
import com.veganafro.controller.implementation.MainActivityPresenter
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [
    NytNetworkingModule::class
])
interface TrendlyComponent {

    @Singleton
    fun mainActivityPresenter(): MainActivityPresenter

    fun inject(app: AppCompatActivity)
}