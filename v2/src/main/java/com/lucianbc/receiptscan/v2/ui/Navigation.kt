package com.lucianbc.receiptscan.v2.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.lucianbc.receiptscan.v2.ui.screens.HomeScreen

@Composable
fun Navigation() {
    val controller = rememberNavController()
    NavHost(navController = controller, startDestination = "home") {
        composable("home") {
            HomeScreen()
        }
    }
}