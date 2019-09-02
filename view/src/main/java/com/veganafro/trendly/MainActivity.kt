package com.veganafro.trendly

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.veganafro.networking.presenter.MainActivityPresenter

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val presenter = MainActivityPresenter()
    }
}
