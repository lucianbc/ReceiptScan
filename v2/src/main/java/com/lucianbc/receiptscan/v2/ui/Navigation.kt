package com.lucianbc.receiptscan.v2.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigate
import androidx.navigation.compose.rememberNavController
import com.lucianbc.receiptscan.v2.ui.screens.CategoriesScreen
import com.lucianbc.receiptscan.v2.ui.screens.HomeScreen
import com.lucianbc.receiptscan.v2.ui.screens.HomeScreenParams

@ExperimentalFoundationApi
@Composable
fun Navigation() {
    val controller = rememberNavController()
    val params = remember { createParams(controller) }

    NavHost(navController = controller, startDestination = "home") {
        composable("home") {
            HomeScreen(params)
        }
        composable("categories") {
            CategoriesScreen()
        }
    }
}

fun createParams(controller: NavController): HomeScreenParams {
    return object : HomeScreenParams {
        override fun goToCategories() {
            controller.navigate("categories")
        }
    }
}