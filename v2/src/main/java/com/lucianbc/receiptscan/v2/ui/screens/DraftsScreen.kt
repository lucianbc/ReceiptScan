package com.lucianbc.receiptscan.v2.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lucianbc.receiptscan.v2.ui.components.ExpenseListItem
import com.lucianbc.receiptscan.v2.ui.components.Screen

@Composable
fun DraftsScreen() {
    Screen(title = "Drafts") {
        LazyColumn(
            contentPadding = PaddingValues(bottom = 50.dp),
            modifier = Modifier
                .fillMaxHeight()
                .padding(horizontal = 8.dp)
        ) {
            itemsIndexed(expenses) { _, item ->
                ExpenseListItem(name = item) {}

                Spacer(
                    modifier = Modifier
                        .height(4.dp)
                        .background(Color.Red)
                )
            }
        }
    }
}

private val expenses = (1..10).map { "Expense $it" }

@Preview
@Composable
fun DraftsScreenPreview() {
    DraftsScreen()
}
