package com.lucianbc.receiptscan.v2.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.lucianbc.receiptscan.v2.ui.screens.OnClick

@Composable
fun ExpenseListItem(name: String, modifier: Modifier = Modifier, onClick: OnClick) {
    Row(
        modifier = Modifier
            .then(modifier)
            .clickable { onClick?.invoke() }
            .fillMaxWidth()
            .background(color = Color(0xffF7F9FA), shape = RoundedCornerShape(8.dp))
            .padding(32.dp)
    ) {
        Text(text = name)
    }
}