package com.example.sunnywhether.ui.weather

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.sunnywhether.R
import com.example.sunnywhether.logic.Common
import com.example.sunnywhether.logic.model.Weather
import com.example.sunnywhether.logic.model.getSky
import com.example.sunnywhether.toast
import kotlinx.android.synthetic.main.activity_weather.*
import kotlinx.android.synthetic.main.forecast.*
import kotlinx.android.synthetic.main.life_index.*
import kotlinx.android.synthetic.main.now.*
import java.text.SimpleDateFormat



class WeatherActivity : AppCompatActivity() {

    companion object{
        const val TAG = "WeatherActivity"
    }

    private val viewModel by lazy { ViewModelProvider(this)[WeatherViewModel::class.java] }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //将背景图与状态栏融合
//        val decorView = window.decorView
//        decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//        window.statusBarColor = Color.TRANSPARENT
//
//        decorView.dispatchWindowVisibilityChanged(decorView.windowInsetsController)
//
//        decorView.windowInsetsController.hide()
        setContentView(R.layout.activity_weather)

        requestReadCDPermission()

        getSPLocation()

        registerLiveData()

        viewModel.refreshWeather(viewModel.locationLng,viewModel.locationLat)
    }

    private fun registerLiveData() {
        viewModel.weatherLiveData.observe(this, Observer {
            val weather = it.getOrNull()
            if (weather!=null){
                showWeatherInfo(weather)
            }else{
                toast(this, "无法获取天气信息")
                it.exceptionOrNull()?.printStackTrace()
            }
        })
    }

    private fun showWeatherInfo(weather:Weather) {
        placeName.text = viewModel.placeName
        val realtime = weather.realtime
        val daily = weather.daily
        // 填充now.xml布局中数据
        val currentTempText = "${realtime.temperature.toInt()} ℃"
        currentTemp.text = currentTempText
        currentSky.text = getSky(realtime.skycon).info
        val currentPM25Text = "空气指数 ${realtime.air_quality.aqi.chn}"
        currentAQI.text = currentPM25Text
        nowLayout.setBackgroundResource(getSky(realtime.skycon).bg)
        // 填充forecast.xml布局中的数据
        forecastLayout.removeAllViews()
        val days = daily.skycon.size
        for (i in 0 until days) {
            val skycon = daily.skycon[i]
            val temperature = daily.temperature[i]
            val view = LayoutInflater.from(this).inflate(R.layout.forecast_item, forecastLayout, false)
            val dateInfo = view.findViewById(R.id.dateInfo) as TextView
            val skyIcon = view.findViewById(R.id.skyIcon) as ImageView
            val skyInfo = view.findViewById(R.id.skyInfo) as TextView
            val temperatureInfo = view.findViewById(R.id.temperatureInfo) as TextView
//            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.UK)
//            dateInfo.text = simpleDateFormat.format(skycon.date).toString()
            dateInfo.text = skycon.date
            val sky = getSky(skycon.value)
            Log.d("Repository","sky:(${sky.icon},${sky.info},${sky.bg})")
            skyIcon.setImageResource(sky.icon)
            skyInfo.text = sky.info
            val tempText = "${temperature.min.toInt()} ~ ${temperature.max.toInt()} ℃"
            temperatureInfo.text = tempText
            forecastLayout.addView(view)
        }
        // 填充life_index.xml布局中的数据
        val lifeIndex = daily.life_index
        coldRiskText.text = lifeIndex.coldRisk[0].desc
        dressingText.text = lifeIndex.dressing[0].desc
        ultravioletText.text = lifeIndex.ultraviolet[0].desc
        carWashingText.text = lifeIndex.carWashing[0].desc
        weatherLayout.visibility = View.VISIBLE
    }

    private fun getSPLocation() {
        if (viewModel.locationLng.isEmpty()){
            viewModel.locationLng = intent.getStringExtra(Common.LOCATION_LNG) ?: ""
        }
        if (viewModel.locationLat.isEmpty()){
            viewModel.locationLat = intent.getStringExtra(Common.LOCATION_LAT) ?: ""
        }
        if (viewModel.placeName.isEmpty()){
            viewModel.placeName = intent.getStringExtra(Common.PLACE_NAME) ?: ""
        }
        Log.d(TAG, viewModel.locationLng)
    }

    private fun requestReadCDPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                1
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1 -> {
                if (grantResults.isNotEmpty() &&
                    grantResults[0] == PackageManager.PERMISSION_DENIED
                ) {
                    finish()
                }
            }
        }
    }

    /**
     * java jdk>1.8才支持解析2020-11-16T00:00+08:00制时间 故不作处理
     */
    fun String.timeChange(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val sd = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX")
        val date = sd.parse(this)
        return sdf.format(date)
    }
}