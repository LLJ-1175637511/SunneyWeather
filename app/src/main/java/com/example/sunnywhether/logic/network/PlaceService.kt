package com.example.sunnywhether.logic.network

import com.example.sunnywhether.logic.model.PlaceResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PlaceService {
    @GET()
    fun searchPlaces(@Query("query") query: String): Call<PlaceResponse>

}