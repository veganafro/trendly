package com.veganafro.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.veganafro.injector.DaggerTrendlyComponent

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        DaggerTrendlyComponent.create().inject(this)
        DaggerTrendlyComponent.create().mainActivityPresenter().loadData()
    }
}
