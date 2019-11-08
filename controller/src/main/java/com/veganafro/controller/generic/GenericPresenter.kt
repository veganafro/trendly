package com.veganafro.controller.generic

import kotlinx.coroutines.CoroutineScope

interface GenericPresenter : CoroutineScope {

    var view: GenericView?

    fun loadData()

    fun subscribe()

    fun unsubscribe()

    fun onDestroy()
}
