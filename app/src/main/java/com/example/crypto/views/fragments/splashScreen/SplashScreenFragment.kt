package com.example.crypto.views.fragments.splashScreen

import android.annotation.SuppressLint
import android.graphics.drawable.AnimatedVectorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.crypto.R
import com.example.crypto.databinding.FragmentMainScreenBinding
import com.example.crypto.databinding.FragmentSplashScreenBinding
import com.example.crypto.viewModels.SplashScreenViewModel
import com.example.crypto.views.fragments.mainScreen.MainScreenFragment
import kotlinx.coroutines.flow.collect
import org.koin.android.ext.android.inject


@SuppressLint("CustomSplashScreen")
class SplashScreenFragment : Fragment(R.layout.fragment_splash_screen) {

    private var _binding: FragmentSplashScreenBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SplashScreenViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSplashScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
        viewModel.setEvent(SplashScreenContract.Event.CachingInitialCoins)
    }

    private fun initObservers() {
        lifecycleScope.launchWhenStarted {
            viewModel.uiState.collect {
                when (it.cachingInitialCoinsState) {
                    is SplashScreenContract.CachingInitialCoinsState.Loading -> {
                        binding.splashScreenAnim.visibility = View.VISIBLE
                        val animation = binding.splashScreenAnim.drawable as AnimatedVectorDrawable
                        animation.start()
                    }
                    is SplashScreenContract.CachingInitialCoinsState.Success -> {
                        findNavController().navigate(R.id.action_splashScreenFragment_to_mainScreenFragment)
                    }
                    is SplashScreenContract.CachingInitialCoinsState.Error -> {
                        //TODO: error handling
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}