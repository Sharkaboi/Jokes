package com.cybershark.jokes.data.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "jokes")
data class JokeEntity(
    @PrimaryKey
    val jokeId: Int,
    val jokePunchline: String,
    val jokeSetup: String,
    val jokeType: String
)
