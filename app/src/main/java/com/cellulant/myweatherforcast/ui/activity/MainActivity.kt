package com.cellulant.myweatherforcast.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.cellulant.myweatherforcast.R
import com.cellulant.myweatherforcast.databinding.ActivityMainBinding
import com.cellulant.myweatherforcast.repositories.MainRepository
import com.cellulant.myweatherforcast.ui.viewmodels.MainViewModel
import com.cellulant.myweatherforcast.ui.viewmodels.MainViewModelProviderFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mainRepository = MainRepository()
        val viewModelProviderFactory = MainViewModelProviderFactory(application, mainRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory)[MainViewModel::class.java]

        val navController =
            (supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment).findNavController()
        binding.bottomNavigationView.apply {
            setupWithNavController(navController)
            setOnItemReselectedListener { /* NO-OP */ }
        }
    }

    override fun onStop() {
        super.onStop()
        val sharedPref = this.getSharedPreferences("DefaultLocation", 0)
        sharedPref
            .edit()
            .apply {
                clear()
                apply()
            }
    }
}