package com.example.playlistmaker.settings.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.databinding.ActivitySettingsBinding


class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    private lateinit var viewModel: SettingsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        viewModel =
            ViewModelProvider(this, SettingsViewModelFactory(this))[SettingsViewModel::class.java]

        binding.backArrow.setOnClickListener {
            this.finish()
        }

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
        viewModel.getThemeState().observe(this) {
            binding.themeSwitch.isChecked = it
        }
    }
}