package com.veganafro.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.veganafro.fragment.NytTrendingFragment
import com.veganafro.injector.DaggerTrendlyComponent
import com.veganafro.injector.TrendlyComponent
import kotlinx.android.synthetic.main.activity_main.toolbar
import kotlinx.android.synthetic.main.activity_main.fragment_container

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val dagger: TrendlyComponent = DaggerTrendlyComponent.create()

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

            val recyclerFragment = supportFragmentManager.fragmentFactory.instantiate(
                classLoader, NytTrendingFragment::class.java.canonicalName!!
            )
            recyclerFragment.arguments = intent.extras
            supportFragmentManager
                .beginTransaction()
                .add(it.id, recyclerFragment)
                .commit()
        }
    }
}
