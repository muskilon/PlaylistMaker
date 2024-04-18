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
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentNewPlayListBinding
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel

class EditPlayListFragment : Fragment() {
    private lateinit var binding: FragmentNewPlayListBinding
    private val viewModel by viewModel<EditPlayListViewModel>()

    private var title: String = EMPTY
    private var description: String = EMPTY
    private var newUri: Uri? = null
    private var playListId = 0L

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewPlayListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.loadPlayList(requireArguments().getLong(PLAYLIST))
        binding.navText.text = getString(R.string.edit)
        binding.createButton.text = getString(R.string.save)

        viewModel.getPlayLists().observe(viewLifecycleOwner) { playList ->
            playListId = playList.id
            title = playList.title
            description = playList.description ?: EMPTY
            newUri = playList.cover
            binding.enterTitleEdit.setText(title)
            binding.enterDescriptionEdit.setText(description)
            playList.cover?.let {
                binding.playListCover.setImageURI(playList.cover)
                binding.imageContainer.foreground = null
            }
        }

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
            viewModel.updatePlayList(title, description, newUri)
            Snackbar.make(view, getString(R.string.playlist_updated, title), Snackbar.LENGTH_LONG)
                .show()
            findNavController().navigateUp()
//            TODO надо разобраться с навигацией
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
        findNavController().navigate(
            R.id.action_editPlayListFragment_to_singlePlayListFragment,
            bundleOf(PLAYLIST to playListId)
        )
    }

    companion object {
        const val EMPTY = ""
        const val PLAYLIST = "playlist"
    }
}