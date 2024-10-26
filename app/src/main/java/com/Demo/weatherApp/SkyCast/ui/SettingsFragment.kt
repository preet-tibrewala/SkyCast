package com.Demo.weatherApp.SkyCast.ui

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.Demo.weatherApp.SkyCast.R

/**
 * This fragment represents a settings screen for the app.
 */
class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings, rootKey)
    }
}