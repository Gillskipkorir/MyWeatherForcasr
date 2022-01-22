package com.cellulant.myweatherforcast.models.forcast

data class ForecastModels(
    val city: City,
    val cnt: Int,
    val cod: String,
    val list: MutableList<MainResponse>,
    val message: Int
)