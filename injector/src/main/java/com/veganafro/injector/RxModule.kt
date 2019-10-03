package com.veganafro.injector

import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Named
import javax.inject.Singleton

@Module
class RxModule {

    @Provides
    fun provideCompositeDisposable(): CompositeDisposable {
        return CompositeDisposable()
    }

    @Provides
    @Singleton
    @Named("mainScheduler")
    fun provideMainScheduler(): Scheduler {
        return AndroidSchedulers.mainThread()
    }

    @Provides
    @Named("backgroundScheduler")
    fun provideBackgroundScheduler(): Scheduler {
        return Schedulers.io()
    }
}
