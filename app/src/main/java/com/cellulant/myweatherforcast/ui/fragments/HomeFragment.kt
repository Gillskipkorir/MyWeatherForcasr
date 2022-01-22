package com.cellulant.myweatherforcast.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewbinding.ViewBinding
import com.cellulant.myweatherforcast.adapter.WeatherAdapter
import com.cellulant.myweatherforcast.databinding.FragmentHomeBinding
import com.cellulant.myweatherforcast.repositories.MainRepository
import com.cellulant.myweatherforcast.ui.BindingFragment
import com.cellulant.myweatherforcast.ui.viewmodels.MainViewModel
import com.cellulant.myweatherforcast.ui.viewmodels.MainViewModelProviderFactory
import com.cellulant.myweatherforcast.utils.Resource
import timber.log.Timber


class HomeFragment : BindingFragment<FragmentHomeBinding>() {
    lateinit var viewModel: MainViewModel
    private lateinit var weatherAdapter: WeatherAdapter

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentHomeBinding::inflate


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mainRepository = MainRepository()
        val viewModelProviderFactory =
            MainViewModelProviderFactory(activity?.application!!, mainRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory)[MainViewModel::class.java]

        setUpRecyclerview()
        viewModel.getForeCast()
        subscribeToObservers()
    }

    private fun subscribeToObservers() {
        viewModel.forcast.observe(viewLifecycleOwner, { response ->
            when (response) {
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    response.data?.let {
                        weatherAdapter.differ.submitList(it.list.toList())
                    }
                }
                is Resource.Error -> {
                    binding.progressBar.visibility = View.GONE
                    response.message?.let { message ->
                        Timber.d("An error occurred $message")
                        Toast.makeText(activity, "An error occurred $message", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
            }
        })

    }

    private fun setUpRecyclerview() {
        weatherAdapter = WeatherAdapter()
        binding.rvWeather.apply {
            adapter = weatherAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

}