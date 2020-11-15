package com.example.sunnywhether.ui.place

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sunnywhether.R
import com.example.sunnywhether.toast
import kotlinx.android.synthetic.main.fragment_place.*

class PlaceFragment : Fragment() {

    private val viewModel by lazy { ViewModelProvider(this)[PlaceViewModel::class.java] }

    private val adapter by lazy {  PlaceAdapter(this, viewModel.placeList) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_place, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val layoutManager = LinearLayoutManager(activity)

        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter

        searchPlaceEdit.addTextChangedListener {
            val content = it.toString()
            if (content.isNotEmpty()) {
                viewModel.searchPlaces(content)
            } else {
                recyclerView.visibility = View.GONE
                bgImageView.visibility = View.VISIBLE
                viewModel.placeList.clear()
                adapter.notifyDataSetChanged()
            }
        }

        viewModel.placeLiveData.observe(viewLifecycleOwner, Observer { result ->
//            val place = result.getOrNull()
//            if (place!=null){
//                recyclerView.visibility = View.VISIBLE
//                bgImageView.visibility = View.GONE
//                viewModel.placeList.clear()
//                viewModel.placeList.addAll(place)
//            }else{
//                toast(activity as Context,"未能查询到地点")
//                result.exceptionOrNull()?.printStackTrace()
//            }
            if (result.isNotEmpty()) {
                recyclerView.visibility = View.VISIBLE
                bgImageView.visibility = View.GONE
                viewModel.placeList.clear()
                viewModel.placeList.addAll(result)
                adapter.notifyDataSetChanged()
            }else {
                toast(activity as Context, "未能查询到地点")
            }
        })

        Log.d("ExcelTest", "activity:${activity==null} assets:${activity?.assets==null}")
        activity?.assets?.let { viewModel.getPlaceExcel(it)
            Log.d("ExcelTest", "getPlaceExcel")
        }
    }


}