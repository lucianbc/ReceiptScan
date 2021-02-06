package com.lucianbc.receiptscan.v2

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Text
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.setContent
import com.lucianbc.receiptscan.v2.ui.ComposableSampleTheme
import androidx.ui.tooling.preview.Preview

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
        Surface(color = MaterialTheme.colors.background) {
            Greeting("My app")
        }
    }
}

@Composable
fun Greeting(name: String) =
    Text(text = "Hello $name!")

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MainScreen()
}