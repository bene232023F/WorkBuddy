package edu.vt.mobiledev.workbuddy

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

// MainActivity hosts the NavHostFragment and wires up the bottom navigation bar.
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inflate the layout containing the NavHostFragment and BottomNavigationView
        setContentView(R.layout.activity_main)

        // Retrieve the NavHostFragment that contains our navigation graph
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        // Obtain the NavController for navigating between fragments
        val navController = navHostFragment.navController

        // Find the BottomNavigationView in the layout
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)
        // Connect the BottomNavigationView with the NavController so tapping items navigates
        bottomNav.setupWithNavController(navController)
    }
}