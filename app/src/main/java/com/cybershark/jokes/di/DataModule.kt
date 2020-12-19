package com.cybershark.jokes.di

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import androidx.room.Room
import com.cybershark.jokes.data.api.ApiConstants
import com.cybershark.jokes.data.api.JokeApiService
import com.cybershark.jokes.data.respositories.MainRepository
import com.cybershark.jokes.data.room.FavoriteJokeDB
import com.cybershark.jokes.data.room.FavoriteJokeDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideRepository(
        favoriteJokeDao: FavoriteJokeDao,
        jokeApiService: JokeApiService
    ): MainRepository = MainRepository(favoriteJokeDao, jokeApiService)

    @Provides
    @Singleton
    fun provideRoomDb(
        @ApplicationContext context: Context
    ): FavoriteJokeDB = Room.databaseBuilder(
        context.applicationContext,
        FavoriteJokeDB::class.java,
        "jokes_db"
    ).build()

    @Provides
    @Singleton
    fun provideRoomDao(favoriteJokeDB: FavoriteJokeDB): FavoriteJokeDao =
        favoriteJokeDB.favoriteJokeDao()

    @Singleton
    @Provides
    fun getSharedPrefs(@ApplicationContext context: Context): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(context)
    }

    @Provides
    @Singleton
    fun getRetrofit(@ApplicationContext context: Context): JokeApiService {
        return Retrofit.Builder()
            .baseUrl(ApiConstants.apiEndpoint)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(JokeApiService::class.java)
    }

}