package com.worldwidemobiledevelopment.recipes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.worldwidemobiledevelopment.recipes.utils.setupWithNavController

class MainActivity : AppCompatActivity() {

    private var navController: LiveData<NavController>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            setupBottomNavigationBar()
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        setupBottomNavigationBar()
    }

    private fun setupBottomNavigationBar() {
        /* Setup bottom navigation and actionBar */
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_nav)

        // Set Navigation graphs
        val navGraphIds = listOf(R.navigation.home, R.navigation.categories, R.navigation.favourite, R.navigation.products, R.navigation.profile)

        // Setup the bottom navigation view with a list of navigation graphs
        val controller = bottomNavigationView.setupWithNavController(
                navGraphIds = navGraphIds,
                fragmentManager = supportFragmentManager,
                containerId = R.id.nav_host_container,
                intent = intent
        )

         // Whenever the selected controller changes, setup the action bar
        controller.observe(this, { navController ->
            setupActionBarWithNavController(navController)
        })

        navController = controller
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController?.value?.navigateUp() ?: false
    }
}