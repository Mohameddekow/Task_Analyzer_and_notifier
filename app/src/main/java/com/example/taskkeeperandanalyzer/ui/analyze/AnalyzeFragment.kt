package com.example.taskkeeperandanalyzer.ui.analyze

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.taskkeeperandanalyzer.constants.TASK_ROOT_REF
import com.example.taskkeeperandanalyzer.databinding.FragmentAnalyzeBinding
import com.example.taskkeeperandanalyzer.model.TaskModel
import com.example.taskkeeperandanalyzer.ui.home.HomeViewModel
import com.example.taskkeeperandanalyzer.utils.Resource
import com.example.taskkeeperandanalyzer.utils.showLongToast
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class AnalyzeFragment : Fragment() {
    private var _binding: FragmentAnalyzeBinding? = null
    private val binding get() = _binding!!


    private val homeViewModel by viewModels<HomeViewModel>()

    private  var note: Float = 0.0F
    private  var gym:  Float = 0.0F
    private  var interview:  Float = 0.0F
    private  var androidSession:  Float = 0.0F
    private  var developerEvent:  Float = 0.0F

    @Inject
    lateinit var auth: FirebaseAuth

    private lateinit var  pieChart : PieChart
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAnalyzeBinding.inflate(inflater, container, false)

        pieChart = binding.myPieChart


        setupPieChart()
        loadPieChartData()

        binding.apply {
            val userId = auth.currentUser?.uid
            if (userId != null) {
                homeViewModel.getAllTasks( userId ,TASK_ROOT_REF)
            }
        }



        return binding.root
    }


    private fun setupPieChart() {
        pieChart.isDrawHoleEnabled = true
        pieChart.setUsePercentValues(true)
        pieChart.setEntryLabelTextSize(12f)
        pieChart.setEntryLabelColor(Color.BLACK)
        pieChart.centerText = "Task type analysis"
        pieChart.setCenterTextSize(24f)
        pieChart.description.isEnabled = false
        val l = pieChart.legend
        l.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
        l.orientation = Legend.LegendOrientation.VERTICAL
        l.setDrawInside(false)
        l.isEnabled = true
    }

    private fun loadPieChartData() {

        homeViewModel.fetchingTasksState.observe(viewLifecycleOwner) { result ->

            binding.progressBar.isVisible = result is Resource.Loading

            when(result) {
                is Resource.Success -> {

                    for (i in result.data!!){
                        when (i.taskType) {
                            "Gym remainder" -> {
                                gym += 1.0F
                            }
                            "Interview remainder" -> {
                                interview += 1.0F
                            }
                            "Note taking" -> {
                                note += 1.0F
                            }
                            "Developer events" -> {
                                developerEvent += 1.0F
                            }
                            "Android session" -> {
                                androidSession += 1.0F
                            }
                        }
                    }


                }

                is Resource.Error -> {
                    showLongToast(requireContext(), result.error?.message.toString())
                }
                else ->{}
            }


            val entries: ArrayList<PieEntry> = ArrayList()

            entries.add(PieEntry(gym, "Gym remainder"))
            entries.add(PieEntry(interview, "Interview remainder"))
            entries.add(PieEntry(note, "Note taking"))
            entries.add(PieEntry(developerEvent, "Developer events"))
            entries.add(PieEntry(androidSession, "Android session"))

            val colors: ArrayList<Int> = ArrayList()
            for (color in ColorTemplate.MATERIAL_COLORS) {
                colors.add(color)
            }
            for (color in ColorTemplate.VORDIPLOM_COLORS) {
                colors.add(color)
            }
            val dataSet = PieDataSet(entries, "Task Type")
            dataSet.colors = colors
            val data = PieData(dataSet)
            data.setDrawValues(true)
            data.setValueFormatter(PercentFormatter(pieChart))
            data.setValueTextSize(12f)
            data.setValueTextColor(Color.BLACK)
            pieChart.data = data
            pieChart.invalidate()
            pieChart.animateY(1400, Easing.EaseInOutQuad)
        }

    }


    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}