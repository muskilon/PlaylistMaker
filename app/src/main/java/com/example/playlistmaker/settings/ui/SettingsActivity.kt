package com.example.playlistmaker.settings.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.R
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
            val termsOfUse =
                Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.terms_of_use_url)))
            startActivity(termsOfUse)
        }

        binding.shareAppIcon.setOnClickListener {
            val sendApp: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, getString(R.string.share_app_url))
                type = "text/plain"
                Intent.createChooser(this, null)
            }
            startActivity(sendApp)
        }

        binding.writeToSupportIcon.setOnClickListener {
            val sendFeedback = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_subject))
                putExtra(Intent.EXTRA_TEXT, getString(R.string.email_text))
                putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.email_address)))
            }
            startActivity(sendFeedback)
        }
        observeNightTheme()

        binding.themeSwitch.setOnCheckedChangeListener { _, isChecked ->
            Log.d("TAG", "Свичер переключился в положение: $isChecked")
            viewModel.updateThemeSetting(isChecked)
        }
    }

    private fun observeNightTheme() {
        viewModel.getThemeState().observe(this) {
            Log.d("TAG", "Обновление liveData =$it")
            Log.d(
                "TAG",
                "Статус свичера до изменения в обсервере ${binding.themeSwitch.isChecked}"
            )
            binding.themeSwitch.isChecked = it
            Log.d(
                "TAG",
                "Статус свичера после изменения в обсервере ${binding.themeSwitch.isChecked}"
            )
        }
    }
}