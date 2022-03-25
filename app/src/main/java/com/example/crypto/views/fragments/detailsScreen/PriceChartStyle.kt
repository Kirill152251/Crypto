package com.example.crypto.views.fragments.detailsScreen

import android.content.Context
import androidx.core.content.ContextCompat
import com.example.crypto.R
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.LineDataSet

class PriceChartStyle(private val context: Context) {
    fun styleChart(lineChart: LineChart) = lineChart.apply {
        axisRight.isEnabled = false
        axisLeft.isEnabled = false
        xAxis.isEnabled = false
        xAxis.setDrawGridLines(false)
        axisLeft.setDrawGridLines(false)
        setTouchEnabled(false)
        description = null
        legend.isEnabled = false
    }

    fun styleOfLine(lineDataSet: LineDataSet) = lineDataSet.apply {
        color = ContextCompat.getColor(context, R.color.orange)
        setDrawCircles(false)
        setDrawValues(false)
        mode = LineDataSet.Mode.CUBIC_BEZIER
    }
}