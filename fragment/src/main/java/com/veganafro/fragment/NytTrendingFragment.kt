package com.veganafro.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import com.veganafro.contract.GenericActivity
import com.veganafro.contract.GenericFragment
import com.veganafro.controller.NytTrendingPresenter
import com.veganafro.model.NytTopic
import kotlinx.android.synthetic.main.nyt_trending_view.view.nyt_trending_recycler_view
import kotlinx.android.synthetic.main.nyt_trending_view.view.nyt_trending_swipe_refresh_view
import javax.inject.Inject

class NytTrendingFragment @Inject constructor(
    private val presenter: NytTrendingPresenter
) : Fragment(R.layout.nyt_trending_view), GenericFragment {

    private var shortAnimationTime: Int = 0

    private var viewAdapter: RecyclerView.Adapter<*>? = null
    private var viewManager: RecyclerView.LayoutManager? = null

    private var swipeRefreshContainer: SwipeRefreshLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter.fragment = this
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.nyt_trending_view, container, false)

        presenter.coSubscribe()

        viewAdapter = NytTrendingAdapter(::onArticleClickedCallback)
        viewManager = LinearLayoutManager(context)

        swipeRefreshContainer = view.nyt_trending_swipe_refresh_view
        swipeRefreshContainer?.setOnRefreshListener { presenter.coSubscribe() }

        shortAnimationTime = resources.getInteger(android.R.integer.config_shortAnimTime)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // prevent the visibility from being set to GONE if we're being restored from an existing state
        savedInstanceState?.let {} ?: apply {
            view.nyt_trending_recycler_view
                .apply {
                    // this optimization tells the recycler view that all the views it's displaying
                    // are the same size, so it can avoid inflating the whole view layout when its contents
                    // change
                    setHasFixedSize(true)

                    visibility = View.GONE

                    adapter?.let {} ?: apply {
                        adapter = viewAdapter
                    }
                    layoutManager?.let {} ?: apply {
                        layoutManager = viewManager
                    }
                }
            swipeRefreshContainer?.isRefreshing = true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

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

    @SuppressLint("NewApi")
    override fun onFetchDataCompleted() {
        view?.nyt_trending_recycler_view
            .apply {
                this?.apply {
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

    private fun onArticleClickedCallback(article: NytTopic.Article) {
        if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
            (requireActivity() as GenericActivity).goToNytArticleDetails(article)
        }
    }
}
