package com.example.crypto.views.fragments.settingsScreen

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.crypto.R
import com.example.crypto.databinding.FragmentSettingsScreenBinding
import com.example.crypto.model.settingsDB.SettingsUserInfo
import com.example.crypto.utils.ResourceForValidation
import com.example.crypto.viewModels.SettingsScreenViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_LONG
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import java.text.SimpleDateFormat
import java.util.*
import java.util.jar.Manifest

class SettingsScreenFragment : Fragment(R.layout.fragment_settings_screen) {

    private var _binding: FragmentSettingsScreenBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SettingsScreenViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Show bottom nav menu
        val bottomMenu = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_nav_menu)
        bottomMenu.isVisible = true
        bindUi()
        saveUserInfo()

        binding.dateOfBirth.setOnClickListener {
            val datePicker = MaterialDatePicker
                .Builder
                .datePicker()
                .setTitleText("SELECT DATE OF BIRTH")
                .build()
            datePicker.addOnPositiveButtonClickListener {
                val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val date = sdf.format(it)
                binding.dateOfBirth.setText(date)
            }
            datePicker.show(requireActivity().supportFragmentManager, "DATE")
        }
        val resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                onActivityResult(result)
            }

        binding.chooseAvatarButton.setOnClickListener {
            openGallery(resultLauncher)
        }
    }

    private fun openCamera() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE
            )
        }
    }

    private fun openGallery(resultLauncher: ActivityResultLauncher<Intent>) {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"

        resultLauncher.launch(intent)
    }

    private fun onActivityResult(result: ActivityResult) {
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            val imageUri = data?.data
            val bitmap = when {
                Build.VERSION.SDK_INT < 28 -> MediaStore.Images.Media.getBitmap(
                    requireActivity().contentResolver,
                    imageUri
                )
                else -> {
                    val source =
                        ImageDecoder.createSource(requireActivity().contentResolver, imageUri!!)
                    ImageDecoder.decodeBitmap(source)
                }
            }
            binding.profilePicture.setImageBitmap(bitmap)
        }
    }


    private fun bindUi() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                val userInfo = viewModel.getUserInfo() ?: SettingsUserInfo("", "", "")
                binding.apply {
                    firstName.setText(userInfo.firstName)
                    lastName.setText(userInfo.lastName)
                    dateOfBirth.setText(userInfo.dateOfBirth)
                }
            }
        }
    }

    private fun saveUserInfo() {
        binding.saveSettings.setOnClickListener {
            val firstName = binding.firstName.text.toString()
            val lastName = binding.lastName.text.toString()
            val dateOfBirth = binding.dateOfBirth.text.toString() ?: ""
            val resourceForValidation = isInputsValid(firstName, lastName)
            if (resourceForValidation.isValid) {
                val settingsUserInfo = SettingsUserInfo(firstName, lastName, dateOfBirth)
                viewModel.insertUserInfo(settingsUserInfo)
                Snackbar.make(it, resourceForValidation.errorOrSuccessInfo, LENGTH_LONG).show()
            } else {
                Snackbar.make(it, resourceForValidation.errorOrSuccessInfo, LENGTH_LONG).show()
            }
        }
    }

    private fun isInputsValid(firstName: String, lastName: String): ResourceForValidation {
        var resourceForValidation = ResourceForValidation(true, getString(R.string.success_input))
        if (firstName.isEmpty()) {
            resourceForValidation = ResourceForValidation(false, getString(R.string.no_first_name))
        }
        if (lastName.isEmpty()) {
            resourceForValidation = ResourceForValidation(false, getString(R.string.no_last_name))
        }
        if (lastName.isEmpty() && firstName.isEmpty()) {
            resourceForValidation =
                ResourceForValidation(false, getString(R.string.no_first_and_last_name))
        }
        if (firstName.length > 20) {
            resourceForValidation =
                ResourceForValidation(false, getString(R.string.first_name_too_long))
        }
        if (lastName.length > 20) {
            resourceForValidation =
                ResourceForValidation(false, getString(R.string.last_name_too_long))
        }
        if (lastName.length > 20 && firstName.length > 20) {
            resourceForValidation =
                ResourceForValidation(false, getString(R.string.first_last_name_are_too_long))
        }
        return resourceForValidation
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}