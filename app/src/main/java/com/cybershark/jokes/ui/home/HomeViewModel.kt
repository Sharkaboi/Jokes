package com.cybershark.jokes.ui.home

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.cybershark.jokes.data.models.Joke
import com.cybershark.jokes.data.respositories.MainRepository
import com.cybershark.jokes.util.*
import kotlinx.coroutines.launch

class HomeViewModel
@ViewModelInject
constructor(
    @Assisted savedStateHandle: SavedStateHandle,
    private val mainRepository: MainRepository
) : ViewModel() {

    private val _uiState: MutableLiveData<UIState> = MutableLiveData<UIState>().getDefault()
    val uiState: LiveData<UIState> = _uiState
    private val _currentJoke: MutableLiveData<JokeState> = MutableLiveData()
    val currentJoke: LiveData<JokeState> = _currentJoke

    fun getRandomJoke() {
        _uiState.setLoading()
        viewModelScope.launch {
            try {
                val response = mainRepository.getRandomJoke()
                if (response.isSuccessful) {
                    val jokeBody: Joke? = response.body()
                    jokeBody?.let { joke ->
                        val count = mainRepository.doesJokeExist(joke.id)
                        if (count == 0) {
                            // not favorite
                            _currentJoke.value = JokeState(
                                isJokeFavorite = false,
                                joke = joke
                            )
                        } else {
                            _currentJoke.value = JokeState(
                                isJokeFavorite = true,
                                joke = joke
                            )
                        }
                    }
                    _uiState.setSuccess("Joke fetched!")
                } else {
                    _uiState.setError("An Error occurred!")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _uiState.setError(e.message.getErrorString())
            }
        }
    }

    fun saveJoke(joke: Joke) {
        _uiState.setLoading()
        viewModelScope.launch {
            val result: Long = mainRepository.saveJoke(joke.getJokeEntity())
            if (result > 0L) {
                _uiState.setSuccess("Saved joke to favorites")
                _currentJoke.value = _currentJoke.value?.copy(isJokeFavorite = true)
            } else {
                _uiState.setError("An Error occurred!")
            }
        }
    }

    fun removeJoke(joke: Joke) {
        _uiState.setLoading()
        viewModelScope.launch {
            val result: Int = mainRepository.removeJoke(joke.getJokeEntity())
            if (result > 0) {
                _uiState.setSuccess("Removed joke from favorites")
                _currentJoke.value = _currentJoke.value?.copy(isJokeFavorite = false)
            } else {
                _uiState.setError("An Error occurred!")
            }
        }
    }
}

data class JokeState(
    val joke: Joke,
    val isJokeFavorite: Boolean
)