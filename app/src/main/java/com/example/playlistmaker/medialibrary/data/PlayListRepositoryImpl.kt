package com.example.playlistmaker.medialibrary.data

import com.example.playlistmaker.medialibrary.domain.PlayList
import com.example.playlistmaker.medialibrary.domain.PlayListRepository
import com.example.playlistmaker.player.data.PlayListDbConvertor
import com.example.playlistmaker.player.data.db.AppDatabase
import com.example.playlistmaker.search.domain.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class PlayListRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val playListDbConvertor: PlayListDbConvertor
) : PlayListRepository {
    private val playListStorage = ArrayList<PlayList>()

    override suspend fun addTrackToPlayList(playList: PlayList, track: Track) {
        appDatabase.playListsDao().updatePlayList(playListDbConvertor.map(playList))
        appDatabase.playListsDao().addTrackToPlayListStorage(track)
    }

    override suspend fun addPlayList(playList: PlayList) {
        appDatabase.playListsDao().addPlayList(playListDbConvertor.map(playList))
        updatePlayListsList()
    }

    override fun getPlayLists(): List<PlayList> {
        return playListStorage
    }

    override suspend fun updatePlayListsList() {
        getPlayListsFromDb().collect { result ->
            playListStorage.clear()
            playListStorage.addAll(result)
        }
    }

    override suspend fun deletePlayList(playList: PlayList) {
        appDatabase.playListsDao().deletePlayList(playListDbConvertor.map(playList))
    }

    private fun getPlayListsFromDb(): Flow<List<PlayList>> = flow {
        val playLists = appDatabase.playListsDao().getAllPlayLists()
        val data = playLists.map {
            playListDbConvertor.map(it)
        }
        emit(data)
    }.flowOn(Dispatchers.IO)

}