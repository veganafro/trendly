package com.veganafro.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.toolbar
import kotlinx.android.synthetic.main.activity_main.fragment_container

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)
        toolbar.setTitleTextColor(resources.getColor(R.color.colorPrimary))
        supportActionBar?.title = "trendly"

        fragment_container?.let {
            savedInstanceState?.let {
                return
            }

            val recyclerFragment = NytTrendingFragment()
            recyclerFragment.arguments = intent.extras
            supportFragmentManager
                .beginTransaction()
                .add(it.id, recyclerFragment)
                .commit()
        }
    }
}
