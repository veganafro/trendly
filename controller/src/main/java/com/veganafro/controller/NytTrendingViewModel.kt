package com.veganafro.controller

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.veganafro.model.NytTopic
import com.veganafro.networking.nyt.NytService
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.Job
import javax.inject.Inject

class NytTrendingViewModel @Inject constructor(
    private val nytService: NytService
) : ViewModel(), CoroutineScope {

    private val job: Job = SupervisorJob()
    override val coroutineContext: CoroutineContext = job
        .plus(Dispatchers.IO)
        .plus(CoroutineExceptionHandler { _: CoroutineContext, throwable: Throwable ->
            Log.v("Trendly|NytTVM", "Something went wrong: ${throwable.message}")
        })

    private val mostShared: MutableLiveData<MutableList<NytTopic.Article>> by lazy {
        MutableLiveData<MutableList<NytTopic.Article>>()
    }

    private fun loadMostShared() {
        job.cancelChildren()
        launch {
            val results = nytService
                .coMostShared(1, BuildConfig.NYT_CONSUMER_KEY)
                .results
            mostShared.postValue(results)
        }
    }

    fun getMostShared(): LiveData<MutableList<NytTopic.Article>> {
        return mostShared
    }

    fun refreshMostShared() {
        loadMostShared()
    }

    fun fetchMostShared() {
        mostShared.value?.let {
            if (it.size > 0) {
                mostShared.postValue(it)
            } else {
                loadMostShared()
            }
        } ?: apply {
            loadMostShared()
        }
    }
}
