package com.cybershark.jokes.data.room

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface FavoriteJokeDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun saveJoke(jokeEntity: JokeEntity): Long

    @Delete
    suspend fun removeJoke(jokeEntity: JokeEntity): Int

    @Query("select * from jokes")
    fun getAllSavedJokes(): LiveData<List<JokeEntity>>

    @Query("delete from jokes")
    suspend fun deleteAllJokes(): Int

    @Query("select count(*) from jokes where jokeId=:jokeId")
    suspend fun doesJokeExist(jokeId: Int): Int

}
