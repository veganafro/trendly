package com.veganafro.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.veganafro.contract.GenericActivity
import com.veganafro.fragment.NytArticleDetailsFragment
import com.veganafro.fragment.NytTrendingFragment
import com.veganafro.injector.DaggerTrendlyComponent
import com.veganafro.injector.TrendlyComponent
import kotlinx.android.synthetic.main.activity_main.toolbar
import kotlinx.android.synthetic.main.activity_main.fragment_container

class MainActivity
    : AppCompatActivity(), GenericActivity {

    private val dagger: TrendlyComponent = DaggerTrendlyComponent.create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // insert the fragment factory
        supportFragmentManager.fragmentFactory = dagger.fragmentFactory()

        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)
        toolbar.setTitleTextColor(
            ContextCompat.getColor(this, R.color.colorPrimary)
        )
        supportActionBar?.title = "trendly"

        fragment_container?.let {
            // if the activity is being restored from a different state,
            // don't do anything and return, otherwise there could be overlapping
            // fragments
            savedInstanceState?.let {
                return
            }

            val recyclerFragment = supportFragmentManager
                .fragmentFactory
                .instantiate(
                    classLoader,
                    NytTrendingFragment::class.java.canonicalName!!
                )
            recyclerFragment.arguments = intent.extras
            supportFragmentManager
                .beginTransaction()
                .add(it.id, recyclerFragment)
                .commit()
        }
    }

    override fun goNytArticleDetails(title: String) {
        fragment_container?.let {
            val detailsFragment = supportFragmentManager
                .fragmentFactory
                .instantiate(
                    classLoader,
                    NytArticleDetailsFragment::class.java.canonicalName!!
                )
            detailsFragment.arguments = intent.extras
            supportFragmentManager
                .beginTransaction()
                .addToBackStack(detailsFragment.tag)
                .replace(it.id, detailsFragment)
                .commit()
        }
    }
}
