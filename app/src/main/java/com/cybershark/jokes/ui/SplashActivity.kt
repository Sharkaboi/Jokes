package com.cybershark.jokes.ui

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate.*
import androidx.lifecycle.lifecycleScope
import com.cybershark.jokes.data.SharedPreferencesKeys
import com.cybershark.jokes.util.launchAndFinishAffinity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            setTheme()
        }
    }

    private suspend fun setTheme() = withContext(Dispatchers.Main) {
        val isDarkThemeAsync = async(Dispatchers.IO) {
            sharedPreferences.getBoolean(SharedPreferencesKeys.THEME_OPTION_KEY, false)
        }
        val isDarkTheme = isDarkThemeAsync.await()
        setDefaultNightMode(if (isDarkTheme) MODE_NIGHT_YES else MODE_NIGHT_NO)
        navigateToMainActivity()
    }

    private fun navigateToMainActivity() = launchAndFinishAffinity<MainActivity>()
}