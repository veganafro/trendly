package com.veganafro.controller.presenter

import android.util.Log
import com.veganafro.model.NytTopic
import com.veganafro.controller.nyt.DaggerNytNetworkingComponent
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MainActivityPresenter : GenericPresenter {

    @Inject lateinit var nytMostShared: Observable<NytTopic>
    private val subscriptions: CompositeDisposable = CompositeDisposable()

    init {
        DaggerNytNetworkingComponent.create().injectNytNetworking(this)
    }

    override fun loadData() {
        subscriptions.clear()

        val subscription: Disposable = nytMostShared
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { nytTopic: NytTopic? ->
                    Log.v("MainActivityPresenter", "success: ${nytTopic?.results}")
                },
                { error: Throwable? ->
                    Log.v("MainActivityPresenter", "failure: ${error?.message}")
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
    }
}