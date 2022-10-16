package com.example.crypto.views.fragments.settings_screen

import android.app.AlertDialog
import android.content.pm.PackageManager
import android.graphics.ImageDecoder
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.crypto.R
import com.example.crypto.databinding.FragmentSettingsScreenBinding
import com.example.crypto.model.settings_db.SettingsUserInfo
import com.example.crypto.view_models.SettingsScreenViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_LONG
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import java.text.SimpleDateFormat
import java.util.*
import com.example.crypto.views.fragments.settings_screen.SettingsScreenContract.*

class SettingsScreenFragment : Fragment(R.layout.fragment_settings_screen) {

    private var _binding: FragmentSettingsScreenBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SettingsScreenViewModel by inject()
    private val makePhoto =
        registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
            viewModel.apply {
                setEvent(Event.SaveAvatar(bitmap))
            }
        }

    private val takePhoto = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        val bitmap = when {
            uri == null -> null
            Build.VERSION.SDK_INT < 28 -> {
                MediaStore.Images.Media.getBitmap(requireContext().contentResolver, uri)
            }
            else -> {
                val source = ImageDecoder.createSource(requireContext().contentResolver, uri)
                ImageDecoder.decodeBitmap(source)
            }
        }
        viewModel.setEvent(Event.SaveAvatar(bitmap))
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

        viewModel.setEvent(Event.FetchUserInfo)

        bindUi()
        collectEffects()
        saveInfoButtonClickListener()

        binding.buttonChooseAvatar.setOnClickListener {
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
            .setItems(options) { _, dialogItem ->
                if (dialogItem == 0) {
                    // Check camera permission and make photo if granted
                    // Else - request permission and make photo
                    if (ContextCompat.checkSelfPermission(
                            requireContext(),
                            android.Manifest.permission.CAMERA
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        makePhoto.launch()
                    } else {
                        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
                            if (it) {
                                makePhoto.launch()
                            } else {
                                Snackbar.make(
                                    requireView(),
                                    resources.getString(R.string.permission_denied),
                                    LENGTH_LONG
                                ).show()
                            }
                        }.launch(android.Manifest.permission.CAMERA)
                    }
                } else {
                    takePhoto.launch("image/*")
                }
            }.create()
        dialog.show()
    }

    private fun collectEffects() = lifecycleScope.launch {
        viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.effect.collect {
                when (it) {
                    Effect.ErrorToSavePhoto -> {
                        Snackbar.make(
                            requireView(),
                            getString(R.string.error_save_photo),
                            LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
    }

    private fun bindUi() = lifecycleScope.launch {
        viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.uiState.collect {
                when (it) {
                    is State.FilledSettings -> {
                        if (it.data == null) {
                            binding.apply {
                                editTextFirstName.setText("")
                                editTextLastName.setText("")
                                editTextDateOfBirth.setText("")
                            }
                        } else {
                            binding.apply {
                                editTextFirstName.setText(it.data.firstName)
                                editTextLastName.setText(it.data.lastName)
                                editTextDateOfBirth.setText(it.data.dateOfBirth)
                            }
                        }
                        if (it.avatar.isEmpty()) {
                            binding.imageProfilePicture.setImageResource(R.drawable.avatar_solid)
                        } else {
                            binding.imageProfilePicture.setImageBitmap(it.avatar.first().bitmap)
                        }
                    }
                    State.IdleState -> {}
                }
            }
        }
    }

    private fun saveInfoButtonClickListener() {
        binding.imageSaveSettings.setOnClickListener {
            val firstName = binding.editTextFirstName.text.toString()
            val lastName = binding.editTextLastName.text.toString()
            val dateOfBirth = binding.editTextDateOfBirth.text.toString()
            if (viewModel.isInputValid(firstName, lastName)) {
                val settingsUserInfo =
                    SettingsUserInfo(firstName, lastName, dateOfBirth)
                viewModel.setEvent(Event.SaveUserInfo(settingsUserInfo))
                Snackbar.make(it, getString(R.string.success_input), LENGTH_LONG).show()
            } else {
                Snackbar.make(it, getString(R.string.save_error), LENGTH_LONG).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}