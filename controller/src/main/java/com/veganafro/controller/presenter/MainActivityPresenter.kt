package com.veganafro.controller.presenter

import android.util.Log
import com.veganafro.model.NytTopic
import com.veganafro.controller.nyt.DaggerNytNetworkingComponent
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class MainActivityPresenter @Inject constructor() {
    @Inject lateinit var nytMostShared: Call<NytTopic>

    init {
        DaggerNytNetworkingComponent.create().inject(this)
        callNytMostShared()
    }

    private fun callNytMostShared() {
        nytMostShared.enqueue(object : Callback<NytTopic> {
            override fun onFailure(call: Call<NytTopic>, t: Throwable) {
                // TODO("not implemented")
                Log.v("MainActivityPresenter", "nyt call unsuccessful: ${call.request().url()} | ${call.request().headers()} | ${t.localizedMessage}")
            }

            override fun onResponse(call: Call<NytTopic>, response: Response<NytTopic>) {
                // TODO("not implemented")
                Log.v("MainActivityPresenter", "nyt call successful: ${response.code()} | ${response.raw()} | ${response.body()}")
            }
        })
    }
}