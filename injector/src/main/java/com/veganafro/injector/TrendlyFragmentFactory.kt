package com.veganafro.injector

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.veganafro.fragment.NytArticleDetailsFragment
import com.veganafro.fragment.NytTrendingFragment
import javax.inject.Inject
import javax.inject.Provider

class TrendlyFragmentFactory @Inject constructor(
    private val nytTrendingFragmentProvider: Provider<NytTrendingFragment>,
    private val nytArticleDetailsFragmentProvider: Provider<NytArticleDetailsFragment>
) : FragmentFactory() {

    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return when (className) {
            NytTrendingFragment::class.java.canonicalName -> nytTrendingFragmentProvider.get()
            NytArticleDetailsFragment::class.java.canonicalName -> nytArticleDetailsFragmentProvider.get()
            else -> TODO("Missing fragment $className")
        }
    }
}
