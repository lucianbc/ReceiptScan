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

interface HomeScreenParams : SettingsScreenParams {
    companion object Empty : HomeScreenParams by Empty
}

@Composable
fun HomeScreen(params: HomeScreenParams) {
    BottomNavigationView {
        item("Home", rememberVectorPainter(image = Icons.Default.Home)) {
            TransactionsScreen()
        }
        item("Drafts", rememberVectorPainter(image = Icons.Default.Email)) {
            DraftsScreen()
        }
        item("Settings", rememberVectorPainter(image = Icons.Default.Settings)) {
            SettingsScreen(params)
        }
        item("Exports", rememberVectorPainter(image = Icons.Default.KeyboardArrowUp)) {
            ExportsScreen()
        }
    }
}


@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() = HomeScreen(HomeScreenParams)