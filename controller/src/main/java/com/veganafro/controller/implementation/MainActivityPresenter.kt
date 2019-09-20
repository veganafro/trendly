package com.veganafro.controller.implementation

import android.util.Log
import com.veganafro.controller.BuildConfig
import com.veganafro.controller.generic.GenericPresenter
import com.veganafro.controller.generic.GenericView
import com.veganafro.model.NytTopic
import com.veganafro.networking.nyt.NytService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MainActivityPresenter @Inject constructor() :
    GenericPresenter {

    private var view: GenericView? = null
    @Inject lateinit var nytMostShared: NytService
    @Inject lateinit var subscriptions: CompositeDisposable

    override fun loadData() {
        subscriptions.clear()

        val subscription: Disposable = nytMostShared
            .mostShared(1, BuildConfig.NYT_CONSUMER_KEY)
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
        this.view = null
    }

    fun setView(genericView: GenericView) {
        view?.let {} ?: run { view = genericView }
    }
}
