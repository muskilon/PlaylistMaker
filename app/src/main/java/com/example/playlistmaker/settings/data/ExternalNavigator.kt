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
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(sendApp)

    }

    fun openLink(link: String) {
        val termsOfUse = Intent(Intent.ACTION_VIEW, Uri.parse(link)).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(termsOfUse)

    }

    fun openEmail(emailData: EmailData) {
        val sendFeedback = Intent(Intent.ACTION_SENDTO).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_SUBJECT, emailData.subject)
            putExtra(Intent.EXTRA_TEXT, emailData.text)
            putExtra(Intent.EXTRA_EMAIL, arrayOf(emailData.address))
        }
        context.startActivity(sendFeedback)

    }
}