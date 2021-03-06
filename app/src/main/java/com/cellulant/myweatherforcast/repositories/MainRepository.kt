package com.cellulant.myweatherforcast.repositories

import com.cellulant.myweatherforcast.api.RetrofitInstance
import com.cellulant.myweatherforcast.models.forcast.ForecastModels
import com.cellulant.myweatherforcast.models.onecall.OneCallResponse
import com.cellulant.myweatherforcast.utils.Constants.API_KEY
import com.cellulant.myweatherforcast.utils.Constants.DEFAULT_LOCATION
import com.cellulant.myweatherforcast.utils.Constants.LANGUAGE
import com.cellulant.myweatherforcast.utils.Constants.UNIT_MEASURE
import retrofit2.Response

class MainRepository {

    suspend fun getForecast(city: String): Response<ForecastModels?>? {

        return RetrofitInstance.api.getFiveDaysWeather(
            city,
            UNIT_MEASURE,
            LANGUAGE,
            API_KEY
        )
    }


    suspend fun getFullForecast(): Response<ForecastModels?>? {
        return RetrofitInstance.api.getThirtyDaysWeather(
            DEFAULT_LOCATION,
            UNIT_MEASURE,
            LANGUAGE,
            API_KEY
        )

    }


    suspend fun getOneCAllForecast(): Response<OneCallResponse?>? {
        val oneCallWeather =
            RetrofitInstance.api.getOnceCallWeather(
                "-1.2833",
                "36.8167",
                UNIT_MEASURE,
                LANGUAGE,
                "daily,minutely,alerts",
                API_KEY
            )
        return oneCallWeather
    }

}