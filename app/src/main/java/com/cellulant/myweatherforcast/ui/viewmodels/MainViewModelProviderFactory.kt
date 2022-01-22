package com.cellulant.myweatherforcast.ui.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cellulant.myweatherforcast.repositories.MainRepository

class MainViewModelProviderFactory(
    val app: Application,
    val mainRepository: MainRepository
) : ViewModelProvider.Factory {


    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(app,mainRepository) as T
    }
}