package com.veganafro.networking.presenter

import android.util.Log
import com.veganafro.model.NytTopic
import com.veganafro.networking.nyt.DaggerNytNetworkingComponent
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class MainActivityPresenter @Inject constructor() {
    @Inject lateinit var nytMostShared: Call<List<NytTopic>>

    init {
        DaggerNytNetworkingComponent.create().inject(this)
        callNytMostShared()
    }

    private fun callNytMostShared() {
        nytMostShared.enqueue(object : Callback<List<NytTopic>> {
            override fun onFailure(call: Call<List<NytTopic>>, t: Throwable) {
                // TODO("not implemented")
                Log.v("nyt call unsuccessful", "MainActivityPresenter")
            }

            override fun onResponse(call: Call<List<NytTopic>>, response: Response<List<NytTopic>>) {
                // TODO("not implemented")
                Log.v("nyt call successful", "MainActivityPresenter")
            }
        })
    }
}