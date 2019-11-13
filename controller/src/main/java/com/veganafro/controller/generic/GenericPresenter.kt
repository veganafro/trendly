package com.veganafro.controller.generic

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job

interface GenericPresenter : CoroutineScope {

    var view: GenericView?

    val job: Job

    fun loadData()

    fun subscribe()

    fun unsubscribe()

    fun onDestroy()
}
