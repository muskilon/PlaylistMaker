package com.example.playlistmaker.medialibrary.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentNewPlayListBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class NewPlayListFragment : Fragment() {
    private lateinit var binding: FragmentNewPlayListBinding
    private lateinit var navBar: BottomNavigationView
    private val viewModel by activityViewModel<NewPlayListViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewPlayListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navBar = requireActivity().findViewById(R.id.bottomNavigationView)
        navBar.isVisible = false

        val textWatcherTitle = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val playListTitle = s.toString()
                if (playListTitle.isNotEmpty()) {
                    binding.createButton.isEnabled = true
                    binding.enterTitle.setBoxStrokeColorStateList(
                        requireContext().getColorStateList(
                            R.color.new_play_list_text_input_state_not_empty
                        )
                    )
                    binding.enterTitle.defaultHintTextColor =
                        (requireContext().getColorStateList(R.color.new_play_list_text_input_state_not_empty))
                }
                if (playListTitle.isEmpty()) {
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
                val playListDescription = s.toString()
                if (playListDescription.isNotEmpty()) {
                    binding.enterDescription.setBoxStrokeColorStateList(
                        requireContext().getColorStateList(
                            R.color.new_play_list_text_input_state_not_empty
                        )
                    )
                    binding.enterDescription.defaultHintTextColor =
                        (requireContext().getColorStateList(R.color.new_play_list_text_input_state_not_empty))
                }
                if (playListDescription.isEmpty()) {
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
}