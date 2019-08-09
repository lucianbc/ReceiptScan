package com.lucianbc.receiptscan.presentation.receipt

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.lucianbc.receiptscan.R
import com.lucianbc.receiptscan.base.BaseActivity

class ReceiptActivity
    : BaseActivity<ReceiptViewModel>(ReceiptViewModel::class.java) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        safeInit()
        setContentView(R.layout.activity_receipt)
    }

    private fun safeInit() {
        intent.extras?.apply {
            getLong(RECEIPT_ID).let { viewModel.init(it) }
        }
    }

    companion object {
        fun navIntent(
            context: Context,
            receiptId: Long
        ) = Intent (context, ReceiptActivity::class.java).apply {
            putExtra(RECEIPT_ID, receiptId)
        }

        private const val RECEIPT_ID = "RECEIPT_ID"
    }
}
