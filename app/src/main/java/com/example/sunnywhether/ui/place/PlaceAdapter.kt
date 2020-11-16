package com.example.sunnywhether.ui.place

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.sunnywhether.R
import com.example.sunnywhether.logic.Common
import com.example.sunnywhether.logic.model.Place
import com.example.sunnywhether.logic.model.Weather
import com.example.sunnywhether.ui.weather.WeatherActivity

class PlaceAdapter(private val fragment: Fragment, private val placeList: MutableList<Place>) :
    RecyclerView.Adapter<PlaceAdapter.ViewHolder>() {

    companion object {
        const val TAG = "PlaceAdapter"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.place_item, parent, false)
        val holder = ViewHolder(view)
        holder.itemView.setOnClickListener {
            val position = holder.adapterPosition
            val place = placeList[position]
            val intent = Intent(parent.context,WeatherActivity::class.java).apply {
                putExtra(Common.LOCATION_LNG,place.location.lng)
                putExtra(Common.LOCATION_LAT,place.location.lat)
                putExtra(Common.PLACE_NAME,place.name)
            }
            fragment.startActivity(intent)
        }
        return holder
    }

    override fun getItemCount(): Int = placeList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val place = placeList[position]
        holder.placeAddress.text = place.address
        holder.placeName.text = place.name
    }



    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val placeName = view.findViewById<TextView>(R.id.placeName)
        val placeAddress = view.findViewById<TextView>(R.id.placeAddress)
    }
}