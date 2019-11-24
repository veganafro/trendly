package com.veganafro.contract

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job

interface GenericPresenter : CoroutineScope {

    var fragment: GenericFragment?

    val job: Job

    fun loadData()

    fun subscribe()

    fun unsubscribe()

    fun onDestroy()
}
