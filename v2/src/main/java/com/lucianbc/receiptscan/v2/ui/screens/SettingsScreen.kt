package com.lucianbc.receiptscan.v2.ui.screens

import androidx.compose.foundation.Text
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Switch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.ui.tooling.preview.Preview
import com.lucianbc.receiptscan.v2.ui.components.Screen

@Composable
fun SettingsScreen() {
    var enabled by remember { mutableStateOf(false) }
    Screen(title = "Settings") {
        SettingRow(key = "Default Currency", value = "RON") {}
        SettingRow(key = "Default Category", value = "Grocery") {}
        SettingRow(key = "Send Receipt Anonymously") {
            Switch(checked = enabled, onCheckedChange = { enabled = !enabled })
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
    SettingsScreen()
}
