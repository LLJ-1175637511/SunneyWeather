package com.example.sunnywhether.logic.network

import com.example.sunnywhether.SWApplication
import com.example.sunnywhether.logic.model.DailyResponse
import com.example.sunnywhether.logic.model.PlaceResponse
import com.example.sunnywhether.logic.model.RealtimeResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface WeatherService {
    @GET("v2.5/${SWApplication.TOKEN}/{lng},{lat}/realtime.json")
    fun getRealtimeWeather(@Path("lng") lng:String,@Path("lat") lat:String):Call<RealtimeResponse>
    @GET("v2.5/${SWApplication.TOKEN}/{lng},{lat}/daily.json")
    fun getDailyWeather(@Path("lng") lng:String,@Path("lat") lat:String):Call<DailyResponse>
}