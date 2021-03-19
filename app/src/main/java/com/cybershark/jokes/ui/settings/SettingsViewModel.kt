package com.cybershark.jokes.ui.settings

import androidx.lifecycle.*
import com.cybershark.jokes.data.respositories.MainRepository
import com.cybershark.jokes.util.UIState
import com.cybershark.jokes.util.getDefault
import com.cybershark.jokes.util.setError
import com.cybershark.jokes.util.setSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel
@Inject
constructor(
    private val savedStateHandle: SavedStateHandle,
    private val mainRepository: MainRepository
) : ViewModel() {

    private val _uiState = MutableLiveData<UIState>().getDefault()
    val uiState: LiveData<UIState> = _uiState

    fun deleteAllJokes() {
        viewModelScope.launch {
            val result = mainRepository.deleteAllJokes()
            if (result > 0) {
                _uiState.setSuccess("Deleted favorites")
            } else {
                _uiState.setError("Error occurred")
            }
        }
    }
}