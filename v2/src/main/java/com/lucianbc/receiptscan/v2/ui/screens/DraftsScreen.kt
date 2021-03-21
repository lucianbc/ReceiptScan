package com.lucianbc.receiptscan.v2.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
                ExpenseListItem(name = item)

                Spacer(modifier = Modifier
                    .height(4.dp)
                    .background(Color.Red))
            }
        }
    }
}

private val expenses = (1..10).map { "Expense $it" }

@Composable
fun ExpenseListItem(name: String) {
    Row(
        modifier = Modifier
            .clickable { }
            .fillMaxWidth()
            .background(color = Color(0xffF7F9FA), shape = RoundedCornerShape(8.dp))
            .padding(32.dp)
    ) {
        Text(text = name)
    }
}

@Preview
@Composable
fun DraftsScreenPreview() {
    DraftsScreen()
}
