package com.example.playlistmaker.settings.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.FragmentSettingsBinding
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class SettingsFragment : Fragment() {
    private lateinit var binding: FragmentSettingsBinding
    private val viewModel by activityViewModel<SettingsViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.termsOfUseArrow.setOnClickListener {
            viewModel.openTerms()
        }

        binding.shareAppIcon.setOnClickListener {
            viewModel.shareApp()
        }

        binding.writeToSupportIcon.setOnClickListener {
            viewModel.openSupport()
        }
        observeNightTheme()

        binding.themeSwitch.setOnCheckedChangeListener { _, isChecked ->
            viewModel.updateThemeSetting(isChecked)
        }
    }

    private fun observeNightTheme() {
        viewModel.getThemeState().observe(viewLifecycleOwner) {
            binding.themeSwitch.isChecked = it
        }
    }
}