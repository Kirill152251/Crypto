package com.example.crypto.views.fragments.splash_screen

import android.annotation.SuppressLint
import android.graphics.drawable.AnimatedVectorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.crypto.R
import com.example.crypto.databinding.FragmentSplashScreenBinding
import com.example.crypto.utils.isOnline
import com.example.crypto.view_models.SplashScreenViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject


@SuppressLint("CustomSplashScreen")
class SplashScreenFragment : Fragment(R.layout.fragment_splash_screen) {

    private var _binding: FragmentSplashScreenBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SplashScreenViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSplashScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bottomMenu = requireActivity().findViewById<BottomNavigationView>(R.id.menu_bottom_nav)
        bottomMenu.isVisible = false

        if (requireContext().isOnline()) {
            initObservers()
            viewModel.setEvent(SplashScreenContract.Event.LoadingInitialCoins)
        } else {
            binding.imageSplashScreenAnim.visibility = View.VISIBLE
            val animation = binding.imageSplashScreenAnim.drawable as AnimatedVectorDrawable
            animation.start()
            lifecycleScope.launchWhenCreated {
                delay(1000)
                findNavController()
                    .navigate(SplashScreenFragmentDirections.actionSplashScreenFragmentToMainScreenFragment())
            }
        }
    }

    private fun initObservers() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    when (it.cachingInitialCoinsState) {
                        is SplashScreenContract.CachingInitialCoinsState.Loading -> {
                            binding.imageSplashScreenAnim.visibility = View.VISIBLE
                            val animation =
                                binding.imageSplashScreenAnim.drawable as AnimatedVectorDrawable
                            animation.start()
                        }
                        is SplashScreenContract.CachingInitialCoinsState.Success -> {
                            findNavController()
                                .navigate(SplashScreenFragmentDirections.actionSplashScreenFragmentToMainScreenFragment())
                        }
                        SplashScreenContract.CachingInitialCoinsState.Error -> {
                            binding.apply {
                                textNetworkProblem.isVisible = true
                                imageServerError.isVisible = true
                            }
                        }
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