package com.example.playlistmaker.settings.data

import com.example.playlistmaker.MyApplication
import com.example.playlistmaker.R

data class EmailData(
    val subject: String = MyApplication.getAppResources().getString(R.string.email_subject),
    val text: String = MyApplication.getAppResources().getString(R.string.email_text),
    val address: String = MyApplication.getAppResources().getString(R.string.email_address)
)
