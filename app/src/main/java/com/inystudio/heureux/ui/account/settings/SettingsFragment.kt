package com.inystudio.heureux.ui.account.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.inystudio.heureux.R
import com.inystudio.heureux.ui.main.MainActivity
import com.inystudio.heureux.ui.start.SignInActivity

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)

        val profileSettings = findPreference<Preference>("KEY_PROFILE")
        profileSettings?.setOnPreferenceClickListener {
            findNavController().navigate(R.id.action_settingsFragment_to_profileSettingsFragment)
            true
        }

        val termsScreen = findPreference<Preference>("KEY_TERMS")
        termsScreen?.setOnPreferenceClickListener {
            findNavController().navigate(R.id.action_settingsFragment_to_termsFragment)
            true
        }

        val aboutScreen = findPreference<Preference>("KEY_ABOUT_US")
        aboutScreen?.setOnPreferenceClickListener {
            findNavController().navigate(R.id.action_settingsFragment_to_aboutUsFragment)
            true
        }


        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())

        val biometricsOption = findPreference<Preference>("KEY_SECURITY_BIOMETRICS")
        biometricsOption?.setOnPreferenceClickListener {
            val biometricsStatus = sharedPreferences.getBoolean("KEY_SECURITY_BIOMETRICS", false)
            val myBioString: String = if (biometricsStatus) { "Enabled" } else { "Disabled" }
            Toast.makeText(requireContext(), "App fingerprint lock: $myBioString", Toast.LENGTH_SHORT ).show()
            true
        }

        val chatNotifications = findPreference<Preference>("KEY_CHAT_NOTIFICATIONS")
        chatNotifications?.setOnPreferenceClickListener {
            val chatNotificationStatus = sharedPreferences.getBoolean("KEY_CHAT_NOTIFICATIONS", true)
            val myChatString: String = if (chatNotificationStatus) { "Enabled" } else { "Disabled" }
            Toast.makeText(requireContext(), "Chat notifications: $myChatString", Toast.LENGTH_SHORT ).show()
            true
        }


        val openGoogleMaps = findPreference<Preference>("KEY_OPEN_MAP")
        openGoogleMaps?.setOnPreferenceClickListener {
            val gmmIntentUri = Uri.parse("geo:0,0?q=Jetro Chambers Westlands")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            startActivity(mapIntent)
            true
        }

        val signOutBtn = findPreference<Preference>("SIGN_OUT")
        signOutBtn?.setOnPreferenceClickListener {
            FirebaseAuth.getInstance().signOut()
            Toast.makeText(requireContext(), "You signed out successfully", Toast.LENGTH_LONG).show()
            true
        }
    }
}