package com.example.crypto.views.fragments.mainScreen

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.crypto.R
import com.example.crypto.databinding.FragmentMainScreenBinding
import com.example.crypto.viewModels.MainScreenViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import org.koin.android.ext.android.inject

class MainScreenFragment : Fragment(R.layout.fragment_main_screen) {

    private var _binding: FragmentMainScreenBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainScreenViewModel by viewModel()
    private val adapter: CoinsListAdapter by inject()

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
        binding.rvCoins.adapter = adapter

        //by default list sorted by market capitalization
        viewModel.setEvent(MainScreenContract.Event.ChoseSortingByMarketCap)
        updateUi()

        binding.sortButton.setOnClickListener {
            showSortDialog()
        }

        pullToRefresh()
    }

    private fun pullToRefresh() {
        binding.swipeToRefreshLayout.setOnRefreshListener {
            when(viewModel.currentState.recycleViewState) {
                is MainScreenContract.RecycleViewState.SortingByMarketCap -> {
                    viewModel.setEvent(MainScreenContract.Event.ChoseSortingByMarketCap)
                    updateUi()
                    binding.swipeToRefreshLayout.isRefreshing = false
                }
                is MainScreenContract.RecycleViewState.SortingByPrice -> {
                    viewModel.setEvent(MainScreenContract.Event.ChoseSortingByPrice)
                    updateUi()
                    binding.swipeToRefreshLayout.isRefreshing = false
                }
                is MainScreenContract.RecycleViewState.SortingByVolatility -> {
                    viewModel.setEvent(MainScreenContract.Event.ChoseSortingByVolatility)
                    updateUi()
                    binding.swipeToRefreshLayout.isRefreshing = false
                }
                is MainScreenContract.RecycleViewState.Loading -> {
                    binding.swipeToRefreshLayout.isRefreshing = false
                }
            }
        }
    }

    private fun updateUi() {
        lifecycleScope.launchWhenStarted {
            viewModel.uiState.collect {
                when (it.recycleViewState) {
                    is MainScreenContract.RecycleViewState.Loading -> {
                        binding.apply {
                            mainScreenProgressbar.isVisible = true
                            rvCoins.isVisible = false
                        }
                    }
                    is MainScreenContract.RecycleViewState.SortingByMarketCap -> {
                        it.recycleViewState.coins.distinctUntilChanged().collectLatest { coins ->
                            adapter.submitData(coins)
                        }
                    }
                    is MainScreenContract.RecycleViewState.SortingByPrice -> {
                        it.recycleViewState.coins.distinctUntilChanged().collectLatest { coins ->
                            adapter.submitData(coins)
                        }
                    }
                    is MainScreenContract.RecycleViewState.SortingByVolatility -> {
                        it.recycleViewState.coins.distinctUntilChanged().collectLatest { coins ->
                            adapter.submitData(coins)
                        }
                    }
                }
            }
        }
    }

    private fun showSortDialog() {
        val options = arrayOf(
            getString(R.string.dialog_sorted_by_cap),
            getString(R.string.dialog_sorted_by_price),
            getString(R.string.dialog_sorted_by_vol)
        )
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.sort_dialog_title))
            .setSingleChoiceItems(options, 0, null)
            .setPositiveButton(getString(R.string.positive_button)) { dialog, _ ->
                when ((dialog as AlertDialog).listView.checkedItemPosition) {
                    0 -> {
                        viewModel.setEvent(MainScreenContract.Event.ChoseSortingByMarketCap)
                        updateUi()
                    }
                    1 -> {
                        viewModel.setEvent(MainScreenContract.Event.ChoseSortingByPrice)
                        updateUi()
                    }
                    2 -> {
                        viewModel.setEvent(MainScreenContract.Event.ChoseSortingByVolatility)
                        updateUi()
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