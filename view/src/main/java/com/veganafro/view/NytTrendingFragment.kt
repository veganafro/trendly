@file:Suppress("UNCHECKED_CAST")

package com.veganafro.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.veganafro.controller.generic.GenericView
import com.veganafro.controller.implementation.MainActivityPresenter
import com.veganafro.injector.DaggerTrendlyComponent
import com.veganafro.model.NytTopic
import kotlinx.android.synthetic.main.nyt_trending_view.view.nyt_trending_recycler_view
import kotlinx.android.synthetic.main.nyt_trending_view.view.nyt_trending_swipe_refresh_view

class NytTrendingFragment
    : Fragment(R.layout.nyt_trending_view), GenericView {

    private var shortAnimationTime: Int = 0

    private var viewAdapter: RecyclerView.Adapter<*>? = null
    private lateinit var viewManager: RecyclerView.LayoutManager

    private lateinit var swipeRefreshContainer: SwipeRefreshLayout

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
        val view: View = inflater.inflate(R.layout.nyt_trending_view, container, false)

        swipeRefreshContainer = view.nyt_trending_swipe_refresh_view
        swipeRefreshContainer.setOnRefreshListener { presenter.subscribe() }

        view.nyt_trending_recycler_view.visibility = View.GONE
        shortAnimationTime = resources.getInteger(android.R.integer.config_longAnimTime)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        swipeRefreshContainer.isRefreshing = true
    }

    override fun onFetchDataStarted() {
    }

    override fun onFetchDataCompleted() {
        view?.nyt_trending_recycler_view
            .apply {
                this?.apply {
                    setHasFixedSize(true)
                    adapter?.apply {} ?: run { this.adapter = viewAdapter }
                    layoutManager?.apply {} ?: run { this.layoutManager = viewManager}

                    alpha = 0f
                    visibility = View.VISIBLE
                    animate()
                        .alpha(1f)
                        .setDuration(shortAnimationTime.toLong())
                        .setListener(null)

                    swipeRefreshContainer.isRefreshing = false
                }
            }
    }

    override fun onFetchDataSuccess(arg: Any?) {
        viewAdapter?.apply {
            (this as NytTrendingAdapter).updateData(arg as MutableList<NytTopic.Article>)
        } ?: run {
            viewAdapter = NytTrendingAdapter(arg as MutableList<NytTopic.Article>)
        }
    }

    override fun onFetchDataError(throwable: Throwable) {
        Log.v("NytTrendingFragment", "Data fetching error: ${throwable.message}")
    }
}
