package com.example.crypto.views.fragments.detailsScreen

import android.annotation.SuppressLint
import android.os.Bundle
import android.transition.Transition
import android.transition.TransitionInflater
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.crypto.R
import com.example.crypto.databinding.FragmentDetailsScreenBinding
import com.example.crypto.model.api.CoinGeckoService
import com.example.crypto.model.constans.*
import com.example.crypto.utils.Resource
import com.example.crypto.utils.converter
import com.example.crypto.viewModels.DetailsScreenViewModel
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import kotlin.math.ln
import kotlin.math.pow

class DetailsScreenFragment : Fragment(R.layout.fragment_details_screen) {

    private var _binding: FragmentDetailsScreenBinding? = null
    private val binding get() = _binding!!
    private val args: DetailsScreenFragmentArgs by navArgs()
    private val style: PriceChartStyle by inject()
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
        bindUi()
        updateChart()
        viewModel.setEvent(DetailsScreenContract.Event.ChoseOneDayInterval)
        binding.backToMainScreenButton.setOnClickListener {
            requireActivity().onBackPressed()
        }

        //Hide bottom nav menu
        val bottomMenu = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_nav_menu)
        bottomMenu.isVisible = false
    }

    private fun updateChart() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    when (it.chartState) {
                        is DetailsScreenContract.ChartState.Loading -> {
                            binding.apply {
                                priceCharts.visibility = View.INVISIBLE
                                maxPrice.visibility = View.INVISIBLE
                                minPrice.visibility = View.INVISIBLE
                                progressBar.visibility = View.VISIBLE
                                noDataMessage.visibility = View.INVISIBLE
                            }
                        }
                        is DetailsScreenContract.ChartState.PerDay -> {
                            bindChart(
                                it.chartState.priceData,
                                viewModel.getMinAndMaxPriceForDetailsScreen(args.coinId, LABEL_DAY)
                            )
                        }
                        is DetailsScreenContract.ChartState.PerWeek -> {
                            bindChart(
                                it.chartState.priceData,
                                viewModel.getMinAndMaxPriceForDetailsScreen(args.coinId, LABEL_WEEK)
                            )
                        }
                        is DetailsScreenContract.ChartState.PerMonth -> {
                            bindChart(
                                it.chartState.priceData,
                                viewModel.getMinAndMaxPriceForDetailsScreen(
                                    args.coinId,
                                    LABEL_MONTH
                                )
                            )
                        }
                        is DetailsScreenContract.ChartState.PerYear -> {
                            bindChart(
                                it.chartState.priceData,
                                viewModel.getMinAndMaxPriceForDetailsScreen(args.coinId, LABEL_YEAR)
                            )
                        }
                        is DetailsScreenContract.ChartState.AllTime -> {
                            bindChart(
                                it.chartState.priceData,
                                viewModel.getMinAndMaxPriceForDetailsScreen(
                                    args.coinId,
                                    LABEL_ALL_TIME
                                )
                            )
                        }
                    }
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun bindChart(
        dataResource: Resource<List<Entry>>,
        minAndMaxPriceResource: Resource<List<String>>
    ) {
        if (dataResource.status == Resource.Status.ERROR || minAndMaxPriceResource.status == Resource.Status.ERROR) {
            binding.apply {
                noDataMessage.visibility = View.VISIBLE
                noDataMessage.text = dataResource.message
                priceCharts.visibility = View.INVISIBLE
                maxPrice.visibility = View.INVISIBLE
                minPrice.visibility = View.INVISIBLE
                progressBar.visibility = View.INVISIBLE
            }
        } else {
            val data = dataResource.data!!
            val minAndMaxPrice = minAndMaxPriceResource.data!!
            if (data.size <= 1) {
                binding.apply {
                    noDataMessage.visibility = View.VISIBLE
                    priceCharts.visibility = View.INVISIBLE
                    maxPrice.visibility = View.INVISIBLE
                    minPrice.visibility = View.INVISIBLE
                    progressBar.visibility = View.INVISIBLE
                }
            } else {
                val lineDataSet = LineDataSet(data, "DATA")
                style.styleChart(binding.priceCharts)
                style.styleOfLine(lineDataSet)
                binding.apply {
                    priceCharts.data = LineData(lineDataSet)
                    priceCharts.invalidate()
                    priceCharts.visibility = View.VISIBLE
                    maxPrice.text = minAndMaxPrice.last() + " $"
                    minPrice.text = minAndMaxPrice.first() + " $"
                    maxPrice.visibility = View.VISIBLE
                    minPrice.visibility = View.VISIBLE
                    progressBar.visibility = View.INVISIBLE
                    noDataMessage.visibility = View.INVISIBLE
                }
            }
        }
    }


    @SuppressLint("SetTextI18n")
    private fun bindUi() {
        binding.apply {
            coinNameDetailsScreen.text = args.coinName
            currentPrice.text = formatPrice(args.coinPrice)
            priceChange.text = args.coinPriceChange.toString() + " %"
            Glide.with(requireContext()).load(args.coinIconUrl).into(coinSymbolDetailsScreen)
            marketCapValue.text = converter(args.marketCap.toDouble())

            oneDayInterval.setOnClickListener {
                viewModel.setEvent(DetailsScreenContract.Event.ChoseOneDayInterval)
                updateChart()
            }
            oneWeekInterval.setOnClickListener {
                viewModel.setEvent(DetailsScreenContract.Event.ChoseOneWeekInterval)
                updateChart()
            }
            oneMonthInterval.setOnClickListener {
                viewModel.setEvent(DetailsScreenContract.Event.ChoseOneMonthInterval)
                updateChart()
            }
            oneYearInterval.setOnClickListener {
                viewModel.setEvent(DetailsScreenContract.Event.ChoseOneYearInterval)
                updateChart()
            }
            allTimeInterval.setOnClickListener {
                viewModel.setEvent(DetailsScreenContract.Event.ChoseAllTimeInterval)
                updateChart()
            }
        }
    }

    private fun formatPrice(price: String): String {
        return if (price.toDouble() >= 100_000_000) {
            converter(price.toDouble())
        } else {
            "$price $"
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