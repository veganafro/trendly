package com.veganafro.controller.implementation

import com.veganafro.controller.BuildConfig
import com.veganafro.controller.generic.GenericPresenter
import com.veganafro.controller.generic.GenericView
import com.veganafro.model.NytTopic
import com.veganafro.networking.nyt.NytService
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject
import javax.inject.Named

class MainActivityPresenter @Inject constructor() :
    GenericPresenter {

    private var view: GenericView? = null
    @field:[Inject Named("mainScheduler")] lateinit var mainScheduler: Scheduler
    @field:[Inject Named("backgroundScheduler")] lateinit var backgroundScheduler: Scheduler

    @Inject lateinit var nytMostShared: NytService
    @Inject lateinit var subscriptions: CompositeDisposable

    override fun loadData() {
        view?.onFetchDataStarted()
        subscriptions.clear()

        // create a cold observable that should be used to make the network request
        val subscription: Observable<NytTopic> = nytMostShared
            .mostShared(1, BuildConfig.NYT_CONSUMER_KEY)
            .subscribeOn(backgroundScheduler)
            .observeOn(mainScheduler)

        // kick off that cold observable into a hot observable at the same place that it's added
        // to the queue of in flight network requests
        subscriptions.add(
            subscription.subscribe(
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
        )
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
