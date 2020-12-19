package com.cybershark.jokes.data.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [JokeEntity::class], version = 1)
abstract class FavoriteJokeDB : RoomDatabase() {

    abstract fun favoriteJokeDao(): FavoriteJokeDao

}
