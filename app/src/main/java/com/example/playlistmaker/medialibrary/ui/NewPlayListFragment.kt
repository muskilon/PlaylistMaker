package com.example.playlistmaker.medialibrary.ui

import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentNewPlayListBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel

open class NewPlayListFragment : Fragment() {
    private lateinit var binding: FragmentNewPlayListBinding
    private val viewModel by viewModel<NewPlayListViewModel>()

    private var title: String = EMPTY
    private var description: String = EMPTY
    private var newUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewPlayListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                uri?.let {
                    binding.playListCover.setImageURI(uri)
                    binding.imageContainer.foreground = null
                    newUri = uri
                }
            }

        binding.imageContainer.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
        binding.createButton.setOnClickListener {
            viewModel.createPlayList(title, description, newUri)
            Snackbar.make(view, getString(R.string.play_list_created, title), Snackbar.LENGTH_LONG)
                .show()
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

        binding.arrowBack.setOnClickListener { exit() }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    exit()
                }
            })
    }

    private fun exit() {
        if (title.isNotEmpty() || description.isNotEmpty() || newUri != null) {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(getString(R.string.play_list_dialog_title))
                .setMessage(getString(R.string.play_list_dialog_message))
                .setNegativeButton(getString(R.string.play_list_dialog_negative)) { _, _ ->

                }
                .setPositiveButton(getString(R.string.play_list_dialog_positive)) { _, _ ->
                    findNavController().navigateUp()
                }
                .show()
        } else findNavController().navigateUp()
    }

    companion object {
        const val EMPTY = ""
    }
}