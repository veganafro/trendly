package com.veganafro.controller.implementation

import com.veganafro.controller.BuildConfig
import com.veganafro.controller.generic.GenericPresenter
import com.veganafro.controller.generic.GenericView
import com.veganafro.model.NytTopic
import com.veganafro.networking.nyt.NytService
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.withContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.Job
import javax.inject.Inject
import javax.inject.Named

class MainActivityPresenter @Inject constructor() :
    GenericPresenter {

    override var view: GenericView? = null
        set(value) {
            field?.let {} ?: run { field = value }
        }

    override val job: Job = Job()
    override val coroutineContext: CoroutineContext = job
        .plus(Dispatchers.IO)
        .plus(CoroutineExceptionHandler { _: CoroutineContext, t: Throwable ->
            view?.onFetchDataError(t)
        })

    @field:[Inject Named("mainScheduler")] lateinit var mainScheduler: Scheduler
    @field:[Inject Named("backgroundScheduler")] lateinit var backgroundScheduler: Scheduler

    @Inject lateinit var nytMostShared: NytService
    @Inject lateinit var subscriptions: CompositeDisposable

    suspend fun coLoadData() {
        withContext(Dispatchers.Main) {
            view?.onFetchDataStarted()
        }

        val nytTopic: NytTopic = nytMostShared
            .coMostShared(1, BuildConfig.NYT_CONSUMER_KEY)

        withContext(Dispatchers.Main) {
            view?.onFetchDataSuccess(nytTopic.results)
            view?.onFetchDataCompleted()
        }
    }

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

    fun coSubscribe() {
        launch {
            coLoadData()
        }
    }

    override fun subscribe() {
        loadData()
    }

    fun coUnsubscribe() {
        // call `cancelChildren` to stop any in flight network requests
        // calling `cancel` will prevent this instance's future calls to `launch` from executing
        job.cancelChildren()
    }

    override fun unsubscribe() {
        subscriptions.clear()
    }

    fun coOnDestroy() {
        view = null
        job.cancel()
    }

    override fun onDestroy() {
        view = null
    }
}
