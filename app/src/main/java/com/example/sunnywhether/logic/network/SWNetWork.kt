package com.example.sunnywhether.logic.network

import retrofit2.await
import retrofit2.http.Query

object SWNetWork {
    private val placeService = ServiceCreator.create<PlaceService>()

    suspend fun searchPlaces(query: String) = placeService.searchPlaces(query).await()


}