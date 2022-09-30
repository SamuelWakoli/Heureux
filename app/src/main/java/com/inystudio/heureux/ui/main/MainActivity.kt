package com.inystudio.heureux.ui.main

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.preference.PreferenceManager
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.inystudio.heureux.R
import com.inystudio.heureux.databinding.ActivityMainBinding
import com.inystudio.heureux.ui.Utils.showPermissionRequestExplanation
import com.inystudio.heureux.ui.start.SignInActivity
import java.util.concurrent.Executor

class MainActivity : AppCompatActivity() {

    private var isBioLockOn: Boolean = false
    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    private lateinit var toolbar: MaterialToolbar
    private lateinit var navController: NavController
    private lateinit var bottomNavView: BottomNavigationView
    private lateinit var binding: ActivityMainBinding
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Auth on create
        biometricAuth()

        toolbar = binding.toolbar
        bottomNavView = binding.bottomNavView
        val navHostFrag =
            supportFragmentManager.findFragmentById(R.id.nav_host_frag) as NavHostFragment
        navController = navHostFrag.navController

        val topLevelDestinations = setOf(R.id.homeFragment, R.id.accountFragment)
        val appBarConfiguration = AppBarConfiguration(topLevelDestinations)

        toolbar.setupWithNavController(navController, appBarConfiguration)
        bottomNavView.setupWithNavController(navController)


        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.homeFragment -> bottomNavView.visibility = View.VISIBLE
                R.id.chatFragment -> bottomNavView.visibility = View.VISIBLE
                R.id.accountFragment -> bottomNavView.visibility = View.VISIBLE
                else -> bottomNavView.visibility = View.GONE
            }
        }

        requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) {}

        toolbar.inflateMenu(R.menu.options)
        toolbar.menu.findItem(R.id.option_call_agent).setOnMenuItemClickListener {
            callAgent()
            true
        }
        toolbar.menu.findItem(R.id.option_whatsapp).setOnMenuItemClickListener {
            //var myPackage1: String = "com.whatsapp"
            //var myPackage2: String = "com.w4b"
            var myPhone = "+254797228948"
            //var myUrl = "https://api.whatsapp.com/send?phone=" + myPhone
            var myUrl = "https://wa.me/" + myPhone
            var intent: Intent = Intent(Intent.ACTION_VIEW)
            //try {
            intent.setData(Uri.parse(myUrl))
            startActivity(intent)
//            } catch (e: PackageManager.NameNotFoundException) {
//                Toast.makeText(this, "")
//            }

            true
        }
        toolbar.menu.findItem(R.id.option_about_us).setOnMenuItemClickListener {
            navController.navigate(R.id.aboutUsFragment)
            true
        }
        toolbar.menu.findItem(R.id.option_make_payment).setOnMenuItemClickListener {
            navController.navigate(R.id.makePaymentFragment)
            true
        }
        toolbar.menu.findItem(R.id.option_settings).setOnMenuItemClickListener {
            navController.navigate(R.id.settingsFragment)
            true
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.aboutUsFragment -> toolbar.menu.setGroupVisible(R.id.app_options_menu, false)
                R.id.makePaymentFragment -> toolbar.menu.setGroupVisible(
                    R.id.app_options_menu,
                    false
                )
                R.id.settingsFragment -> toolbar.menu.setGroupVisible(R.id.app_options_menu, false)
                else -> toolbar.menu.setGroupVisible(R.id.app_options_menu, true)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if (Firebase.auth.currentUser == null) {
            startActivity(Intent(this, SignInActivity::class.java))
            finish()
        }
    }

    private fun biometricAuth() {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        isBioLockOn = sharedPreferences.getBoolean("KEY_SECURITY_BIOMETRICS", false)

        if (isBioLockOn) {
            executor = ContextCompat.getMainExecutor(this)
            biometricPrompt = BiometricPrompt(
                this@MainActivity,
                executor,
                object : BiometricPrompt.AuthenticationCallback() {
                    override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                        super.onAuthenticationError(errorCode, errString)

                        if (errorCode == BiometricPrompt.ERROR_HW_NOT_PRESENT
                            && errorCode == BiometricPrompt.ERROR_HW_UNAVAILABLE
                            && errorCode == BiometricPrompt.ERROR_VENDOR
                        ) {
                            Toast.makeText(
                                this@MainActivity,
                                "$errString Disable biometrics.",
                                Toast.LENGTH_LONG
                            ).show()
                            isBioLockOn = false
                            navController.navigate(R.id.settingsFragment)
                        }
                        if (errorCode == BiometricPrompt.ERROR_NEGATIVE_BUTTON) {
                            finish()
                        }
                        if (errorCode == BiometricPrompt.ERROR_LOCKOUT) {
                            Toast.makeText(
                                this@MainActivity,
                                "Please try again after 30 seconds.",
                                Toast.LENGTH_LONG
                            ).show()
                            Thread.sleep(3000)
                            finish()
                        }
                        if (errorCode == BiometricPrompt.ERROR_TIMEOUT) {
                            Toast.makeText(
                                this@MainActivity,
                                "$errString Please try again later.",
                                Toast.LENGTH_LONG
                            ).show()
                            Thread.sleep(3000)
                            finish()
                        }
                        if (errorCode == BiometricPrompt.ERROR_NO_BIOMETRICS) {
                            Toast.makeText(
                                this@MainActivity,
                                "$errString Disable biometrics.",
                                Toast.LENGTH_LONG
                            ).show()
                            isBioLockOn = false
                            navController.navigate(R.id.settingsFragment)
                        }
                        if (errorCode == BiometricPrompt.ERROR_USER_CANCELED) {
                            Thread.sleep(3000)
                            finish()
                        }
                        if (errorCode == BiometricPrompt.ERROR_CANCELED) {
                            Toast.makeText(
                                this@MainActivity,
                                errString,
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }

                    override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                        super.onAuthenticationSucceeded(result)
                        Toast.makeText(this@MainActivity, "Welcome back!", Toast.LENGTH_SHORT)
                            .show()
                    }

                    override fun onAuthenticationFailed() {
                        super.onAuthenticationFailed()
                        Toast.makeText(this@MainActivity, "Please try again", Toast.LENGTH_LONG)
                            .show()
                        Thread.sleep(3000)
                        finish()
                    }
                })
            promptInfo = BiometricPrompt.PromptInfo.Builder()
                .setTitle("Heureux Properties")
                .setSubtitle("Authenticate using fingerprint to continue")
                .setNegativeButtonText("Cancel")
                .build()


            //start auth
            biometricPrompt.authenticate(promptInfo)
        }


    }

    private fun callAgent() {
        when {
            ContextCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE)
                    == PackageManager.PERMISSION_GRANTED -> {
                val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "+254797228948"))
                startActivity(intent)
            }

            ContextCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE)
                    == PackageManager.PERMISSION_DENIED -> {
                showPermissionRequestExplanation(
                    getString(R.string.call_permission_head),
                    getString(R.string.permission_request)
                ) { requestPermissionLauncher.launch(android.Manifest.permission.CALL_PHONE) }
            }

        }
    }
}