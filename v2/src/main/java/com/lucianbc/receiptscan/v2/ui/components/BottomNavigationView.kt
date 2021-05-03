package com.lucianbc.receiptscan.v2.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp


interface BottomNavigationScope {
    fun item(label: String, painter: Painter, screen: @Composable () -> Unit)
}

@Composable
fun BottomNavigationView(scope: @Composable BottomNavigationScope.() -> Unit) {
    val items = ScopeImpl().run { scope(); items }
    BottomNavigationView(tabs = items)
}

private class ScopeImpl : BottomNavigationScope {
    val items = mutableListOf<BottomNavigationItem>()

    override fun item(
        label: String,
        painter: Painter,
        screen: @Composable () -> Unit
    ) {
        items.add(BottomNavigationItem(label, painter, screen))
    }
}

private data class BottomNavigationItem(
    val label: String,
    val iconPainter: Painter,
    val screen: @Composable () -> Unit,
)

@Composable
private fun BottomNavigationView(tabs: List<BottomNavigationItem>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        val enabledScreenIndex = remember { mutableStateOf(0) }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            tabs.forEachIndexed { index, item ->
                if (index == enabledScreenIndex.value) {
                    item.screen.invoke()
                }
            }
        }
        BottomBar(buttons = tabs) {
            enabledScreenIndex.value = it
        }
    }
}

@Composable
private fun BottomBar(
    buttons: List<BottomNavigationItem>,
    onItemClicked: (Int) -> Unit,
) {
    fun RowScope.itemModifier() = Modifier
        .fillMaxHeight()
        .width(168.dp)
        .fillMaxWidth()
        .weight(1f, false)

    Surface(elevation = 8.dp, modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            horizontalArrangement = Arrangement.Center,
        ) {
            buttons.forEachIndexed { index, item ->

                Column(
                    modifier = itemModifier().clickable { onItemClicked(index) },
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Icon(
                        painter = item.iconPainter,
                        contentDescription = item.label,
                        modifier = Modifier.size(24.dp)
                    )
                    Text(text = item.label)
                }
            }

        }
    }
}