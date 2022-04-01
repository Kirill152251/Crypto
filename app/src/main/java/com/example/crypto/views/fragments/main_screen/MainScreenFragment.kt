package com.example.crypto.views.fragments.main_screen

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.crypto.R
import com.example.crypto.databinding.CoinsListItemBinding
import com.example.crypto.databinding.FragmentMainScreenBinding
import com.example.crypto.model.api.responses.coins_list.Coin
import com.example.crypto.model.constans.QUERY_SORT_BY_MARKET_CAP
import com.example.crypto.model.constans.QUERY_SORT_BY_PRICE
import com.example.crypto.model.constans.QUERY_SORT_BY_VOLATILITY
import com.example.crypto.utils.isOnline
import com.example.crypto.view_models.MainScreenViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.example.crypto.views.fragments.main_screen.MainScreenContract.*


class MainScreenFragment : Fragment(R.layout.fragment_main_screen) {

    private var _binding: FragmentMainScreenBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainScreenViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val decoration = DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        binding.rvCoins.addItemDecoration(decoration)
        val adapter = CoinsListAdapter(requireContext()) { coinItem, binding ->
            itemClickListener(coinItem, binding)
        }
        binding.rvCoins.adapter = adapter

        if (requireContext().isOnline()) {
            lifecycleScope.launch {
                when (viewModel.getSortingTypeFromDataStore()) {
                    QUERY_SORT_BY_MARKET_CAP -> {
                        viewModel.setEvent(Event.ChoseSortingByMarketCap)
                        updateUi(adapter)
                    }
                    QUERY_SORT_BY_PRICE -> {
                        viewModel.setEvent(Event.ChoseSortingByPrice)
                        updateUi(adapter)
                    }
                    QUERY_SORT_BY_VOLATILITY -> {
                        viewModel.setEvent(Event.ChoseSortingByVolatility)
                        updateUi(adapter)
                    }
                }
            }
            binding.imageSort.setOnClickListener {
                lifecycleScope.launch {
                    showSortDialog(viewModel.getSortingTypeFromDataStore(), adapter)
                }
            }
            pullToRefresh(adapter)
        } else {
            viewModel.setEvent(Event.FetchFromDb)
            updateUi(adapter)
            binding.imageSort.visibility = View.GONE
        }

        val bottomMenu = requireActivity().findViewById<BottomNavigationView>(R.id.menu_bottom_nav)
        bottomMenu.isVisible = true
    }

    private fun itemClickListener(coin: Coin, binding: CoinsListItemBinding) {
        val extras = FragmentNavigatorExtras(binding.textCoinPrice to "price_details_screen")
        findNavController().navigate(
            MainScreenFragmentDirections.actionMainScreenFragmentToDetailsScreenFragment(
                coinId = coin.coinId,
                coinPrice = coin.currentPrice,
                coinIconUrl = coin.image,
                coinName = coin.name,
                coinPriceChange = coin.volatility.toFloat(),
                marketCap = coin.marketCapValue
            ),
            navigatorExtras = extras
        )
    }

    private fun pullToRefresh(adapter: CoinsListAdapter) {
        binding.swipeToRefreshLayout.setOnRefreshListener {
            when (viewModel.currentState.recycleViewState) {
                is RecycleViewState.SortingByMarketCap -> {
                    viewModel.setEvent(Event.ChoseSortingByMarketCap)
                    updateUi(adapter)
                    binding.swipeToRefreshLayout.isRefreshing = false
                }
                is RecycleViewState.SortingByPrice -> {
                    viewModel.setEvent(Event.ChoseSortingByPrice)
                    updateUi(adapter)
                    binding.swipeToRefreshLayout.isRefreshing = false
                }
                is RecycleViewState.SortingByVolatility -> {
                    viewModel.setEvent(Event.ChoseSortingByVolatility)
                    updateUi(adapter)
                    binding.swipeToRefreshLayout.isRefreshing = false
                }
                is RecycleViewState.Loading -> {
                    binding.swipeToRefreshLayout.isRefreshing = false
                }
                is RecycleViewState.ItemsFromDb -> {}
            }
        }
    }

    private fun updateUi(adapter: CoinsListAdapter) {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    when (it.recycleViewState) {
                        is RecycleViewState.Loading -> {
                            binding.apply {
                                progressBarMainScreen.isVisible = true
                                rvCoins.isVisible = false
                            }
                        }
                        is RecycleViewState.SortingByMarketCap -> {
                            viewModel.setEvent(
                                Event.SaveSortingType(QUERY_SORT_BY_MARKET_CAP)
                            )
                            it.recycleViewState.coins.distinctUntilChanged()
                                .collectLatest { coins ->
                                    adapter.submitData(coins)
                                }
                        }
                        is RecycleViewState.SortingByPrice -> {
                            viewModel.setEvent(Event.SaveSortingType(QUERY_SORT_BY_PRICE))
                            it.recycleViewState.coins.distinctUntilChanged()
                                .collectLatest { coins ->
                                    adapter.submitData(coins)
                                }
                        }
                        is RecycleViewState.SortingByVolatility -> {
                            viewModel.setEvent(Event.SaveSortingType(QUERY_SORT_BY_VOLATILITY))
                            it.recycleViewState.coins.distinctUntilChanged()
                                .collectLatest { coins ->
                                    adapter.submitData(coins)
                                }
                        }
                        is RecycleViewState.ItemsFromDb -> {
                            it.recycleViewState.coins.distinctUntilChanged()
                                .collectLatest { coins ->
                                    adapter.submitData(coins)
                                }
                        }
                    }
                }
            }
        }
    }

    private fun showSortDialog(order: String, adapter: CoinsListAdapter) {
        val options = arrayOf(
            getString(R.string.dialog_sorted_by_cap),
            getString(R.string.dialog_sorted_by_price),
            getString(R.string.dialog_sorted_by_vol)
        )
        var checkedItem = 0
        when (order) {
            QUERY_SORT_BY_MARKET_CAP -> checkedItem = 0
            QUERY_SORT_BY_PRICE -> checkedItem = 1
            QUERY_SORT_BY_VOLATILITY -> checkedItem = 2
        }
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.sort_dialog_title))
            .setSingleChoiceItems(options, checkedItem, null)
            .setPositiveButton(getString(R.string.positive_button)) { dialog, _ ->
                when ((dialog as AlertDialog).listView.checkedItemPosition) {
                    0 -> {
                        viewModel.setEvent(Event.ChoseSortingByMarketCap)
                        updateUi(adapter)
                        binding.rvCoins.layoutManager?.scrollToPosition(0)
                    }
                    1 -> {
                        viewModel.setEvent(Event.ChoseSortingByPrice)
                        updateUi(adapter)
                        binding.rvCoins.layoutManager?.scrollToPosition(0)
                    }
                    2 -> {
                        viewModel.setEvent(Event.ChoseSortingByVolatility)
                        updateUi(adapter)
                        binding.rvCoins.layoutManager?.scrollToPosition(0)
                    }
                }
            }
            .setNegativeButton(getString(R.string.negative_button)) { _, _ -> }
            .create()
        dialog.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}