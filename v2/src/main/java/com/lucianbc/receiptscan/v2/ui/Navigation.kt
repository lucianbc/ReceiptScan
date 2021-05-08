package com.lucianbc.receiptscan.v2.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigate
import androidx.navigation.compose.rememberNavController
import com.lucianbc.receiptscan.v2.ui.components.NavigationBarParams
import com.lucianbc.receiptscan.v2.ui.screens.*

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
            CategoriesScreen(params)
        }
        composable("currencies") {
            CurrenciesScreen(params)
        }
        composable("transaction") {
            ReceiptScreen()
        }
    }
}

private val createParams = { controller: NavController ->
    object : HomeScreenParams, NavigationBarParams {
        override fun goToCategories() =
            controller.navigate("categories")

        override fun goToCurrencies() =
            controller.navigate("currencies")

        override fun goToTransaction() =
            controller.navigate("transaction")

        override fun goBack() {
            controller.navigateUp()
        }
    }
}
