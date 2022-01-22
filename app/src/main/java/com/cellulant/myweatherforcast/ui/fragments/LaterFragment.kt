package com.cellulant.myweatherforcast.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.cellulant.myweatherforcast.adapter.WeatherAdapter
import com.cellulant.myweatherforcast.api.RetrofitInstance
import com.cellulant.myweatherforcast.databinding.FragmentLaterBinding
import com.cellulant.myweatherforcast.models.current.CurrentWeatherModel
import com.cellulant.myweatherforcast.ui.BindingFragment
import com.cellulant.myweatherforcast.ui.activity.MainActivity
import com.cellulant.myweatherforcast.ui.viewmodels.MainViewModel
import com.cellulant.myweatherforcast.utils.Constants
import com.cellulant.myweatherforcast.utils.Resource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class LaterFragment : BindingFragment<FragmentLaterBinding>() {
    private lateinit var viewModel: MainViewModel
    private lateinit var weatherAdapter: WeatherAdapter
    private lateinit var place: String


    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentLaterBinding::inflate


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        setUpRecyclerview()
        fetchFromSharedPrefs()

        searchCity()
        getCurrentWeather(place)
        viewModel.getForeCast(place)
        subscribeToObservers()
    }

    private fun fetchFromSharedPrefs() {
        requireContext().getSharedPreferences("DefaultLocation", 0).apply {
            getString("Place", "").let { binding.place.text = it }
            getString("Description", "").let { binding.cloudCounts.text = it }
            getString("Temperature", "").let { binding.tempTextView.text = it }
            getString("Humidity", "").let { binding.humidity.text = it }
            getString("Pressure", "").let { binding.pressure.text = it }
            getString("WindSpend", "").let { binding.windSpeed.text = it }
            getString("Sunrise", "").let { binding.sunrise.text = it }
            getString("Sunset", "").let { binding.sunset.text = it }
            getString("UpdatedOn", "").let { binding.lastUpdate.text = it }

            getString("Place", "").let { place = it.toString() }
            getString("IconUrl", "").let {
                Glide.with(requireContext()).load(it).into(binding.icon)
            }
        }
    }

    private fun searchCity() {
        binding.etSearch.setOnEditorActionListener(TextView.OnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchAction()
                return@OnEditorActionListener true
            }
            false
        })
    }

    private fun searchAction() {
        val typedCity = binding.etSearch.text.toString()
        if (typedCity.isEmpty()) {
            Toast.makeText(requireContext(), "Enter City Name", Toast.LENGTH_SHORT).show()
        } else {
            getCurrentWeather(typedCity)
            viewModel.getForeCast(typedCity)
        }
    }

    private fun getCurrentWeather(city: String) {
        val responseCall: Call<CurrentWeatherModel> =
            RetrofitInstance.api.getCurrentWeather(
                city,
                Constants.UNIT_MEASURE,
                Constants.LANGUAGE,
                Constants.API_KEY
            )
        responseCall.enqueue(object : Callback<CurrentWeatherModel?> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(
                call: Call<CurrentWeatherModel?>,
                response: Response<CurrentWeatherModel?>,
            ) =
                if (response.isSuccessful) {
                    val weather = response.body()

                    val dateFormat: DateFormat =
                        SimpleDateFormat("yyyy-MM-dd hh:mm", Locale.getDefault())
                    val myDate = dateFormat.format(Date())

                    val windSpeed = "Wind: ${weather?.wind?.speed} m/s"
                    val pressure = "Pressure: ${weather?.main?.pressure} hPa"
                    val humidity = "Humidity: ${weather?.wind?.speed} %"
                    val sunset = "Sunrise: ${weather?.sys?.sunset}"
                    val sunrise = "Sunset: ${weather?.sys?.sunrise}"
                    val place = "${weather?.name}, ${weather?.sys?.country}"
                    val temp = "${weather?.main?.temp} °C"
                    val lastUpdatedOn = "Last Updated:$myDate"
                    val imageUrl =
                        "${Constants.IMAGE_BASE_URL}${weather?.weather?.get(0)?.icon}.png"
                    val desc = weather?.weather?.get(0)?.description

                    //Set Image Using Glide
                    Glide.with(requireContext()).load(imageUrl).into(binding.icon)

                    binding.cloudCounts.text = desc
                    binding.tempTextView.text = temp
                    binding.windSpeed.text = windSpeed
                    binding.humidity.text = humidity
                    binding.pressure.text = pressure
                    binding.sunrise.text = sunrise
                    binding.sunset.text = sunset
                    binding.place.text = place

                    binding.lastUpdate.text = lastUpdatedOn

                    //Saving the data in shared Prefs
                    requireContext()
                        .getSharedPreferences("DefaultLocation", 0)
                        .edit()
                        .putString("Place", place)
                        .putString("City", "${weather?.name}")
                        .putString("Description", desc)
                        .putString("Temperature", temp)
                        .putString("Humidity", humidity)
                        .putString("Pressure", pressure)
                        .putString("WindSpend", windSpeed)
                        .putString("IconUrl", imageUrl)
                        .putString("Sunrise", sunrise)
                        .putString("Sunset", sunset)
                        .putString("UpdatedOn", lastUpdatedOn)
                        .apply()


                } else {
                    Toast.makeText(requireContext(), "Error Occurred", Toast.LENGTH_SHORT).show()
                }

            override fun onFailure(call: Call<CurrentWeatherModel?>, t: Throwable) {
                Timber.d("Failed due to: ${t.localizedMessage}")
            }
        })
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