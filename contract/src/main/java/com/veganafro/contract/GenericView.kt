package com.veganafro.contract

interface GenericView {

    fun onFetchDataStarted()

    fun onFetchDataCompleted()

    fun onFetchDataSuccess(arg: Any?)

    fun onFetchDataError(throwable: Throwable)
}
