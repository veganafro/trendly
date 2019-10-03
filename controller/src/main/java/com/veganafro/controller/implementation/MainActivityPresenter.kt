package com.veganafro.controller.implementation

import com.veganafro.controller.BuildConfig
import com.veganafro.controller.generic.GenericPresenter
import com.veganafro.controller.generic.GenericView
import com.veganafro.model.NytTopic
import com.veganafro.networking.nyt.NytService
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import javax.inject.Inject
import javax.inject.Named

class MainActivityPresenter @Inject constructor() :
    GenericPresenter {

    private var view: GenericView? = null
    @field:[Inject Named("mainScheduler")] lateinit var mainScheduler: Scheduler
    @field:[Inject Named("backgroundScheduler")] lateinit var backgroundScheduler: Scheduler

    private var view: GenericView? = null
    @field:[Inject Named("mainScheduler")] lateinit var mainScheduler: Scheduler
    @field:[Inject Named("backgroundScheduler")] lateinit var backgroundScheduler: Scheduler

    private var view: GenericView? = null
    @field:[Inject Named("mainScheduler")] lateinit var mainScheduler: Scheduler
    @field:[Inject Named("backgroundScheduler")] lateinit var backgroundScheduler: Scheduler

    private var view: GenericView? = null
    @Inject lateinit var nytMostShared: NytService
    @Inject lateinit var subscriptions: CompositeDisposable

    override fun loadData() {
        view?.onFetchDataStarted()
        subscriptions.clear()

        val subscription: Disposable = nytMostShared
            .mostShared(1, BuildConfig.NYT_CONSUMER_KEY)
            .subscribeOn(backgroundScheduler)
            .observeOn(mainScheduler)
            .subscribe(
                { nytTopic: NytTopic? ->
                    view?.onFetchDataSuccess(nytTopic?.results)
                },
                { error: Throwable? ->
                    view?.onFetchDataError(error!!)
                },
                {
                    view?.onFetchDataCompleted()
                }
            )

        subscriptions.add(subscription)
    }

    override fun subscribe() {
        loadData()
    }

    override fun unsubscribe() {
        subscriptions.clear()
    }

    override fun onDestroy() {
        this.view = null
    }

    fun setView(view: GenericView) {
        this.view?.let {} ?: run { this.view = view }
    }
}
