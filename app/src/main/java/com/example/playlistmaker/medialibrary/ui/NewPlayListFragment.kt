package com.example.playlistmaker.medialibrary.ui

import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentNewPlayListBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class NewPlayListFragment : Fragment() {
    private lateinit var binding: FragmentNewPlayListBinding
    private lateinit var navBar: BottomNavigationView
    private val viewModel by activityViewModel<NewPlayListViewModel>()

    private var title: String = EMPTY
    private var description: String = EMPTY
    private var newUri: String = EMPTY

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewPlayListBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navBar = requireActivity().findViewById(R.id.bottomNavigationView)
        navBar.isVisible = false

        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                //обрабатываем событие выбора пользователем фотографии
                if (uri != null) {
                    binding.playListCover.setImageURI(uri)
                    newUri = uri.toString()
                } else {
                    Log.d("PhotoPicker", "No media selected")
                }
            }

        binding.imageContainer.setOnClickListener {
            viewModel.checkPermissions()
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
        binding.createButton.setOnClickListener {
            newUri = viewModel.saveFile(newUri.toUri()).toString()
            viewModel.createPlayList(title, description, newUri.toUri())
            findNavController().navigateUp()
        }

        val textWatcherTitle = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                title = s.toString()
                if (title.isNotEmpty()) {
                    binding.createButton.isEnabled = true
                    binding.enterTitle.setBoxStrokeColorStateList(
                        requireContext().getColorStateList(
                            R.color.new_play_list_text_input_state_not_empty
                        )
                    )
                    binding.enterTitle.defaultHintTextColor =
                        (requireContext().getColorStateList(R.color.new_play_list_text_input_state_not_empty))
                }
                if (title.isEmpty()) {
                    binding.createButton.isEnabled = false
                    binding.enterTitle.setBoxStrokeColorStateList(
                        requireContext().getColorStateList(
                            R.color.new_play_list_text_input_state_default
                        )
                    )
                    binding.enterTitle.defaultHintTextColor =
                        (requireContext().getColorStateList(R.color.new_play_list_hint_state_default))
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // empty
            }
        }
        binding.enterTitleEdit.addTextChangedListener(textWatcherTitle)

        val textWatcherDescription = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                description = s.toString()
                if (description.isNotEmpty()) {
                    binding.enterDescription.setBoxStrokeColorStateList(
                        requireContext().getColorStateList(
                            R.color.new_play_list_text_input_state_not_empty
                        )
                    )
                    binding.enterDescription.defaultHintTextColor =
                        (requireContext().getColorStateList(R.color.new_play_list_text_input_state_not_empty))
                }
                if (description.isEmpty()) {
                    binding.enterDescription.setBoxStrokeColorStateList(
                        requireContext().getColorStateList(
                            R.color.new_play_list_text_input_state_default
                        )
                    )
                    binding.enterDescription.defaultHintTextColor =
                        (requireContext().getColorStateList(R.color.new_play_list_hint_state_default))
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // empty
            }
        }
        binding.enterDescriptionEdit.addTextChangedListener(textWatcherDescription)
    }

    override fun onDestroy() {
        super.onDestroy()
        navBar.isVisible = true
    }

    companion object {
        const val EMPTY = ""
    }
}