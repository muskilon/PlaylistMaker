package com.example.playlistmaker.settings.data

import com.example.playlistmaker.MyApplication
import com.example.playlistmaker.R
import com.example.playlistmaker.settings.domain.SharingInterActor

class SharingInterActorImpl(
    private val externalNavigator: ExternalNavigator,
) : SharingInterActor {
    override fun sharePlaylist(message: String) {
        externalNavigator.shareLink(message)
    }

    override fun shareApp() {
        externalNavigator.shareLink(getShareAppLink())
    }

    override fun openTerms() {
        externalNavigator.openLink(getTermsLink())
    }

    override fun openSupport() {
        externalNavigator.openEmail(getSupportEmailData())
    }

    private fun getShareAppLink(): String {
        return MyApplication.getAppResources().getString(R.string.share_app_url)
    }

    private fun getSupportEmailData(): EmailData {
        return EmailData()
    }

    private fun getTermsLink(): String {
        return MyApplication.getAppResources().getString(R.string.terms_of_use_url)
    }
}