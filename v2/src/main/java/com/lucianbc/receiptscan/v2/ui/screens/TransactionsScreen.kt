package com.lucianbc.receiptscan.v2.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
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
import androidx.constraintlayout.compose.ConstraintLayout
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
                LazyRow(
                    modifier = Modifier.padding(horizontal = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
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
    val icon = Category.Grocery.icon()
    val iconSize = 48.dp
    val verticalPadding = 16.dp
    val horizontalPadding = 16.dp
    val width = 104.dp
    val iconDistanceToRight = width - iconSize - horizontalPadding
    val roundness = 16.dp

    ConstraintLayout(
        modifier = Modifier
            .background(Color.Cyan, RoundedCornerShape(roundness))
    ) {
        val (iconRef, nameRef, valueRef) = createRefs()
        Icon(
            painter = painterResource(id = icon),
            contentDescription = "",
            modifier = Modifier
                .width(iconSize)
                .height(iconSize)
                .constrainAs(iconRef) {
                    top.linkTo(parent.top, verticalPadding)
                    absoluteLeft.linkTo(parent.absoluteLeft, horizontalPadding)
                    absoluteRight.linkTo(parent.absoluteRight, iconDistanceToRight)
                }
        )
        Text(
            text = name,
            style = MaterialTheme.typography.overline,
            modifier = Modifier
                .constrainAs(nameRef) {
                    top.linkTo(iconRef.bottom, 32.dp)
                    absoluteLeft.linkTo(parent.absoluteLeft, horizontalPadding)
                }
        )
        Text(
            text = "140 RON",
            style = MaterialTheme.typography.subtitle2,
            modifier = Modifier
                .constrainAs(valueRef) {
                    top.linkTo(nameRef.bottom)
                    absoluteLeft.linkTo(parent.absoluteLeft, horizontalPadding)
                    bottom.linkTo(parent.bottom, verticalPadding)
                }
        )
    }
}

private val categories = (1..10).map { "cat$it" }
private val transactions = (1..100).map { "transaction $it" }

@Preview
@Composable
fun TransactionsScreenPreview() = TransactionsScreen()