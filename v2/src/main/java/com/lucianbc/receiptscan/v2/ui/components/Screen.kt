package com.lucianbc.receiptscan.v2.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.lucianbc.receiptscan.v2.ui.ComposableSampleTheme

@Composable
fun Screen(content: @Composable ColumnScope.() -> Unit) {
    ComposableSampleTheme {
        Column(modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()) {
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
