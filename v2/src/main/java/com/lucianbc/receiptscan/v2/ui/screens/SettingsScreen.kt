package com.lucianbc.receiptscan.v2.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucianbc.receiptscan.v2.ui.components.Screen
import com.lucianbc.receiptscan.v2.ui.viewModels.SettingsViewModel


interface SettingsScreenParams {
    fun goToCategories()
    fun goToCurrencies()

    object Empty : SettingsScreenParams {
        override fun goToCategories() {}
        override fun goToCurrencies() {}
    }
}

@Composable
fun SettingsScreen(params: SettingsScreenParams, viewModel: SettingsViewModel) {
    val viewState by viewModel.settingsState.collectAsState()
    
    Screen(title = "Settings") {
        SettingRow(
            key = "Default Currency",
            value = viewState.defaultCurrency.currencyCode,
            params::goToCurrencies,
        )
        SettingRow(
            key = "Default Category",
            value = viewState.defaultCategory.name,
            params::goToCategories,
        )
        SettingRow(key = "Send Receipt Anonymously") {
            Switch(checked = viewState.shareAnonymousData,
                onCheckedChange = { viewModel.toggleSendReceiptAnonymously() })
        }
    }
}

typealias OnClick = (() -> Unit)?

@Composable
fun SettingRow(key: String, onClick: OnClick = null, value: @Composable () -> Unit) {
    Row(
        modifier = Modifier
            .let { if (onClick == null) it else it.then(Modifier.clickable(onClick = onClick)) }
            .then(Modifier.fillMaxWidth())
            .then(Modifier.padding(24.dp)),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(text = key, fontSize = 20.sp)
        value()
    }
}

@Composable
fun SettingRow(key: String, value: String, onClick: OnClick = null) {
    SettingRow(key = key, onClick = onClick) {
        Text(text = value, fontSize = 20.sp, color = Color(0xFFC9C9C9))
    }
}

@Composable
@Preview
fun SettingsScreenPreview() {
    SettingsScreen(SettingsScreenParams.Empty, SettingsViewModel.Empty)
}
