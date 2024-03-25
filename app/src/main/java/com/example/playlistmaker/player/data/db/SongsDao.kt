package com.example.playlistmaker.player.data.db

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

    @Query("SELECT trackId FROM songs_table")
    suspend fun getTrackIdList(): List<String>

    @Delete(entity = SongsEntity::class)
    fun deleteSong(songsEntity: SongsEntity)
}