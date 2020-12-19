package com.cybershark.jokes.ui.settings

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.viewModels
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.cybershark.jokes.BuildConfig
import com.cybershark.jokes.R
import com.cybershark.jokes.data.AppConstants
import com.cybershark.jokes.data.SharedPreferencesKeys
import com.cybershark.jokes.util.*
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : PreferenceFragmentCompat() {

    private val settingsViewModel by viewModels<SettingsViewModel>()

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupData()
        setupListeners()
        setupObservers()
    }

    private fun setupObservers() {
        observe(settingsViewModel.uiState) { uiState ->
            when (uiState) {
                is UIState.Success -> {
                    view?.shortSnackBar(uiState.message)
                }
                is UIState.Error -> {
                    view?.shortSnackBar(uiState.message)
                }
                else -> Unit
            }
        }
    }

    private fun setupData() {
        val aboutInfo = findPreference<Preference>(SharedPreferencesKeys.ABOUT_INFO_KEY)
        val shareApp = findPreference<Preference>(SharedPreferencesKeys.SHARE_APP_KEY)
        aboutInfo?.summary = BuildConfig.VERSION_NAME
        aboutInfo?.setOnPreferenceClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("About")
                .setMessage("A Simple jokes app made to teach students in Android StudyJams")
                .setPositiveButton("Ok") { dialog, _ ->
                    dialog.dismiss()
                }
                .setNeutralButton("Github") { _, _ ->
                    openGithubPage()
                }
                .show()
            true
        }
        shareApp?.setOnPreferenceClickListener {
            shareApp()
            true
        }
    }


    private fun setupListeners() {
        val themeOption =
            findPreference<SwitchPreferenceCompat>(SharedPreferencesKeys.THEME_OPTION_KEY)
        themeOption?.setOnPreferenceChangeListener { _, newValue ->
            if (newValue as Boolean) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            true
        }
        findPreference<Preference>(SharedPreferencesKeys.DELETE_FAVORITES_KEY)?.setOnPreferenceClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Remove all favorites?")
                .setMessage("This is an irreversible action")
                .setPositiveButton("Confirm") { dialog, _ ->
                    settingsViewModel.deleteAllJokes()
                    dialog.dismiss()
                }
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
            true
        }
    }

    private fun openGithubPage() = openLink(AppConstants.GITHUB_LINK)

    private fun shareApp() =
        openShareChooser("Hey Check out this Great app: ${AppConstants.GITHUB_LINK}")
}