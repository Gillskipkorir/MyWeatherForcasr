package com.cellulant.myweatherforcast.api

import com.cellulant.myweatherforcast.models.current.CurrentWeatherModel
import com.cellulant.myweatherforcast.models.forcast.ForecastModels
import com.cellulant.myweatherforcast.models.onecall.OneCallResponse
import com.cellulant.myweatherforcast.utils.Constants.API_KEY
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherAPI {

    @GET("weather")
    fun getCurrentWeather(
        @Query("q")
        q: String?,
        @Query("units")
        units: String?,
        @Query("lang")
        lang: String?,
        @Query("appid")
        appId: String
    ): Call<CurrentWeatherModel>


    @GET("forecast")
    suspend fun getFiveDaysWeather(
        @Query("q")
        q: String?,
        @Query("units")
        units: String?,
        @Query("lang")
        lang: String?,
        @Query("appid")
        appId: String = API_KEY
    ): Response<ForecastModels?>?

    @GET("onecall")
    suspend fun getOnceCallWeather(
        @Query("lat")
        lat: String?,
        @Query("lon")
        lon: String?,
        @Query("units")
        units: String?,
        @Query("lang")
        lang: String?,
        @Query("exclude")
        exclude: String?,
        @Query("appid")
        appId: String = API_KEY
    ): Response<OneCallResponse?>?
}