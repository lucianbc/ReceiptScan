package com.lucianbc.receiptscan.v2.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

interface NavigationBarParams {
    fun goBack()

    object Empty : NavigationBarParams {
        override fun goBack() {}
    }
}

@Composable
fun TitleBar(
    title: String,
    backEnabled: Boolean = false,
    params: NavigationBarParams,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (backEnabled) {
            Icon(
                Icons.Default.ArrowBack,
                "back",
                modifier = Modifier
                    .padding(end = 24.dp)
                    .clickable(onClick = params::goBack)
            )
        }
        Text(text = title, style = MaterialTheme.typography.h6)
    }
}