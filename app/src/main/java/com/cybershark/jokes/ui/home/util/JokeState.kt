package com.cybershark.jokes.ui.home.util

import com.cybershark.jokes.data.models.Joke

data class JokeState(
    val joke: Joke,
    val isJokeFavorite: Boolean
)
