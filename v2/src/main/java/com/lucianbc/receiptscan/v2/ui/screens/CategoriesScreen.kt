package com.lucianbc.receiptscan.v2.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lucianbc.receiptscan.v2.R
import com.lucianbc.receiptscan.v2.ui.components.Screen

@ExperimentalFoundationApi
@Composable
fun CategoriesScreen() {
    Screen {
        TitleBar(title = "Categories")
        LazyVerticalGrid(
            cells = GridCells.Fixed(2)
        ) {
            items(Category.values()) {
                CategoryItem(it)
            }
        }
    }
}

@Composable
fun TitleBar(title: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(text = title, style = MaterialTheme.typography.h6)
    }
}

@Composable
fun CategoryItem(category: Category) {
    Column(
        modifier = Modifier
            .clickable { }
            .padding(vertical = 16.dp, horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        CategoryAvatar(category = category)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = category.let { when(it) {
            Category.NotAssigned -> "Others"
            else -> it.name
        } })
    }
}

@Composable
fun CategoryAvatar(category: Category, size: Int = 80, padding: Int = 16) {
    Image(
        contentDescription = "",
        modifier = Modifier
            .background(MaterialTheme.colors.secondary, shape = CircleShape)
            .padding(padding.dp)
            .width(size.dp)
            .height(size.dp),
        painter = painterResource(id = category.icon()),
    )
}

enum class Category {
    NotAssigned,
    Grocery,
    Coffee,
    Transportation,
    Restaurant,
    Snack
}

private fun Category.icon(): Int {
    return when (this) {
        Category.NotAssigned -> R.drawable.ic_coin_24dp
        Category.Grocery -> R.drawable.ic_grocery
        Category.Coffee -> R.drawable.ic_coffee_cup
        Category.Transportation -> R.drawable.ic_taxi
        Category.Restaurant -> R.drawable.ic_restaurant
        Category.Snack -> R.drawable.ic_snack
    }
}

@ExperimentalFoundationApi
@Preview
@Composable
fun CategoriesScreenPreview() {
    CategoriesScreen()
}
