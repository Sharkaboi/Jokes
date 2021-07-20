package com.cybershark.jokes.data.respositories

import androidx.lifecycle.LiveData
import com.cybershark.jokes.data.api.JokeApiService
import com.cybershark.jokes.data.models.Joke
import com.cybershark.jokes.data.room.FavoriteJokeDao
import com.cybershark.jokes.data.room.JokeEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MainRepository(
    private val favoriteJokeDao: FavoriteJokeDao,
    private val jokeApiService: JokeApiService
) {
    // Room functions
    val listOfJokes: LiveData<List<JokeEntity>> = favoriteJokeDao.getAllSavedJokes()
    suspend fun saveJoke(jokeEntity: JokeEntity): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            favoriteJokeDao.saveJoke(jokeEntity)
            return@withContext Result.success(Unit)
        } catch (e: Exception) {
            return@withContext Result.failure(e)
        }
    }

    suspend fun removeJoke(jokeEntity: JokeEntity): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            favoriteJokeDao.removeJoke(jokeEntity)
            return@withContext Result.success(Unit)
        } catch (e: Exception) {
            return@withContext Result.failure(e)
        }
    }

    suspend fun deleteAllJokes(): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            favoriteJokeDao.deleteAllJokes()
            return@withContext Result.success(Unit)
        } catch (e: Exception) {
            return@withContext Result.failure(e)
        }
    }

    suspend fun doesJokeExist(jokeId: Int): Result<Boolean> = withContext(Dispatchers.IO) {
        try {
            val count = favoriteJokeDao.doesJokeExist(jokeId)
            return@withContext Result.success(count > 0)
        } catch (e: Exception) {
            return@withContext Result.failure(e)
        }
    }

    // Retrofit Functions
    suspend fun getRandomJoke(): Result<Joke> = withContext(Dispatchers.IO) {
        try {
            val joke = jokeApiService.getRandomJoke()
            return@withContext Result.success(joke)
        } catch (e: Exception) {
            return@withContext Result.failure(e)
        }
    }

}