package com.cybershark.jokes.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    private val mainRepository: MainRepository
) : ViewModel() {

    val listOfFavoriteJokes: LiveData<List<JokeEntity>> = mainRepository.listOfJokes
    private val _uiState = MutableLiveData<UIState>().getDefault()
    val uiState: LiveData<UIState> = _uiState

    fun removeJoke(jokeEntity: JokeEntity) {
        _uiState.setLoading()
        viewModelScope.launch {
            val result = mainRepository.removeJoke(jokeEntity)
            if (result.isSuccess) {
                _uiState.setSuccess("Removed from favorites")
            } else {
                _uiState.setError(result.exceptionOrNull())
            }
        }
    }
}