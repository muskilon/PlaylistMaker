package com.example.playlistmaker.medialibrary.data

import com.example.playlistmaker.medialibrary.domain.PlayListRepository
import com.example.playlistmaker.player.data.PlayListTracksDbConvertor
import com.example.playlistmaker.player.data.db.AppDatabase
import com.example.playlistmaker.player.data.db.PlayListEntity
import com.example.playlistmaker.search.domain.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class PlayListRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val playListDbConvertor: PlayListTracksDbConvertor
) : PlayListRepository {
    private val playListStorage = ArrayList<PlayListEntity>()

    override suspend fun addTrackToPlayList(track: Track, playListId: Long) {
        appDatabase.playListsDao().addTrackToPlayList(playListDbConvertor.map(track, playListId))
    }

    override suspend fun addPlayList(playList: PlayListEntity) {
        appDatabase.playListsDao().addPlayList(playList)
        updatePlayListsList()
    }

    override fun getPlayLists(): List<PlayListEntity> {
        return playListStorage
    }

    override suspend fun updatePlayListsList() {
        getPlayListsFromDb().collect { result ->
            playListStorage.clear()
            playListStorage.addAll(result)
        }
    }

    private fun getPlayListsFromDb(): Flow<List<PlayListEntity>> = flow {
        val playLists = appDatabase.playListsDao().getAllPlayLists()
        emit(playLists)
    }.flowOn(Dispatchers.IO)
}