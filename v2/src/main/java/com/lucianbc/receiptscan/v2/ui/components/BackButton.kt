package com.lucianbc.receiptscan.v2.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

private val standaloneMargins = Modifier.padding(top = 16.dp, start = 16.dp)

@Composable
fun BackButton(standalone: Boolean = false, onClick: () -> Unit) {
    Icon(
        Icons.Default.ArrowBack,
        "back",
        modifier = Modifier
            .then(
                if (standalone)
                    standaloneMargins
                else Modifier
            )
            .clickable(onClick = {
                onClick()
//                println("Doing this here")
            })
    )
}