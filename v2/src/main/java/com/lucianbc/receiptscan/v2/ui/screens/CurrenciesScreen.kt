package com.lucianbc.receiptscan.v2.ui.screens

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.lucianbc.receiptscan.v2.ui.components.NavigationBarParams
import com.lucianbc.receiptscan.v2.ui.components.Screen
import com.lucianbc.receiptscan.v2.ui.components.TitleBar
import java.util.*

@Composable
fun CurrenciesScreen(params: NavigationBarParams) {
    Screen {
        TitleBar(title = "Currencies", params = params, backEnabled = true)
        LazyColumn {
            itemsIndexed(Currency.getAvailableCurrencies().toList()) { _, it ->
                Text(it.displayName)
            }
        }
    }
}

@Preview
@Composable
fun CurrenciesScreenPreview() = CurrenciesScreen(params = NavigationBarParams.Empty)