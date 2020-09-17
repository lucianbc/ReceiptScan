package com.lucianbc.receiptscan

import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        rectToNavChange(findNavController(R.id.root_navigator))
    }

    private fun rectToNavChange(navController: NavController) {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            manageStatusPar(destination)
        }
    }

    private fun manageStatusPar(destination: NavDestination) {
        if (destination.id == R.id.scanner)
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        else
            window.clearFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )

    }
}
