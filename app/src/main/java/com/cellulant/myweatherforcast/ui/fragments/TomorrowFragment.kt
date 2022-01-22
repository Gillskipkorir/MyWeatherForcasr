package com.cellulant.myweatherforcast.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewbinding.ViewBinding
import com.cellulant.myweatherforcast.adapter.OneCallAdapter
import com.cellulant.myweatherforcast.adapter.WeatherAdapter
import com.cellulant.myweatherforcast.databinding.FragmentTommorowBinding
import com.cellulant.myweatherforcast.ui.BindingFragment
import com.cellulant.myweatherforcast.ui.activity.MainActivity
import com.cellulant.myweatherforcast.ui.viewmodels.MainViewModel
import com.cellulant.myweatherforcast.utils.Resource
import timber.log.Timber


class TomorrowFragment : BindingFragment<FragmentTommorowBinding>() {
    lateinit var viewModel: MainViewModel
    private lateinit var weatherAdapter: WeatherAdapter

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentTommorowBinding::inflate


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        setUpRecyclerview()

        viewModel.getOneCallForeCast()
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