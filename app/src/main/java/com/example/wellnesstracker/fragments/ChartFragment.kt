package com.example.wellnesstracker.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.example.wellnesstracker.databinding.FragmentChartBinding
import com.example.wellnesstracker.utils.PrefsHelper
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import java.util.*

class ChartFragment : Fragment() {
    private var _binding: FragmentChartBinding? = null
    private val binding get() = _binding!!
    private lateinit var prefs: PrefsHelper

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChartBinding.inflate(inflater, container, false)
        prefs = PrefsHelper(requireContext())

        setupChart()
        drawMoodTrend()

        return binding.root
    }

    private fun setupChart() {
        val chart = binding.lineChart

        chart.setNoDataText("No mood data yet")
        chart.description.isEnabled = false
        chart.legend.isEnabled = false
        chart.setTouchEnabled(true)
        chart.setPinchZoom(true)
        chart.setDrawGridBackground(false)
        chart.setExtraOffsets(10f, 10f, 10f, 10f)

        // X Axis (Days)
        chart.xAxis.apply {
            position = XAxis.XAxisPosition.BOTTOM
            textColor = Color.DKGRAY
            textSize = 12f
            setDrawGridLines(false)
            axisLineColor = Color.parseColor("#CCCCCC")
            granularity = 1f
            valueFormatter = object : ValueFormatter() {
                private val days = listOf("6d", "5d", "4d", "3d", "2d", "1d", "Today")
                override fun getFormattedValue(value: Float): String {
                    val index = value.toInt().coerceIn(0, days.size - 1)
                    return days[index]
                }
            }
        }

        // Y Axis (Mood Emojis)
        chart.axisLeft.apply {
            textColor = Color.DKGRAY
            textSize = 12f
            axisMinimum = 0f
            axisMaximum = 5f
            granularity = 1f
            setDrawGridLines(true)
            gridColor = Color.parseColor("#DDDDDD")
            axisLineColor = Color.parseColor("#CCCCCC")
            valueFormatter = object : ValueFormatter() {
                private val moods = mapOf(
                    0f to "😡",
                    1f to "😢",
                    2f to "😔",
                    3f to "😐",
                    4f to "🙂",
                    5f to "😄"
                )
                override fun getFormattedValue(value: Float): String {
                    return moods[value] ?: ""
                }
            }
        }
        chart.axisRight.isEnabled = false
    }

    private fun drawMoodTrend() {
        val moods = prefs.loadMoods()
        val mapping = mapOf("😄" to 5f, "🙂" to 4f, "😐" to 3f, "😔" to 2f, "😢" to 1f, "😡" to 0f)

        val values = mutableListOf<Entry>()
        for (i in 6 downTo 0) {
            val cal = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -i) }

            val start = cal.apply {
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
            }.timeInMillis
            cal.apply {
                set(Calendar.HOUR_OF_DAY, 23)
                set(Calendar.MINUTE, 59)
                set(Calendar.SECOND, 59)
            }
            val end = cal.timeInMillis

            val dayMoods = moods.filter { it.timestamp in start..end }
            val avg = if (dayMoods.isEmpty()) 0f else dayMoods.map { mapping[it.emoji] ?: 3f }.average().toFloat()
            values.add(Entry((6 - i).toFloat(), avg))
        }

        val dataSet = LineDataSet(values, "Mood Trend").apply {
            color = Color.parseColor("#3362E9")     // Blue line (like hydration theme)
            lineWidth = 3f
            circleRadius = 6f
            setCircleColor(Color.parseColor("#76D6FF"))
            valueTextColor = Color.DKGRAY
            valueTextSize = 10f
            mode = LineDataSet.Mode.CUBIC_BEZIER
            setDrawFilled(true)
            fillColor = Color.parseColor("#B3E5FC")
            fillAlpha = 100
        }

        binding.lineChart.data = LineData(dataSet)
        binding.lineChart.animateY(1000)
        binding.lineChart.invalidate()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
