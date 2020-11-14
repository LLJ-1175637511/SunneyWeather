package com.example.sunnywhether

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import jxl.Workbook
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.lang.Exception
import kotlin.concurrent.thread

class ExcelTest : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bt.setOnClickListener {
            readExcel()
        }
    }

    private fun readExcel() {
        try {
//            thread {
                val open = assets.open("weather.xls")
                val book = Workbook.getWorkbook(open)
                val allSheets = book.sheets
                allSheets.forEach { sheet ->
                    val rows = sheet.rows
                    val cols = sheet.columns
                    for (i in 0..cols) {
                        for (m in 0..rows) {
                            if (m >= 5) return@forEach
                            val content = sheet.getCell(i, m).contents
                            content?.let { Log.d("ExcelTest", "第${i}行${m}列：$content") }
                        }
                    }
                }
//            }.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}