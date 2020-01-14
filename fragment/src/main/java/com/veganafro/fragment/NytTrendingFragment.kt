package com.veganafro.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.veganafro.contract.GenericActivity
import com.veganafro.controller.NytTrendingViewModel
import com.veganafro.model.NytTopic
import kotlinx.android.synthetic.main.nyt_trending_view.view.nyt_trending_recycler_view
import kotlinx.android.synthetic.main.nyt_trending_view.view.nyt_trending_swipe_refresh_view
import javax.inject.Inject
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer

class NytTrendingFragment @Inject constructor(
    private val viewModelFactory: ViewModelProvider.Factory
) : Fragment(R.layout.nyt_trending_view) {

    private var shortAnimationTime: Int = 0

    private var viewAdapter: RecyclerView.Adapter<*>? = null
    private var viewManager: RecyclerView.LayoutManager? = null

    private var swipeRefreshContainer: SwipeRefreshLayout? = null

    private val viewModel: NytTrendingViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.getMostShared().observe(this, Observer {
            (viewAdapter as NytTrendingAdapter).submitList(it)

            view?.nyt_trending_recycler_view
                .apply {
                    if (this?.visibility!!.equals(View.GONE)) {
                        alpha = 0f
                        visibility = View.VISIBLE
                        animate()
                            .alpha(1f)
                            .setDuration(shortAnimationTime.toLong())
                            .setListener(null)
                    }

                    swipeRefreshContainer?.isRefreshing = false
                }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.nyt_trending_view, container, false)

        viewAdapter = NytTrendingAdapter(::onArticleClickedCallback)
        viewManager = LinearLayoutManager(context)

        swipeRefreshContainer = view.nyt_trending_swipe_refresh_view
        swipeRefreshContainer?.setOnRefreshListener { viewModel.refreshMostShared() }

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
            viewModel.fetchMostShared()
            swipeRefreshContainer?.isRefreshing = true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        viewAdapter = null
        viewManager = null

        swipeRefreshContainer?.isRefreshing = false
        swipeRefreshContainer = null
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.getMostShared().removeObservers(this)
    }

    private fun onArticleClickedCallback(article: NytTopic.Article) {
        if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
            (requireActivity() as GenericActivity).goToNytArticleDetails(article)
        }
    }
}
