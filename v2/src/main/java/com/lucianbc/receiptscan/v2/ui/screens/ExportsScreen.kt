package com.lucianbc.receiptscan.v2.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.lucianbc.receiptscan.v2.ui.components.Screen
import com.lucianbc.receiptscan.v2.R
import java.util.*
import kotlin.random.Random

@Composable
fun ExportsScreen() {
    val icons = listOf(
        rememberVectorPainter(Icons.Default.Add) to {}
    )

    Screen(title = "Exports", icons = icons) {
        LazyColumn(
            contentPadding = PaddingValues(bottom = 50.dp),
            modifier = Modifier
                .fillMaxHeight()
                .padding(horizontal = 8.dp)
        ) {
            itemsIndexed(exports) { _, it ->
                ExportRow(item = it)
                Spacer(
                    modifier = Modifier
                        .height(4.dp)
                        .background(Color.Red)
                )
            }
        }
    }
}

val exports = (1..10).map {
    ExportItem(Date(), Date(), Random.nextInt(0, 3).let { Status.values()[it] })
}

enum class Status {
    UPLOADING,
    WAITING_DOWNLOAD,
    COMPLETE
}

data class ExportItem(
    val firstDate: Date,
    val lastDate: Date,
    val status: Status,
)

@Composable
fun ExportRow(item: ExportItem) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color(0xffF7F9FA), shape = RoundedCornerShape(8.dp))
    ) {
        val verticalSpace = 24.dp
        val horizontalSpace = 16.dp
        val startGuide = createGuidelineFromStart(verticalSpace)
        val endGuide = createGuidelineFromEnd(verticalSpace)

        val (
            beginTextRef,
            endTextRef,
            statusTextRef,
            downloadButtonRef,
            copyButtonRef,
        ) = createRefs()

        Text(text = item.firstDate.show, modifier = Modifier.constrainAs(beginTextRef) {
            start.linkTo(startGuide)
            top.linkTo(parent.top, horizontalSpace)
        })
        Text(text = item.lastDate.show, modifier = Modifier.constrainAs(endTextRef) {
            start.linkTo(beginTextRef.end, 16.dp)
            top.linkTo(parent.top, horizontalSpace)
        })
        Text(text = item.status.name, modifier = Modifier.constrainAs(statusTextRef) {
            start.linkTo(startGuide)
            top.linkTo(beginTextRef.bottom, 8.dp)
            bottom.linkTo(parent.bottom, horizontalSpace)
        })

        Icon(
            painter = painterResource(id = R.drawable.ic_file_download_black_24dp),
            contentDescription = null,
            modifier = Modifier
                .clickable { }
                .constrainAs(downloadButtonRef) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    end.linkTo(copyButtonRef.start, 16.dp)
                }
        )
        Icon(
            painter = painterResource(id = R.drawable.ic_content_copy_black_24dp),
            contentDescription = null,
            modifier = Modifier
                .clickable { }
                .constrainAs(copyButtonRef) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    end.linkTo(endGuide)
                },
        )
    }
}

@Composable
@Preview
fun ExportsScreenPreview() = ExportsScreen()