package com.example.weathermvvm.network

import com.example.weatherapp.model.WeatherResponse
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface WeatherApi {

    @GET("data/2.5/forecast?")
    fun getWeatherList(
        @Query("q") cityName: String,
        @Query("appid") key:String
    ): Single<WeatherResponse>

}
