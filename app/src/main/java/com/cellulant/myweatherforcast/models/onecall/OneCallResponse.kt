package com.cellulant.myweatherforcast.models.onecall

data class OneCallResponse(
    val current: Current,
    val hourly: MutableList<Hourly>,
    val lat: Double,
    val lon: Double,
    val timezone: String,
    val timezone_offset: Int
)