package com.cellulant.myweatherforcast.ui.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView.OnEditorActionListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.cellulant.myweatherforcast.R
import com.cellulant.myweatherforcast.api.RetrofitInstance
import com.cellulant.myweatherforcast.databinding.ActivityMainBinding
import com.cellulant.myweatherforcast.models.current.CurrentWeatherModel
import com.cellulant.myweatherforcast.repositories.MainRepository
import com.cellulant.myweatherforcast.ui.viewmodels.MainViewModel
import com.cellulant.myweatherforcast.utils.Constants.API_KEY
import com.cellulant.myweatherforcast.utils.Constants.DEFAULT_LOCATION
import com.cellulant.myweatherforcast.utils.Constants.IMAGE_BASE_URL
import com.cellulant.myweatherforcast.utils.Constants.LANGUAGE
import com.cellulant.myweatherforcast.utils.Constants.UNIT_MEASURE
import com.cellulant.myweatherforcast.ui.viewmodels.MainViewModelProviderFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import java.util.*

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


        getCurrentWeather(DEFAULT_LOCATION)
        searchCity()
    }

    private fun searchCity() {
        binding.etSearch.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                hideKeyboard()
                searchAction()
                return@OnEditorActionListener true
            }
            false
        })
    }

    private fun searchAction() {
        val typedCity = binding.etSearch.text.toString()
        if (typedCity.isEmpty()) {
            Toast.makeText(this, "Search For City", Toast.LENGTH_SHORT).show()
        } else {
            getCurrentWeather(typedCity)
        }
    }

    private fun hideKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    private fun getCurrentWeather(city: String) {
        val responseCall: Call<CurrentWeatherModel> =
            RetrofitInstance.api.getCurrentWeather(city, UNIT_MEASURE, LANGUAGE, API_KEY)
        responseCall.enqueue(object : Callback<CurrentWeatherModel?> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(
                call: Call<CurrentWeatherModel?>,
                response: Response<CurrentWeatherModel?>,
            ) =
                if (response.isSuccessful) {
                    val weather = response.body()

                    /* todo: Convert Time String to */

                    val windSpeed = "Wind: ${weather?.wind?.speed} m/s"
                    val pressure = "Pressure: ${weather?.main?.pressure} hPa"
                    val humidity = "Humidity: ${weather?.wind?.speed} %"
                    val sunset = "Sunrise: ${weather?.sys?.sunset}"
                    val sunrise = "Sunset: ${weather?.sys?.sunrise}"
                    val cityName = "${weather?.name}, ${weather?.sys?.country}"
                    val temp = "${weather?.main?.temp} °C"
                    val imageUrl = "$IMAGE_BASE_URL${weather?.weather?.get(0)?.icon}.png"

                    Glide.with(this@MainActivity).load(imageUrl).into(binding.icon)

                    binding.cloudCounts.text = weather?.weather?.get(0)?.description
                    binding.tempTextView.text = temp
                    binding.windSpeed.text = windSpeed
                    binding.humidity.text = humidity
                    binding.pressure.text = pressure
                    binding.sunrise.text = sunrise
                    binding.sunset.text = sunset
                    binding.place.text = cityName


                } else {
                    Toast.makeText(this@MainActivity, "Error Occurred", Toast.LENGTH_SHORT).show()
                }

            override fun onFailure(call: Call<CurrentWeatherModel?>, t: Throwable) {
                Timber.d("Failed due to: ${t.localizedMessage}")
            }
        })
    }


}