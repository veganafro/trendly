package com.veganafro.controller.generic

interface GenericPresenter {

    fun loadData()

    fun subscribe()

    fun unsubscribe()

    fun onDestroy()
}
