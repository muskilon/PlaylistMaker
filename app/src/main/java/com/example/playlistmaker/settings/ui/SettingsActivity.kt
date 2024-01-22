package com.example.playlistmaker.settings.ui

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.R
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import com.example.playlistmaker.main.ui.MainActivity.Companion.NIGHT_MODE_KEY

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val sharedPreferences = Creator.getSharedPreferences()

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

        when (this.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)) {
            Configuration.UI_MODE_NIGHT_YES -> {
                binding.themeSwitch.isChecked = true
            }
            Configuration.UI_MODE_NIGHT_NO -> {
                binding.themeSwitch.isChecked = false
            }
            Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                binding.themeSwitch.isChecked = false
            }
        }

        binding.themeSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                sharedPreferences.edit()
                    .putBoolean(NIGHT_MODE_KEY, true)
                    .apply()
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                sharedPreferences.edit()
                    .putBoolean(NIGHT_MODE_KEY, false)
                    .apply()
            }

        }
    }
}