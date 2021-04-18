package com.lucianbc.receiptscan.v2.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet


typealias IconAction = Pair<Painter, () -> Unit>

@Composable
fun ScreenTitle(
    title: String,
    icons: List<IconAction> = emptyList(),
) {
    val constraintSet = ConstraintSet {
        val leftEnd = createGuidelineFromStart(16.dp)
        val rightEnd = createGuidelineFromEnd(16.dp)
        val titleRef = createRefFor("title")
        constrain(titleRef) {
            linkTo(start = leftEnd, end = rightEnd, bias = 0.5f)
            top.linkTo(parent.top, 64.dp)
            if (icons.isEmpty())
                bottom.linkTo(parent.bottom, 16.dp)
        }
        if (icons.isNotEmpty()) {
            val iconRefs = Array(icons.size) { createRefFor("icon-$it") }
            createHorizontalChain(*iconRefs, chainStyle = ChainStyle.Packed)
            iconRefs.forEachIndexed { ix, it ->
                constrain(it) {
                    if (ix == 0) {
                        start.linkTo(titleRef.start)
                    } else {
                        start.linkTo(iconRefs[ix-1].end, 16.dp)
                    }
                    if (ix == icons.size - 1) {
                        end.linkTo(titleRef.end)
                    }

                    top.linkTo(titleRef.bottom, 16.dp)
                    bottom.linkTo(parent.bottom, 16.dp)
                }
            }
        }
    }

    ConstraintLayout(
        constraintSet = constraintSet,
        modifier = Modifier
            .fillMaxWidth()
    ) {

        Text(
            text = title,
            style = MaterialTheme.typography.h4,
            modifier = Modifier
                .layoutId("title"),
        )

        icons.forEachIndexed { ix, it ->
            Icon(
                it.first, contentDescription = "",
                modifier = Modifier
                    .clickable { it.second() }
                    .layoutId("icon-$ix")
                ,
            )
        }
    }
}

@Preview(showBackground = true, name = "With no icons")
@Composable
fun ScreenBigTitlePreview() = ScreenTitle(title = "Exports")

@Preview(showBackground = true, name = "With one icon")
@Composable
fun ScreenBigTitleWithIconPreview() = ScreenTitle(title = "Exports", icons = listOf(
    rememberVectorPainter(Icons.Default.Add) to {},
))

@Preview(showBackground = true, name = "With multiple icon")
@Composable
fun ScreenBigTitleWithIconsPreview() = ScreenTitle(title = "Exports", icons = listOf(
    rememberVectorPainter(Icons.Default.Add) to {},
    rememberVectorPainter(Icons.Default.Search) to {},
    rememberVectorPainter(Icons.Default.Share) to {},
    rememberVectorPainter(Icons.Default.AccountCircle) to {},
))

