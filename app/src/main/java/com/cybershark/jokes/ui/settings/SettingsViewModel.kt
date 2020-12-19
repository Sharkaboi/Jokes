package com.cybershark.jokes.ui.settings

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.cybershark.jokes.data.respositories.MainRepository
import com.cybershark.jokes.util.UIState
import com.cybershark.jokes.util.getDefault
import com.cybershark.jokes.util.setError
import com.cybershark.jokes.util.setSuccess
import kotlinx.coroutines.launch

class SettingsViewModel
@ViewModelInject
constructor(
    @Assisted savedStateHandle: SavedStateHandle,
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