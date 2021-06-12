package com.lucianbc.receiptscan.v2.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
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
            BackButton(onClick = params::goBack)
        }
        Spacer(modifier = Modifier.width(24.dp))
        Text(text = title, style = MaterialTheme.typography.h6)
    }
}