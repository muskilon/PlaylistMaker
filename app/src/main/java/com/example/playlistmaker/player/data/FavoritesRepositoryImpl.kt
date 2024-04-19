package com.example.playlistmaker.player.data

import com.example.playlistmaker.db.AppDatabase
import com.example.playlistmaker.db.SongsEntity
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
    private val favoritesStorage = ArrayList<Track>()

    override suspend fun addSongToFavorites(song: Track) {
        appDatabase.favoritesDao().insertSong(songsDbConvertor.map(song))
        updateFavorites()
    }

    override suspend fun deleteSongFromFavorites(song: Track) {
        appDatabase.favoritesDao().deleteSong(songsDbConvertor.map(song))
        updateFavorites()
    }

    override suspend fun updateFavorites() {
        getFavoritesFromDb().collect { result ->
            with(favoritesStorage) {
                clear()
                addAll(result)
            }
        }
    }

    override fun getFavorites(): List<Track> {
        return favoritesStorage
    }

    private fun convertFromSongsEntity(songs: List<SongsEntity>): List<Track> {
        return songs.map { tracks -> songsDbConvertor.map(tracks) }
    }

    private fun getFavoritesFromDb(): Flow<List<Track>> = flow {
        val songs = appDatabase.favoritesDao().getSongs().asReversed()
        emit(convertFromSongsEntity(songs))
    }.flowOn(Dispatchers.IO)
}