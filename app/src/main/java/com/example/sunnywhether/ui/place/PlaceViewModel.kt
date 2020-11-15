package com.example.sunnywhether.ui.place

import android.content.res.AssetManager
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.sunnywhether.getPlaceName
import com.example.sunnywhether.logic.model.Location
import com.example.sunnywhether.logic.model.Place
import jxl.Workbook
import java.lang.Exception
import kotlin.concurrent.thread

class PlaceViewModel : ViewModel() {

    private val searchLiveData = MutableLiveData<String>()

    val placeList = ArrayList<Place>()

    private val placeAllList = mutableListOf<Place>()

    val placeLiveData = Transformations.switchMap(searchLiveData) { query ->
//        Repository.searchPlaces(query)
        val addressList = mutableListOf<Place>()
        placeAllList.forEach {
            if (it.address.contains(query)) {
                addressList.add(it)
            }
        }
        MutableLiveData<MutableList<Place>>(addressList)
    }

    fun searchPlaces(query: String) {
        searchLiveData.value = query
    }

    fun getPlaceExcel(assets: AssetManager) {
        if (placeAllList.isNotEmpty()) return
        try {
            thread {
                val open = assets.open("weather.xls")
                val book = Workbook.getWorkbook(open)
                val allSheets = book.sheets

                allSheets.forEach { sheet ->
                    val rows = sheet.rows
                    val cols = sheet.columns
                    for (m in 2 until rows) {
                        var name = ""
                        var address = ""
                        var lat = ""
                        var lon = ""
                        for (i in 0 until cols) {
                            val content = sheet.getCell(i, m).contents
                            when (i) {
                                1 -> {
                                    name = content.getPlaceName()
                                    address = content.trim()
                                }
                                2 -> lon = content
                                3 -> lat = content
                            }
//                            content?.let { Log.d("ExcelTest", "第${i}行${m}列：$content") }
                        }
                        placeAllList.add(Place(name, Location(lon, lat), address))
                    }
                }

            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}