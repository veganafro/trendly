package com.veganafro.controller

import android.util.Log
import com.veganafro.contract.GenericPresenter
import com.veganafro.contract.GenericFragment
import com.veganafro.networking.nyt.NytService
import com.veganafro.model.NytTopic
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.Observable
import io.reactivex.Scheduler
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.withContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.Job
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Named

class NytTrendingPresenter @Inject constructor(
    private val nytMostShared: NytService
) : GenericPresenter {

    override var fragment: GenericFragment? = null
        set(value) {
            field?.let {} ?: run { field = value }
        }

    override val job: Job = Job()
    override val coroutineContext: CoroutineContext = job
        .plus(Dispatchers.IO)
        .plus(CoroutineExceptionHandler { coroutineContext: CoroutineContext, throwable: Throwable ->
            Log.e(
                "Trendly|NytTP",
                "Active status ${coroutineContext.isActive}",
                throwable
            )
        })

    @Inject lateinit var subscriptions: CompositeDisposable
    @field:[Inject Named("mainScheduler")] lateinit var mainScheduler: Scheduler
    @field:[Inject Named("backgroundScheduler")] lateinit var backgroundScheduler: Scheduler

    suspend fun coLoadData() {
        withContext(Dispatchers.Main) {
            fragment?.onFetchDataStarted()
        }

        try {
            val nytTopic = nytMostShared
                .coMostShared(1, BuildConfig.NYT_CONSUMER_KEY)

            withContext(Dispatchers.Main) {
                fragment?.onFetchDataSuccess(nytTopic.results)
                fragment?.onFetchDataCompleted()
            }
        } catch (ioException: IOException) {
            withContext(Dispatchers.Main) {
                fragment?.onFetchDataError(ioException)
            }
        } catch (httpException: HttpException) {
            withContext(Dispatchers.Main) {
                fragment?.onFetchDataError(httpException)
            }
        }
    }

    override fun loadData() {
        fragment?.onFetchDataStarted()
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
                    fragment?.onFetchDataSuccess(nytTopic?.results)
                },
                { error: Throwable? ->
                    fragment?.onFetchDataError(error!!)
                },
                {
                    fragment?.onFetchDataCompleted()
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
        fragment = null
        job.cancel()
    }

    override fun onDestroy() {
        fragment = null
    }
}
