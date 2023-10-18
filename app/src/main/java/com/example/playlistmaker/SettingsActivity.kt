package com.example.playlistmaker

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val backArrow = findViewById<ImageView>(R.id.arrow_back)
        backArrow.setOnClickListener {
            val back = Intent(this, MainActivity::class.java)
            startActivity(back)
        }

        val termsOfUseArrow = findViewById<ImageView>(R.id.terms_of_use_arrow)
        termsOfUseArrow.setOnClickListener {
            val termsOfUse =
                Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.terms_of_use_url)))
            startActivity(termsOfUse)
        }

        val shareAppIcon = findViewById<ImageView>(R.id.share_app_icon)
        shareAppIcon.setOnClickListener {
            val sendApp: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, getString(R.string.share_app_url))
                type = "text/plain"
            }
            val shareApp = Intent.createChooser(sendApp, null)
            startActivity(shareApp)
        }

        val feedbackIcon = findViewById<ImageView>(R.id.write_to_support_icon)
        feedbackIcon.setOnClickListener {
            val sendFeedback = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse(getString(R.string.email_adress))
                putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_subject))
                putExtra(Intent.EXTRA_TEXT, getString(R.string.email_text))
            }
            startActivity(sendFeedback)
        }

    }
}