package com.inystudio.heureux.ui.main

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.inystudio.heureux.R
import com.inystudio.heureux.databinding.ActivityMainBinding
import com.inystudio.heureux.ui.Utils.showPermissionRequestExplanation

class MainActivity : AppCompatActivity() {

    private lateinit var toolbar        : MaterialToolbar
    private lateinit var navController  : NavController
    private lateinit var bottomNavView  : BottomNavigationView
    private lateinit var binding: ActivityMainBinding
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>




    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        toolbar = binding.toolbar
        bottomNavView = binding.bottomNavView
        val navHostFrag = supportFragmentManager.findFragmentById(R.id.nav_host_frag) as NavHostFragment
        navController = navHostFrag.navController

        val topLevelDestinations = setOf(R.id.homeFragment, R.id.chatFragment, R.id.accountFragment)
        val appBarConfiguration = AppBarConfiguration(topLevelDestinations)

        toolbar.setupWithNavController(navController, appBarConfiguration)
        bottomNavView.setupWithNavController(navController)


        navController.addOnDestinationChangedListener { _, destination, _ ->

            when (destination.id) {
                R.id.viewMyPurchasesFragment -> bottomNavView.visibility = View.GONE
                R.id.termsFragment -> bottomNavView.visibility = View.GONE
                R.id.reportIssueFragment -> bottomNavView.visibility = View.GONE
                R.id.settingsFragment -> bottomNavView.visibility = View.GONE
                R.id.profileSettingsFragment -> bottomNavView.visibility = View.GONE
                else -> bottomNavView.visibility = View.VISIBLE
            }
        }
        requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()){}

        toolbar.inflateMenu(R.menu.options)
        toolbar.menu.findItem(R.id.option_call_agent).setOnMenuItemClickListener {
            callAgent()
            true
        }
    }


    private fun callAgent() {
        when {
            ContextCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE)
                    == PackageManager.PERMISSION_GRANTED -> {
                val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "+254704176344"))
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



//    fun signOut() {
//        FirebaseAuth.getInstance().signOut()
//        startActivity(Intent(this, SignInActivity::class.java))
//        finish()
//    }
}