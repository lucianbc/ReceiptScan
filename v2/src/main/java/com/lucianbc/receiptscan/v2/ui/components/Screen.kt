package com.lucianbc.receiptscan.v2.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lucianbc.receiptscan.v2.ui.ComposableSampleTheme

@Composable
fun Screen(content: @Composable () -> Unit) {
    ComposableSampleTheme {
        Column(modifier = Modifier.fillMaxHeight().fillMaxWidth()) {
            content()
        }
    }
}

@Composable
fun Screen(title: String, icons: List<IconAction> = emptyList(), content: @Composable () -> Unit) {
    Screen {
        ScreenTitle(title = title, icons)
        content()
    }
}
