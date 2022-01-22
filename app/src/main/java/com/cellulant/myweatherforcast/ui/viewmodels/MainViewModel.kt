package com.cellulant.myweatherforcast.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cellulant.myweatherforcast.models.forcast.ForecastModels
import com.cellulant.myweatherforcast.models.onecall.OneCallResponse
import com.cellulant.myweatherforcast.repositories.MainRepository
import com.cellulant.myweatherforcast.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException


class MainViewModel(app: Application, val mainRepository: MainRepository) :
    AndroidViewModel(app) {

    val forcast: MutableLiveData<Resource<ForecastModels>> = MutableLiveData()
    var forcastResponse: ForecastModels? = null

    val fullforecast: MutableLiveData<Resource<ForecastModels>> = MutableLiveData()
    var fullforecastResponse: ForecastModels? = null


    val oneCallForecast: MutableLiveData<Resource<OneCallResponse>> = MutableLiveData()
    var oneCallForecastResponse: OneCallResponse? = null


    fun getForeCast() = viewModelScope.launch {
        safeGetForecast()
    }

    fun getFullForeCast() = viewModelScope.launch {
        safeGetALLForecast()
    }

    fun getOneCallForeCast() = viewModelScope.launch {
        safeGetOneCallForecast()
    }

    private suspend fun safeGetForecast() {
        forcast.postValue(Resource.Loading())
        try {
            val response = mainRepository.getForecast()
            forcast.postValue(handleWeatherResponse(response))
        } catch (t: Throwable) {
            when (t) {
                is IOException -> forcast.postValue(Resource.Error("Network failure"))
                else -> forcast.postValue(Resource.Error("Conversion error"))
            }
        }
    }

    private suspend fun safeGetALLForecast() {
        fullforecast.postValue(Resource.Loading())
        try {
            val response = mainRepository.getForecast()
            fullforecast.postValue(handleFullWeatherResponse(response))
        } catch (t: Throwable) {
            when (t) {
                is IOException -> fullforecast.postValue(Resource.Error("Network failure"))
                else -> fullforecast.postValue(Resource.Error("Conversion error"))
            }
        }
    }


    private fun handleWeatherResponse(response: Response<ForecastModels?>?): Resource<ForecastModels> {

        if (response != null) {
            if (response.isSuccessful) {
                response.body()?.let {
                    val oldData = forcastResponse?.list
                    val newData = it.list
                    oldData?.addAll(newData)
                    return Resource.Success(forcastResponse ?: it)
                }
            }
        }
        return Resource.Error(response?.message())

    }

    private fun handleFullWeatherResponse(response: Response<ForecastModels?>?): Resource<ForecastModels> {

        if (response != null) {
            if (response.isSuccessful) {
                response.body()?.let {
                    val oldData = fullforecastResponse?.list
                    val newData = it.list
                    oldData?.addAll(newData)
                    return Resource.Success(fullforecastResponse ?: it)
                }
            }
        }
        return Resource.Error(response?.message())

    }


    private suspend fun safeGetOneCallForecast() {
        oneCallForecast.postValue(Resource.Loading())
        try {
            val response = mainRepository.getOneCAllForecast()
            oneCallForecast.postValue(handleOneCallWeatherResponse(response))
        } catch (t: Throwable) {
            when (t) {
                is IOException -> oneCallForecast.postValue(Resource.Error("Network failure"))
                else -> oneCallForecast.postValue(Resource.Error("Conversion error"))
            }
        }
    }

    private fun handleOneCallWeatherResponse(response: Response<OneCallResponse?>?): Resource<OneCallResponse> {

        if (response != null) {
            if (response.isSuccessful) {
                response.body()?.let {
                    val oldData = oneCallForecastResponse?.hourly
                    val newData = it.hourly
                    oldData?.addAll(newData)
                    return Resource.Success(oneCallForecastResponse ?: it)
                }
            }
        }
        return Resource.Error(response?.message())

    }

}