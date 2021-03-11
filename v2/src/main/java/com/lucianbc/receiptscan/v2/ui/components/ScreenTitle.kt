package com.lucianbc.receiptscan.v2.ui.components

import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.ui.tooling.preview.Preview

@Composable
fun ScreenTitle(title: String) {
    Row(
        modifier = Modifier.then(Modifier.fillMaxWidth())
            .then(Modifier.padding(top = 64.dp, bottom = 16.dp)),
        horizontalArrangement = Arrangement.Center,
    ) {
        Text(
            fontSize = 36.sp,
            fontFamily = FontFamily.SansSerif,
            text = title,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ScreenTitlePreview() {
    ScreenTitle(title = "Title here")
}
