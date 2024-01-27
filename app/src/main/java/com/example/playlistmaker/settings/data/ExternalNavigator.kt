package com.example.playlistmaker.settings.data

import android.content.Context
import android.content.Intent
import android.net.Uri

class ExternalNavigator(
    private val context: Context
) {
    fun shareLink(link: String) {
        val sendApp: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, link)
            type = "text/plain"
            Intent.createChooser(this, null)
        }
        context.startActivity(sendApp)

    }

    fun openLink(link: String) {
        val termsOfUse = Intent(Intent.ACTION_VIEW, Uri.parse(link))
        context.startActivity(termsOfUse)

    }

    fun openEmail(emailData: EmailData) {
        val sendFeedback = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_SUBJECT, emailData.subject)
            putExtra(Intent.EXTRA_TEXT, emailData.text)
            putExtra(Intent.EXTRA_EMAIL, arrayOf(emailData.address))
        }
        context.startActivity(sendFeedback)

    }
}