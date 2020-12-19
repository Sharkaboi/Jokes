package com.cybershark.jokes.data.respositories

import androidx.lifecycle.LiveData
import com.cybershark.jokes.data.api.JokeApiService
import com.cybershark.jokes.data.models.Joke
import com.cybershark.jokes.data.room.FavoriteJokeDao
import com.cybershark.jokes.data.room.JokeEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class MainRepository(
    private val favoriteJokeDao: FavoriteJokeDao,
    private val jokeApiService: JokeApiService
) {
    // Room functions
    val listOfJokes: LiveData<List<JokeEntity>> = favoriteJokeDao.getAllSavedJokes()
    suspend fun saveJoke(jokeEntity: JokeEntity): Long = withContext(Dispatchers.IO) {
        return@withContext favoriteJokeDao.saveJoke(jokeEntity)
    }

    suspend fun removeJoke(jokeEntity: JokeEntity): Int = withContext(Dispatchers.IO) {
        return@withContext favoriteJokeDao.removeJoke(jokeEntity)
    }

    suspend fun deleteAllJokes(): Int = withContext(Dispatchers.IO) {
        return@withContext favoriteJokeDao.deleteAllJokes()
    }

    suspend fun doesJokeExist(jokeId: Int): Int = withContext(Dispatchers.IO) {
        return@withContext favoriteJokeDao.doesJokeExist(jokeId)
    }

    // Retrofit Functions
    suspend fun getRandomJoke(): Response<Joke> = withContext(Dispatchers.IO) {
        return@withContext jokeApiService.getRandomJoke()
    }

}