package com.example.playlistmaker.medialibrary.ui

import android.Manifest
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.medialibrary.domain.FilesInteractor
import com.example.playlistmaker.medialibrary.domain.PlayListInteractor
import com.example.playlistmaker.player.data.db.PlayListEntity
import com.markodevcic.peko.PermissionRequester
import com.markodevcic.peko.PermissionResult
import kotlinx.coroutines.launch

class NewPlayListViewModel(
    private val filesInteractor: FilesInteractor,
    private val playListInteractor: PlayListInteractor
) : ViewModel() {
    fun saveFile(uri: Uri): Uri {
        return filesInteractor.saveFile(uri)
    }

    fun createPlayList(title: String, description: String, uri: Uri) {
        Log.d("TAG", uri.toString())
        viewModelScope.launch {
            playListInteractor.addPlayList(
                PlayListEntity(
                    title = title,
                    description = description,
                    cover = uri.toString(),
                    id = 0L
                )
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun checkPermissions() {
        Log.d("TAG", "touch")
        val requester = PermissionRequester.instance()
        val granted: Boolean = requester.areGranted(Manifest.permission.READ_MEDIA_IMAGES)
        if (granted) {
            Log.d("TAG", "Разрешение есть")
        } else {
            viewModelScope.launch {
                requester.request(
                    Manifest.permission.READ_MEDIA_IMAGES
                ).collect { result ->
                    when (result) {
                        is PermissionResult.Granted -> Log.d("TAG", "Разрешение есть")
                        is PermissionResult.Denied -> Log.d(
                            "TAG",
                            "Пользователь отказал в разрешении"
                        )// Пользователь отказал в предоставлении разрешения
                        is PermissionResult.Denied.NeedsRationale -> Log.d(
                            "TAG",
                            "Разрешение на фото очень нужно"
                        )

                        is PermissionResult.Denied.DeniedPermanently -> Log.d(
                            "TAG",
                            "идем в настройки"
                        )

                        is PermissionResult.Cancelled -> Log.d("TAG", "ничего не делаем, уходим")
                    }
                }
            }
        }
    }
}