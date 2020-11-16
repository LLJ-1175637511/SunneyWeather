package com.example.sunnywhether.logic

import android.util.Log
import androidx.lifecycle.liveData
import com.example.sunnywhether.logic.model.Place
import com.example.sunnywhether.logic.model.Weather
import com.example.sunnywhether.logic.network.SWNetWork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlin.coroutines.CoroutineContext

object Repository {
    fun searchPlaces(query:String) = fire(Dispatchers.IO){
        val placeResponse = SWNetWork.searchPlaces(query)
        if (placeResponse.status == "ok"){
            val places = placeResponse.places
            Result.success(places)
        }else{
            Result.failure<List<Place>>(RuntimeException("response status is ${placeResponse.status}"))
        }
    }

    fun refreshWeather(lng:String,lat:String)= liveData(Dispatchers.IO) {

        val result = try {
            coroutineScope {
                val deferredRealtime = async {
                    SWNetWork.getRealtimeWeather(lng, lat)
                }
                val deferredDaily = async {
                    SWNetWork.getDailyWeather(lng, lat)
                }
                val realtimeResponse = deferredRealtime.await()
                val dailyResponse = deferredDaily.await()
                println("rrStatus:${realtimeResponse.status} drStatus:${dailyResponse.status}")
//                Log.d("Repository","rrStatus:${realtimeResponse.status} drStatus:${dailyResponse.status}")
                if (realtimeResponse.status=="ok"&&dailyResponse.status=="ok"){
                    val weather = Weather(realtimeResponse.result.realtime,dailyResponse.result.daily)
                    Result.success(weather)
                }else{
                    Result.failure<Weather>(RuntimeException("realtime response status is ${realtimeResponse.status}"+
                            "daily response status is ${dailyResponse.status}"))
                }
            }
        }catch (e:Exception){
            Result.failure<Weather>(e)
        }

        Log.d("Repository","isSuc:${result.isSuccess} isNull:${result.getOrNull()}")
        emit(result)
    }

    /**
     * 设置一个统一入口 避免多次try catch
     * place为封装后的调用
     * weather为普通的调用
     */
    private fun <T> fire(context: CoroutineContext,block:suspend()->Result<T>) = liveData<Result<T>> {
        val result = try {
            block()
        }catch (e:Exception){
            Result.failure<T>(e)
        }
        emit(result)
    }
}