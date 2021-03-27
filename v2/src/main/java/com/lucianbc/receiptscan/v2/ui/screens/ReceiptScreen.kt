package com.lucianbc.receiptscan.v2.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.lucianbc.receiptscan.v2.ui.components.Screen
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.*


@Composable
fun ReceiptScreen() {
    Screen {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(top = 64.dp, start = 32.dp, end = 32.dp, bottom = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            CategoryAvatar(category = receipt.category)
            HorizontalSpace()
            Text(text = receipt.category.name)
            HorizontalSpace()
            Text(text = receipt.merchant)
            HorizontalSpace()
            Text(text = receipt.date.show)
            HorizontalSpace()
            Row {
                Text(text = receipt.total.show)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = receipt.currency.show)
            }
            HorizontalSpace()
            SpacedRow {
                Text(text = "Item")
                Text(text = "Price")
            }

            HorizontalSpace()
            Line()
            HorizontalSpace()

            receipt.items.forEach {
                SpacedRow {
                    Text(text = it.name)
                    Text(text = it.price.show)
                }
                HorizontalSpace()
            }

            Line()
            HorizontalSpace()

            SpacedRow {
                Text(text = "Total")
                Text(text = receipt.total.show)
            }
        }
    }
}

@Composable
private fun HorizontalSpace(height: Int = 8) = Spacer(modifier = Modifier.height(height.dp))

@Composable
private fun Line() = Divider(color = Color.Gray, thickness = 1.dp)

@Composable
private fun SpacedRow(content: @Composable RowScope.() -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        content = content
    )
}

val receipt = Receipt(
    category = Category.Grocery,
    merchant = "S.C. PROFI ROM FOOD S.R.L",
    date = Date(),
    total = 11.9,
    currency = Currency.getInstance("RON"),
    items = listOf(
        ReceiptItem("item", 4.2),
        ReceiptItem("item", 7.7),
    ).repeat(10),
)

private fun List<ReceiptItem>.repeat(times: Int) = (1..times)
    .flatMap { this }
    .mapIndexed { index, x -> x.copy(name = "${x.name} - $index") }

data class Receipt(
    val category: Category,
    val merchant: String,
    val date: Date,
    val total: Double,
    val currency: Currency,
    val items: List<ReceiptItem>,
)

data class ReceiptItem(
    val name: String,
    val price: Double,
)

private val format = SimpleDateFormat("dd-MM-yyyy", Locale.US)

val Date.show: String
    get() = format.format(this)

val Double.show: String
    get() = this.toString()

val Currency.show: String
    get() = this.currencyCode

@Preview
@Composable
fun ReceiptScreenPreview() {
    ReceiptScreen()
}