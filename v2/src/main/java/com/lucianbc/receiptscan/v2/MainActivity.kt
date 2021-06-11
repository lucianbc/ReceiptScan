package com.lucianbc.receiptscan.v2

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.lucianbc.receiptscan.v2.ui.Navigation

@ExperimentalFoundationApi
class MainActivity : AppCompatActivity() {

    @ExperimentalFoundationApi
    @SuppressLint("MissingSuperCall")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Navigation()
        }
    }
}

@ExperimentalFoundationApi
@Preview(
    showBackground = true,
)
@Composable
fun DefaultPreview() {
    Navigation()
}
