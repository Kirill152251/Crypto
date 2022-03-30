package com.example.crypto.views.fragments.settings_screen

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.crypto.R
import com.example.crypto.databinding.FragmentSettingsScreenBinding
import com.example.crypto.model.constans.*
import com.example.crypto.model.settings_db.SettingsUserInfo
import com.example.crypto.utils.ResourceForValidation
import com.example.crypto.utils.getBitmapFromView
import com.example.crypto.view_models.SettingsScreenViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_LONG
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import java.text.SimpleDateFormat
import java.util.*

class SettingsScreenFragment : Fragment(R.layout.fragment_settings_screen) {

    private var _binding: FragmentSettingsScreenBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SettingsScreenViewModel by inject()
    private val resultLauncherCamera =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            onActivityResult(result, CAMERA_REQUEST)
        }
    private val resultLauncherGallery =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            onActivityResult(result, GALLERY_REQUEST)
        }
    private val permissionResultLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                resultLauncherCamera.launch(intent)
            } else {
                Snackbar.make(
                    requireView(),
                    resources.getString(R.string.permission_denied),
                    LENGTH_LONG
                ).show()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bottomMenu = requireActivity().findViewById<BottomNavigationView>(R.id.menu_bottom_nav)
        bottomMenu.isVisible = true

        bindUi()
        saveUserInfoButton()

        binding.chooseAvatarButton.setOnClickListener {
            chooseProfilePicture()
        }
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        binding.editTextDateOfBirth.setOnClickListener {
            val datePicker = MaterialDatePicker
                .Builder
                .datePicker()
                .setTitleText(resources.getString(R.string.date_picker_title))
                .build()
            datePicker.addOnPositiveButtonClickListener {
                val date = sdf.format(it)
                binding.editTextDateOfBirth.setText(date)
            }
            datePicker.show(requireActivity().supportFragmentManager, "DATE")
        }
    }

    private fun chooseProfilePicture() {
        val options = arrayOf(
            getString(R.string.take_a_picture),
            getString(R.string.pick_from_gallery),
        )
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.download_photo))
            .setItems(options,
                DialogInterface.OnClickListener { _, which ->
                    when (which) {
                        0 -> {
                            if (ContextCompat.checkSelfPermission(
                                    requireContext(),
                                    android.Manifest.permission.CAMERA
                                ) == PackageManager.PERMISSION_GRANTED
                            ) {
                                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                                resultLauncherCamera.launch(intent)
                            } else {
                                permissionResultLauncher.launch(android.Manifest.permission.CAMERA)
                            }
                        }
                        1 -> {
                            val intent = Intent(Intent.ACTION_PICK)
                            intent.type = "image/*"
                            resultLauncherGallery.launch(intent)
                        }
                    }
                }).create()
        dialog.show()
    }

    private fun onActivityResult(result: ActivityResult, requestCode: Int) {
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            when (requestCode) {
                GALLERY_REQUEST -> {
                    val imageUri = data?.data
                    val bitmap = when {
                        Build.VERSION.SDK_INT < 28 -> MediaStore.Images.Media.getBitmap(
                            requireActivity().contentResolver,
                            imageUri
                        )
                        else -> {
                            val source =
                                ImageDecoder.createSource(
                                    requireActivity().contentResolver,
                                    imageUri!!
                                )
                            ImageDecoder.decodeBitmap(source)
                        }
                    }
                    viewModel.updateProfilePicture(bitmap)
                    binding.imageProfilePicture.setImageBitmap(bitmap)
                }
                CAMERA_REQUEST -> {
                    val extras = data!!.extras
                    val bitmap = extras!!.get("data") as Bitmap
                    viewModel.updateProfilePicture(bitmap)
                    binding.imageProfilePicture.setImageBitmap(bitmap)
                }
            }
        }
    }


    private fun bindUi() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getUserInfo().collect {
                    binding.apply {
                        editTextFirstName.setText(it.firstName)
                        editTextLastName.setText(it.lastName)
                        editTextDateOfBirth.setText(it.dateOfBirth)
                        if (it.profilePicture == null) {
                            imageProfilePicture.setImageResource(R.drawable.avatar_solid)
                        } else
                            imageProfilePicture.setImageBitmap(it.profilePicture)
                    }
                }
            }
        }
    }

    private fun saveUserInfoButton() {
        binding.imageSaveSettings.setOnClickListener {
            val firstName = binding.editTextFirstName.text.toString()
            val lastName = binding.editTextLastName.text.toString()
            val dateOfBirth = binding.editTextDateOfBirth.text.toString() ?: ""
            val profilePicture = getBitmapFromView(binding.imageProfilePicture)

            val resourceForValidation = isInputsValid(firstName, lastName)
            if (resourceForValidation.isValid) {
                val settingsUserInfo =
                    SettingsUserInfo(firstName, lastName, dateOfBirth, profilePicture)
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