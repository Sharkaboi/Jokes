package com.cybershark.jokes.ui

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import com.cybershark.jokes.data.SharedPreferencesKeys
import com.cybershark.jokes.util.launchAndFinishAffinity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme()
        navigateToMainActivity()
    }

    private fun setTheme() {
        val isDarkTheme =
            sharedPreferences.getBoolean(SharedPreferencesKeys.THEME_OPTION_KEY, false)

        setDefaultNightMode(if (isDarkTheme) MODE_NIGHT_YES else MODE_NIGHT_NO)
    }

    private fun navigateToMainActivity() = launchAndFinishAffinity<MainActivity>()
}