package com.veganafro.injector

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.veganafro.controller.NytTrendingViewModel
import javax.inject.Inject
import javax.inject.Provider

class TrendlyViewModelFactory @Inject constructor(
    private val nytTrendingViewModel: Provider<NytTrendingViewModel>
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when (modelClass) {
            NytTrendingViewModel::class.java -> nytTrendingViewModel.get()
            else -> TODO("Missing ViewModel $modelClass")
        } as T
    }

}
