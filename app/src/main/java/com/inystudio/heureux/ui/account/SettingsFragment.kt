package com.inystudio.heureux.ui.account

import android.os.Bundle
import androidx.navigation.fragment.findNavController
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.inystudio.heureux.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)

        val profileSettings = findPreference<Preference>("KEY_PROFILE")
        profileSettings?.setOnPreferenceClickListener {
            findNavController().navigate(R.id.action_settingsFragment_to_profileSettingsFragment)
            true
        }

        val permissionScreen = findPreference<Preference>("KEY_TERMS")
        permissionScreen?.setOnPreferenceClickListener {
            findNavController().navigate(R.id.action_settingsFragment_to_termsFragment)
            true
        }
    }
}