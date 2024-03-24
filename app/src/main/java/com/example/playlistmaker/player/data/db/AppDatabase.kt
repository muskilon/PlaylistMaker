package com.example.playlistmaker.player.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(version = 1, entities = [SongsEntity::class])
abstract class AppDatabase : RoomDatabase() {
    abstract fun songsDao(): SongsDao
}