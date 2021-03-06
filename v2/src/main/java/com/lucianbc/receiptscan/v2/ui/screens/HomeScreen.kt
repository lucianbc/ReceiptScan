package com.lucianbc.receiptscan.v2.ui.screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.tooling.preview.Preview
import com.lucianbc.receiptscan.v2.ui.components.BottomNavigationView
import com.lucianbc.receiptscan.v2.ui.viewModels.SettingsViewModel

interface HomeScreenParams : SettingsScreenParams, TransactionsScreenParams {
    companion object Empty : HomeScreenParams by Empty
}

@Composable
fun HomeScreen(params: HomeScreenParams, viewModel: SettingsViewModel) {
    BottomNavigationView {
        item("Home", rememberVectorPainter(image = Icons.Default.Home)) {
            TransactionsScreen(params)
        }
        item("Drafts", rememberVectorPainter(image = Icons.Default.Email)) {
            DraftsScreen()
        }
        item("Settings", rememberVectorPainter(image = Icons.Default.Settings)) {
            SettingsScreen(params, viewModel)
        }
        item("Exports", rememberVectorPainter(image = Icons.Default.KeyboardArrowUp)) {
            ExportsScreen()
        }
    }
}


@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() = HomeScreen(HomeScreenParams, SettingsViewModel.Empty)