package com.veganafro.controller.generic

interface GenericView {

    fun onFetchDataStarted()

    fun onFetchDataCompleted()

    fun onFetchDataSuccess(arg: Any?)

    fun onFetchDataError(throwable: Throwable)
}
