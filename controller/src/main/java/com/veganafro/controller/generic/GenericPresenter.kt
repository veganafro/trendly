package com.veganafro.controller.generic

abstract class GenericPresenter {

    abstract fun loadData()

    abstract fun subscribe()

    abstract fun unsubscribe()

    abstract fun onDestroy()
}