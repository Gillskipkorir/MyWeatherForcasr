package com.cellulant.myweatherforcast.models.onecall

data class Weather(
    val description: String,
    val icon: String,
    val id: Int,
    val main: String
)