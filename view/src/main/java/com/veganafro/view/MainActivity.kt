package com.veganafro.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.veganafro.controller.presenter.MainActivityPresenter

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val presenter = MainActivityPresenter()
    }
}
