package com.example.crypto.views.fragments.settings_screen

import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
import com.example.crypto.model.constans.*
import com.example.crypto.model.settings_db.SettingsUserInfo
import com.example.crypto.utils.StoragePhoto
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

class SettingsScreenFragment : Fragment(R.layout.fragment_settings_screen) {

    private var _binding: FragmentSettingsScreenBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SettingsScreenViewModel by inject()

    private val permissionResultLauncher =
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
        }

    private val takePhoto = registerForActivityResult(ActivityResultContracts.GetContent()) {
        deletePhotoFromInternalStorage()
        val bitmap = if (Build.VERSION.SDK_INT < 28) {
            MediaStore.Images.Media.getBitmap(
                requireContext().contentResolver,
                it
            )
        } else {
            val source = ImageDecoder.createSource(requireContext().contentResolver, it)
            ImageDecoder.decodeBitmap(source)
        }
        val isSavedSuccessfully = savePhotoToStorage(bitmap)
        if (isSavedSuccessfully) {
            setPhotoIntoImageView()
        } else {
            Snackbar.make(requireView(), getString(R.string.error_save_photo), LENGTH_LONG).show()
        }
    }
    private val makePhoto =
        registerForActivityResult(ActivityResultContracts.TakePicturePreview()) {
            deletePhotoFromInternalStorage()
            val isSavedSuccessfully = savePhotoToStorage(it)
            if (isSavedSuccessfully) {
                setPhotoIntoImageView()
            } else {
                Snackbar.make(requireView(), getString(R.string.error_save_photo), LENGTH_LONG)
                    .show()
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

        viewModel.setEvent(Event.FetchUserInfo)

        bindUi()
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

    private fun setPhotoIntoImageView() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                val photo = loadPhotoFromStorage()
                if (photo.isEmpty()) {
                    binding.imageProfilePicture.setImageResource(R.drawable.avatar_solid)
                } else {
                    binding.imageProfilePicture.setImageBitmap(photo.first().bitmap)
                }
            }
        }
    }

    private fun deletePhotoFromInternalStorage() = requireContext().deleteFile(PROFILE_PHOTO_NAME)

    private suspend fun loadPhotoFromStorage(): List<StoragePhoto> {
        return withContext(Dispatchers.IO) {
            val files = requireContext().filesDir.listFiles()
            files?.filter { it.name == PROFILE_PHOTO_NAME }?.map {
                val bytes = it.readBytes()
                val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                StoragePhoto(it.name, bitmap)
            } ?: listOf()
        }
    }

    private fun savePhotoToStorage(bitmap: Bitmap): Boolean {
        return try {
            requireContext().openFileOutput(PROFILE_PHOTO_NAME, Context.MODE_PRIVATE)
                .use { stream ->
                    if (!bitmap.compress(Bitmap.CompressFormat.JPEG, 95, stream)) {
                        throw IOException("Couldn't save bitmap")
                    }
                }
            true
        } catch (e: IOException) {
            false
        }
    }

    private fun chooseProfilePicture() {
        val options = arrayOf(
            getString(R.string.take_a_picture),
            getString(R.string.pick_from_gallery),
        )
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.download_photo))
            .setItems(options) { _, which ->
                when (which) {
                    0 -> {
                        if (ContextCompat.checkSelfPermission(
                                requireContext(),
                                android.Manifest.permission.CAMERA
                            ) == PackageManager.PERMISSION_GRANTED
                        ) {
                            makePhoto.launch()

                        } else {
                            permissionResultLauncher.launch(android.Manifest.permission.CAMERA)
                        }
                    }
                    1 -> {
                        takePhoto.launch("image/*")
                    }
                }
            }.create()
        dialog.show()
    }

    private fun bindUi() = lifecycleScope.launch {
        setPhotoIntoImageView()
        viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.uiState.collect {
                when (it) {
                    is State.FilledSettings -> {
                        if (it.equals(null)) {
                            binding.apply {
                                editTextFirstName.setText("")
                                editTextLastName.setText("")
                                editTextDateOfBirth.setText("")
                            }
                        } else {
                            binding.apply {
                                editTextFirstName.setText(it.data?.firstName)
                                editTextLastName.setText(it.data?.lastName)
                                editTextDateOfBirth.setText(it.data?.dateOfBirth)
                            }
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
            val isValid = isInputsValid(firstName, lastName)
            if (isValid) {
                val settingsUserInfo =
                    SettingsUserInfo(firstName, lastName, dateOfBirth)
                viewModel.setEvent(Event.SaveUserInfo(settingsUserInfo))
                Snackbar.make(it, getString(R.string.success_input), LENGTH_LONG).show()
            } else {
                Snackbar.make(it, getString(R.string.save_error), LENGTH_LONG).show()
            }
        }
    }

    private fun isInputsValid(firstName: String, lastName: String): Boolean {
        return !(firstName.isEmpty() || lastName.isEmpty()
                || lastName.length > MAX_INPUT_SIZE
                || firstName.length > MAX_INPUT_SIZE)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}