package com.example.crypto.views.fragments.details_screen

import android.os.Bundle
import android.transition.TransitionInflater
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import coil.load
import com.example.crypto.R
import com.example.crypto.databinding.FragmentDetailsScreenBinding
import com.example.crypto.utils.ApiResourceForPriceCharts
import com.example.crypto.utils.priceConverter
import com.example.crypto.view_models.DetailsScreenViewModel
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class DetailsScreenFragment : Fragment(R.layout.fragment_details_screen) {

    private var _binding: FragmentDetailsScreenBinding? = null
    private val binding get() = _binding!!
    private val args: DetailsScreenFragmentArgs by navArgs()
    private val viewModel: DetailsScreenViewModel by viewModel {
        parametersOf(args.coinId)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setSharedElementTransitionAnimation()
        val chartStyle = PriceChartStyle(requireContext())
        bindUi(chartStyle)
        updateChart(chartStyle)
        viewModel.setEvent(DetailsScreenContract.Event.ChoseOneDayInterval)
        binding.imageBackToMainScreen.setOnClickListener {
            requireActivity().onBackPressed()
        }

        val bottomMenu = requireActivity().findViewById<BottomNavigationView>(R.id.menu_bottom_nav)
        bottomMenu.isVisible = false
    }

    private fun updateChart(chartStyle: PriceChartStyle) = lifecycleScope.launch {
        viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.uiState.collect {
                when (it.chartState) {
                    is DetailsScreenContract.ChartState.Loading -> {
                        binding.apply {
                            priceCharts.visibility = View.INVISIBLE
                            textMaxPrice.visibility = View.INVISIBLE
                            textMinPrice.visibility = View.INVISIBLE
                            progressBar.visibility = View.VISIBLE
                            textNoData.visibility = View.INVISIBLE
                        }
                    }
                    is DetailsScreenContract.ChartState.PerDay -> {
                        bindChart(
                            it.chartState.priceData,
                            chartStyle
                        )
                    }
                    is DetailsScreenContract.ChartState.PerWeek -> {
                        bindChart(
                            it.chartState.priceData,
                            chartStyle
                        )
                    }
                    is DetailsScreenContract.ChartState.PerMonth -> {
                        bindChart(
                            it.chartState.priceData,
                            chartStyle
                        )
                    }
                    is DetailsScreenContract.ChartState.PerYear -> {
                        bindChart(
                            it.chartState.priceData,
                            chartStyle
                        )
                    }
                    is DetailsScreenContract.ChartState.AllTime -> {
                        bindChart(
                            it.chartState.priceData,
                            chartStyle
                        )
                    }
                }
            }
        }
    }

    private fun bindChart(
        dataResource: ApiResourceForPriceCharts<List<Entry>>,
        style: PriceChartStyle
    ) {
        when (dataResource) {
            is ApiResourceForPriceCharts.Success -> {
                val data = dataResource.data
                val min = dataResource.minPrice
                val max = dataResource.maxPrice
                if (data.size <= 1) {
                    binding.apply {
                        textNoData.visibility = View.VISIBLE
                        priceCharts.visibility = View.INVISIBLE
                        textMaxPrice.visibility = View.INVISIBLE
                        textMinPrice.visibility = View.INVISIBLE
                        progressBar.visibility = View.INVISIBLE
                        textMaxPrice.visibility = View.INVISIBLE
                        textMinPrice.visibility = View.INVISIBLE
                    }
                } else {
                    val lineDataSet = LineDataSet(data, "DATA")
                    style.styleChart(binding.priceCharts)
                    style.styleOfLine(lineDataSet)
                    binding.apply {
                        priceCharts.data = LineData(lineDataSet)
                        priceCharts.invalidate()
                        priceCharts.visibility = View.VISIBLE
                        textMaxPrice.visibility = View.VISIBLE
                        textMinPrice.visibility = View.VISIBLE
                        progressBar.visibility = View.INVISIBLE
                        textNoData.visibility = View.INVISIBLE
                        textMaxPrice.visibility = View.VISIBLE
                        textMaxPrice.text = getString(R.string.dollar_at_the_end, max)
                        textMinPrice.visibility = View.VISIBLE
                        textMinPrice.text = getString(R.string.dollar_at_the_end, min)
                    }
                }
            }
            is ApiResourceForPriceCharts.Error -> {
                binding.apply {
                    textNoData.visibility = View.VISIBLE
                    textNoData.text = ApiResourceForPriceCharts.DEFAULT_ERROR_MESSAGE
                    priceCharts.visibility = View.INVISIBLE
                    progressBar.visibility = View.INVISIBLE
                    textMaxPrice.visibility = View.INVISIBLE
                    textMinPrice.visibility = View.INVISIBLE
                }
            }
        }
    }

    private fun bindUi(chartStyle: PriceChartStyle) {
        binding.apply {
            textCoinNameToolbar.text = args.coinName
            textCurrentPrice.text = getString(
                R.string.dollar_at_the_beginning,
                args.coinPrice.toDouble().priceConverter()
            )
            textPriceChange.text =
                getString(R.string.percent_at_the_end, args.coinPriceChange.toString())
            imageCoinSymbol.load(args.coinIconUrl)
            textMarketCapValue.text = getString(
                R.string.dollar_at_the_beginning,
                args.marketCap.toDouble().priceConverter()
            )

            radioButtonOneDay.setOnClickListener {
                viewModel.setEvent(DetailsScreenContract.Event.ChoseOneDayInterval)
                updateChart(chartStyle)
            }
            radioButtonOneWeek.setOnClickListener {
                viewModel.setEvent(DetailsScreenContract.Event.ChoseOneWeekInterval)
                updateChart(chartStyle)
            }
            radioButtonOneMonth.setOnClickListener {
                viewModel.setEvent(DetailsScreenContract.Event.ChoseOneMonthInterval)
                updateChart(chartStyle)
            }
            radioButtonOneYear.setOnClickListener {
                viewModel.setEvent(DetailsScreenContract.Event.ChoseOneYearInterval)
                updateChart(chartStyle)
            }
            radioButtonAllTime.setOnClickListener {
                viewModel.setEvent(DetailsScreenContract.Event.ChoseAllTimeInterval)
                updateChart(chartStyle)
            }
        }
    }

    private fun setSharedElementTransitionAnimation() {
        val animation = TransitionInflater.from(requireContext()).inflateTransition(
            android.R.transition.slide_right
        )
        sharedElementEnterTransition = animation
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}