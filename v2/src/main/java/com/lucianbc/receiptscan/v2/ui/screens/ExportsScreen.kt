package com.lucianbc.receiptscan.v2.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.lucianbc.receiptscan.v2.ui.components.Screen
import com.lucianbc.receiptscan.v2.R


@Composable
fun ExportsScreen() {
    Screen(title = "Exports") {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                contentDescription = "",
                painter = painterResource(id = R.drawable.ic_add),
                colorFilter = ColorFilter.tint(Color(0xffC9C9C9)),
            )
        }
    }
}

@Composable
@Preview
fun ExportsScreenPreview() = ExportsScreen()