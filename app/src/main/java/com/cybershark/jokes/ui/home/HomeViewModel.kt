package com.cybershark.jokes.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cybershark.jokes.data.models.Joke
import com.cybershark.jokes.data.respositories.MainRepository
import com.cybershark.jokes.ui.home.util.JokeState
import com.cybershark.jokes.util.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
@Inject
constructor(
    private val mainRepository: MainRepository
) : ViewModel() {

    private val _uiState: MutableLiveData<UIState> = MutableLiveData<UIState>().getDefault()
    val uiState: LiveData<UIState> = _uiState
    private val _currentJoke: MutableLiveData<JokeState> = MutableLiveData()
    val currentJoke: LiveData<JokeState> = _currentJoke

    fun getRandomJoke() {
        _uiState.setLoading()
        viewModelScope.launch {
            val response = mainRepository.getRandomJoke()
            if (response.isSuccess) {
                val jokeBody: Joke? = response.getOrNull()
                jokeBody?.let { joke ->
                    val result = mainRepository.doesJokeExist(joke.id)
                    _currentJoke.value = JokeState(
                        isJokeFavorite = result.getOrElse { false },
                        joke = joke
                    )
                }
                _uiState.setSuccess("Joke fetched!")
            } else {
                _uiState.setError(response.exceptionOrNull())
            }
        }
    }

    fun saveJoke(joke: Joke) {
        _uiState.setLoading()
        viewModelScope.launch {
            val result = mainRepository.saveJoke(joke.getJokeEntity())
            if (result.isSuccess) {
                _uiState.setSuccess("Saved joke to favorites")
                _currentJoke.value = _currentJoke.value?.copy(isJokeFavorite = true)
            } else {
                _uiState.setError(result.exceptionOrNull())
            }
        }
    }

    fun removeJoke(joke: Joke) {
        _uiState.setLoading()
        viewModelScope.launch {
            val result = mainRepository.removeJoke(joke.getJokeEntity())
            if (result.isSuccess) {
                _uiState.setSuccess("Removed joke from favorites")
                _currentJoke.value = _currentJoke.value?.copy(isJokeFavorite = false)
            } else {
                _uiState.setError(result.exceptionOrNull())
            }
        }
    }
}

