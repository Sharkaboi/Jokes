package com.cybershark.jokes.util

sealed class UIState {
    object Loading : UIState()
    object Idle : UIState()
    data class Error(val message: String) : UIState()
    data class Success(val message: String) : UIState()
}
