package com.lucianbc.receiptscan.v2

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.setContent
import androidx.ui.tooling.preview.Preview
import com.lucianbc.receiptscan.v2.ui.ComposableSampleTheme
import com.lucianbc.receiptscan.v2.ui.components.ScreenTitle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen()
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
