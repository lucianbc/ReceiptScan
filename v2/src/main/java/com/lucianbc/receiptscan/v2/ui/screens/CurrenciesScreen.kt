package com.lucianbc.receiptscan.v2.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
                CurrencyListItem(currency = it) {}

            }
        }
    }
}

@Composable
fun CurrencyListItem(currency: Currency, onClick: OnClick) {
    val biggerPadding = 16.dp
    val smallerPadding = 8.dp
    Column(modifier = Modifier
        .fillMaxWidth()
        .clickable { onClick?.invoke() }
        .padding(
            top = biggerPadding,
            start = biggerPadding,
            end = biggerPadding,
            bottom = smallerPadding
        )

    ) {
        Text(text = currency.currencyCode, style = MaterialTheme.typography.h6)
        Text(text = currency.displayName, style = MaterialTheme.typography.body2)
    }
}

@Preview
@Composable
fun CurrenciesScreenPreview() = CurrenciesScreen(params = NavigationBarParams.Empty)