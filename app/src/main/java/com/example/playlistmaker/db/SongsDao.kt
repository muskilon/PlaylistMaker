package com.example.playlistmaker.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface SongsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSong(song: SongsEntity)

    @Query("SELECT * FROM songs_table")
    suspend fun getSongs(): List<SongsEntity>

    @Delete(entity = SongsEntity::class)
    fun deleteSong(songsEntity: SongsEntity)
}