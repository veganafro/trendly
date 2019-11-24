package com.veganafro.contract

interface GenericFragment {

    fun onFetchDataStarted()

    fun onFetchDataCompleted()

    fun onFetchDataSuccess(arg: Any?)

    fun onFetchDataError(throwable: Throwable)
}
