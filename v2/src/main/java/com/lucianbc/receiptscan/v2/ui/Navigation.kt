package com.lucianbc.receiptscan.v2.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.lucianbc.receiptscan.v2.ui.components.NavigationBarParams
import com.lucianbc.receiptscan.v2.ui.screens.*
import com.lucianbc.receiptscan.v2.ui.viewModels.DraftViewModelImpl
import com.lucianbc.receiptscan.v2.ui.viewModels.HomeViewModelImpl

@ExperimentalFoundationApi
@Composable
fun Navigation() {
    val controller = rememberNavController()
    val params = remember { createParams(controller) }

    NavHost(navController = controller, startDestination = "home") {
        composable("home") {
            val vm = it.parentViewModel<HomeViewModelImpl>(controller = controller)
            HomeScreen(params, vm)
        }
        composable("categories") {
            val vm = it.parentViewModel<HomeViewModelImpl>(controller = controller)
            CategoriesScreen(params, vm)
        }
        composable("currencies") {
            val vm = it.parentViewModel<HomeViewModelImpl>(controller = controller)
            CurrenciesScreen(params, vm)
        }
        composable("transaction") {
            ReceiptScreen(params)
        }

        navigation("draft-start", "draft") {
            val navParams = object : EditableReceiptParams, NavigationBarParams {
                override fun goToCategories() {
                    controller.navigate("draft-categories")
                }

                override fun goToCurrencies() {
                    controller.navigate("draft-currencies")
                }

                override fun goBack() {
                    controller.navigateUp()
                }
            }

            composable("draft-start") {
                val vm = it.parentViewModel<DraftViewModelImpl>(controller = controller)
                EditableReceiptScreen(navParams, vm)
            }

            composable("draft-categories") {
                val vm = it.parentViewModel<DraftViewModelImpl>(controller = controller)
                CategoriesScreen(params = navParams, viewModel = vm)
            }

            composable("draft-currencies") {
                val vm = it.parentViewModel<DraftViewModelImpl>(controller = controller)
                CurrenciesScreen(params = navParams, viewModel = vm)
            }
        }

    }
}

@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.parentViewModel(controller: NavController): T {
    val parentEntry = checkNotNull(destination.parent?.id?.let(controller::getBackStackEntry)) {
        "Cannot build parent view model because no backstack entry was found for the parent nav graph"
    }
    return ViewModelProvider(parentEntry).get()
}

private val createParams = { controller: NavController ->
    object : HomeScreenParams, NavigationBarParams {
        override fun goToCategories() =
            controller.navigate("categories")

        override fun goToCurrencies() =
            controller.navigate("currencies")

        override fun goToTransaction() =
            controller.navigate("transaction")

        override fun goToDraft() {
            controller.navigate("draft")
        }

        override fun goBack() {
            controller.navigateUp()
        }
    }
}
