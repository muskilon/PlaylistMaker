package com.example.playlistmaker.presentation.ui

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import com.example.playlistmaker.R
import com.example.playlistmaker.presentation.ui.MainActivity.Companion.NIGHT_MODE_KEY

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val backArrow = findViewById<ImageView>(R.id.backArrow)
        backArrow.setOnClickListener {
            this.finish()
        }

        val termsOfUseArrow = findViewById<ImageView>(R.id.termsOfUseArrow)
        termsOfUseArrow.setOnClickListener {
            val termsOfUse =
                Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.terms_of_use_url)))
            startActivity(termsOfUse)
        }

        val shareAppIcon = findViewById<ImageView>(R.id.shareAppIcon)
        shareAppIcon.setOnClickListener {
            val sendApp: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, getString(R.string.share_app_url))
                type = "text/plain"
                Intent.createChooser(this, null)
            }
            startActivity(sendApp)
        }

        val feedbackIcon = findViewById<ImageView>(R.id.writeToSupportIcon)
        feedbackIcon.setOnClickListener {
            val sendFeedback = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_subject))
                putExtra(Intent.EXTRA_TEXT, getString(R.string.email_text))
                putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.email_address)))
            }
            startActivity(sendFeedback)
        }

        val themeSwitcher = findViewById<SwitchCompat>(R.id.themeSwitch)
        when (this.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)) {
            Configuration.UI_MODE_NIGHT_YES -> {
                themeSwitcher.isChecked = true
            }
            Configuration.UI_MODE_NIGHT_NO -> {
                themeSwitcher.isChecked = false
            }
            Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                themeSwitcher.isChecked = false
            }
        }

        themeSwitcher.setOnCheckedChangeListener { _, isChecked ->
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