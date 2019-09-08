package com.veganafro.controller.presenter

interface GenericPresenter {

    fun loadData()

    fun subscribe()

    fun unsubscribe()

    fun onDestroy()
}