package com.lucianbc.receiptscan.v2

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.lucianbc.receiptscan.v2.ui.ComposableSampleTheme
import com.lucianbc.receiptscan.v2.ui.components.ScreenTitle
import androidx.activity.compose.setContent
import com.lucianbc.receiptscan.v2.ui.screens.DraftsScreen

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DraftsScreen()
        }
    }
}

@Composable
fun MainScreen() {
    ComposableSampleTheme {
        ScreenTitle(title = "Title here")
    }
}

@Preview(
    showBackground = true,
)
@Composable
fun DefaultPreview() {
    MainScreen()
}
