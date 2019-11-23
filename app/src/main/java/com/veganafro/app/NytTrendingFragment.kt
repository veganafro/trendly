package com.veganafro.app

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import com.veganafro.contract.GenericView
import com.veganafro.controller.NytTrendingPresenter
import com.veganafro.injector.DaggerTrendlyComponent
import com.veganafro.model.NytTopic
import kotlinx.android.synthetic.main.nyt_trending_view.view.nyt_trending_recycler_view
import kotlinx.android.synthetic.main.nyt_trending_view.view.nyt_trending_swipe_refresh_view

class NytTrendingFragment
    : Fragment(R.layout.nyt_trending_view), GenericView {

    private var shortAnimationTime: Int = 0

    private var viewAdapter: RecyclerView.Adapter<*>? = null
    private var viewManager: RecyclerView.LayoutManager? = null

    private var swipeRefreshContainer: SwipeRefreshLayout? = null

    private var presenter: NytTrendingPresenter = DaggerTrendlyComponent
        .create()
        .nytTrendingPresenter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?)
            : View? {
        val view: View = inflater.inflate(R.layout.nyt_trending_view, container, false)

        presenter.view = this
        presenter.coSubscribe()

        viewAdapter = NytTrendingAdapter()
        viewManager = LinearLayoutManager(context)

        swipeRefreshContainer = view.nyt_trending_swipe_refresh_view
        swipeRefreshContainer?.setOnRefreshListener { presenter.coSubscribe() }

        shortAnimationTime = resources.getInteger(android.R.integer.config_longAnimTime)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // prevent the visibility from being set to GONE if we're being restored from an existing state
        savedInstanceState?.let {} ?: apply {
            view.nyt_trending_recycler_view.visibility = View.GONE
        }
        swipeRefreshContainer?.isRefreshing = true
    }

    override fun onDestroyView() {
        super.onDestroyView()

        presenter.view = null
        presenter.coUnsubscribe()

        viewAdapter = null
        viewManager = null

        swipeRefreshContainer?.isRefreshing = false
        swipeRefreshContainer = null
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.coOnDestroy()
    }

    override fun onFetchDataStarted() {
        Log.v("Trendly|NytTF", "called onFetchDataStarted")
    }

    override fun onFetchDataCompleted() {
        view?.nyt_trending_recycler_view
            .apply {
                this?.apply {
                    // this optimization tells the recycler view that all the views it's displaying
                    // are the same size, so it can avoid inflating the whole view layout when its contents
                    // change
                    setHasFixedSize(true)
                    adapter?.apply {} ?: run { this.adapter = viewAdapter }
                    layoutManager?.apply {} ?: run { this.layoutManager = viewManager }

                    if (visibility.equals(View.GONE)) {
                        alpha = 0f
                        visibility = View.VISIBLE
                        animate()
                            .alpha(1f)
                            .setDuration(shortAnimationTime.toLong())
                            .setListener(null)
                    }

                    swipeRefreshContainer?.isRefreshing = false
                }
            }
    }

    @Suppress("UNCHECKED_CAST")
    override fun onFetchDataSuccess(arg: Any?) {
        (viewAdapter as NytTrendingAdapter).submitList(arg as MutableList<NytTopic.Article>)
    }

    override fun onFetchDataError(throwable: Throwable) {
        Log.e("Trendly|NytTF", "Data fetching error: ${throwable.message}")
        swipeRefreshContainer?.isRefreshing = false
        Snackbar.make(view!!, "Sorry, couldn't refresh", Snackbar.LENGTH_SHORT)
            .show()
    }
}
