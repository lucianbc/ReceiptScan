package com.lucianbc.receiptscan

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val navController by lazy {
        Navigation.findNavController(this, R.id.nav_host_fragment)
    }
    private val appBarConfig = AppBarConfiguration.Builder(setOf(
        R.id.destination_transactions
    )).build()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupBottomNavMenu()
//        setupActionBar()
    }

    private fun setupBottomNavMenu() {
        bottom_navigation?.let {
            NavigationUI.setupWithNavController(it, navController)
        }
    }

    private fun setupActionBar() {
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfig)
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, null)
    }
}
