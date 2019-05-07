package com.lucianbc.receiptscan.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.lucianbc.receiptscan.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        scan_btn.setOnClickListener {
            startActivity(Intent(this, ScannerActivity::class.java))
        }
    }
}
