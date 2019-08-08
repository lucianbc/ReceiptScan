package com.lucianbc.receiptscan.presentation.receipt

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.lucianbc.receiptscan.R

class ReceiptActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_receipt)
    }

    companion object {
        fun navIntent(
            context: Context
        ) = Intent (context, ReceiptActivity::class.java)
    }
}
