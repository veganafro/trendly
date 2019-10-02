@file:Suppress("UNCHECKED_CAST")

package com.veganafro.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.veganafro.controller.generic.GenericView
import com.veganafro.controller.implementation.MainActivityPresenter
import com.veganafro.injector.DaggerTrendlyComponent
import com.veganafro.model.NytTopic

class NytTrendingFragment
    : Fragment(R.layout.nyt_trending_view), GenericView {

    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    private var presenter: MainActivityPresenter = DaggerTrendlyComponent
        .create()
        .mainActivityPresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        presenter.setView(this)
        presenter.subscribe()

        viewManager = LinearLayoutManager(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?)
            : View? {
        return inflater.inflate(R.layout.nyt_trending_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onFetchDataStarted() {
    }

    override fun onFetchDataCompleted() {
    }

    override fun onFetchDataSuccess(arg: Any?) {
        viewAdapter = NytTrendingAdapter(arg as List<NytTopic.Article>)

        view!!.findViewById<RecyclerView>(R.id.nyt_trending_recycler_view)
            .apply {
                setHasFixedSize(true)
                adapter = viewAdapter
                layoutManager = viewManager
            }
    }

    override fun onFetchDataError(throwable: Throwable) {
    }
}