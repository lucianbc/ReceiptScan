package com.lucianbc.receiptscan.v2.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lucianbc.receiptscan.v2.R
import com.lucianbc.receiptscan.v2.ui.components.Screen

@Composable
fun TransactionsScreen() {
    val icons = listOf(
        rememberVectorPainter(Icons.Default.Search) to {},
        painterResource(id = R.drawable.ic_baseline_filter_alt_24) to {}
    )

    Screen("Transactions", icons = icons) {
        LazyColumn {
            item(key = "categoriesRow") {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    itemsIndexed(categories) { _, it ->
                        CategoryItem(it)
                    }
                }
            }
            itemsIndexed(transactions) { _, it ->
                Text(text = it)
            }
        }
    }
}

@Composable
fun CategoryItem(name: String) {
    Column(
        modifier = Modifier
            .background(Color.Cyan)
            .padding(horizontal = 24.dp, vertical = 8.dp)
    ) {
        Text(text = name)
    }
}

private val categories = (1..10).map { "cat$it" }
private val transactions = (1..100).map { "transaction $it" }

@Preview
@Composable
fun TransactionsScreenPreview() = TransactionsScreen()