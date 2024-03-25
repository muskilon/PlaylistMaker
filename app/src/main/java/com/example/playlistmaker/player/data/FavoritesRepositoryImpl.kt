package com.example.playlistmaker.player.data

import com.example.playlistmaker.player.data.db.AppDatabase
import com.example.playlistmaker.player.data.db.SongsEntity
import com.example.playlistmaker.player.domain.FavoritesRepository
import com.example.playlistmaker.search.domain.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class FavoritesRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val songsDbConvertor: SongsDbConvertor
) : FavoritesRepository {
    override fun getFavoritesSongs(): Flow<List<Track>> = flow {
        val songs = appDatabase.songsDao().getSongs()
        emit(convertFromSongsEntity(songs))
    }.flowOn(Dispatchers.IO)

    override suspend fun addSongToFavorites(song: Track) {
        appDatabase.songsDao().insertSong(songsDbConvertor.map(song))
    }

    override suspend fun deleteSongFromFavorites(song: Track) {
        appDatabase.songsDao().deleteSong(songsDbConvertor.map(song))
    }

    private fun convertFromSongsEntity(songs: List<SongsEntity>): List<Track> {
        return songs.map { songs -> songsDbConvertor.map(songs) }
    }
}