package com.example.playlistmaker.di

import android.content.Context
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import com.example.playlistmaker.MyApplication
import com.example.playlistmaker.player.data.MusicPlayer
import com.example.playlistmaker.player.data.MusicPlayerImpl
import com.example.playlistmaker.search.data.HistorySharedPreferences
import com.example.playlistmaker.search.data.ItunesAPI
import com.example.playlistmaker.search.data.NetworkClient
import com.example.playlistmaker.search.data.RetrofitNetworkClient
import com.example.playlistmaker.settings.data.ExternalNavigator
import com.example.playlistmaker.settings.data.SettingsStorage
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModules = module {
    factory { Gson() }
    single { Handler(Looper.getMainLooper()) }
    single {
        androidContext().getSharedPreferences(
            MyApplication.SHARED_PREFERENCES,
            Context.MODE_PRIVATE
        )
    }

//  Settings
    single { ExternalNavigator(context = androidContext()) }
    single { SettingsStorage(sharedPreferences = get()) }

//  Search
    single<NetworkClient> { RetrofitNetworkClient(androidContext(), itunesAPI = get()) }
    single<ItunesAPI> {
        Retrofit.Builder()
            .baseUrl("https://itunes.apple.com/search/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ItunesAPI::class.java)
    }
    single<HistorySharedPreferences> {
        HistorySharedPreferences(
            sharedPreferences = get(),
            gson = get()
        )
    }

//  Player
    factory<MusicPlayer> { MusicPlayerImpl(mediaPlayer = get()) }
    factory { MediaPlayer() }

}