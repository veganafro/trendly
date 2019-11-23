package com.veganafro.injector

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.veganafro.fragment.NytTrendingFragment
import javax.inject.Inject
import javax.inject.Provider

class TrendlyFragmentFactory @Inject constructor(
    private val nytTrendingFragmentProvider: Provider<NytTrendingFragment>
) : FragmentFactory() {

    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return when (className) {
            NytTrendingFragment::class.java.canonicalName -> nytTrendingFragmentProvider.get()
            else -> TODO("Missing fragment $className")
        }
    }
}
