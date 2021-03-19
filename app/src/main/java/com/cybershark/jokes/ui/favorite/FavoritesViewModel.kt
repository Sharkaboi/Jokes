package com.cybershark.jokes.ui.favorite

import androidx.lifecycle.*
import com.cybershark.jokes.data.respositories.MainRepository
import com.cybershark.jokes.data.room.JokeEntity
import com.cybershark.jokes.util.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel
@Inject
constructor(
    private val savedStateHandle: SavedStateHandle,
    private val mainRepository: MainRepository
) : ViewModel() {

    val listOfFavoriteJokes: LiveData<List<JokeEntity>> = mainRepository.listOfJokes
    private val _uiState = MutableLiveData<UIState>().getDefault()
    val uiState: LiveData<UIState> = _uiState

    fun removeJoke(jokeEntity: JokeEntity) {
        _uiState.setLoading()
        viewModelScope.launch {
            val result = mainRepository.removeJoke(jokeEntity)
            if (result > 0) {
                _uiState.setSuccess("Removed from favorites")
            } else {
                _uiState.setError("An error occurred")
            }
        }
    }
}