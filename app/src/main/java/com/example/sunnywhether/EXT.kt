package com.example.sunnywhether

import android.content.Context
import android.widget.Toast

fun toast(context: Context, msg: String) {
    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
}

fun longToast(context: Context, msg: String) {
    Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
}

fun String.getPlaceName(): String {
    if (this == "") return "xxx"
    if (this.contains("自治区")) {
        return "${this.split("自治区")[0]}自治区"
    }else if (this.contains("特别行政区")){
        return "${this.split("特别行政区")[0]}特别行政区"
    } else {
        if (this.contains("省")){
            return "${this.split("省")[0]}省"
        }else if (this.contains("市")){
            return "${this.split("市")[0]}市"
        }else{
            return ""
        }
    }
}

