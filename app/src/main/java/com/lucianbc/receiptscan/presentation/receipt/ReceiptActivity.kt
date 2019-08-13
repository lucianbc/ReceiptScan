package com.lucianbc.receiptscan.presentation.receipt

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.lucianbc.receiptscan.R
import com.lucianbc.receiptscan.base.BaseActivity
import com.lucianbc.receiptscan.presentation.ShareOptionsSheet
import kotlinx.android.synthetic.main.fragment_receipt.*

class ReceiptActivity
    : BaseActivity<ReceiptViewModel>(ReceiptViewModel::class.java) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        safeInit()
        setContentView(R.layout.activity_receipt)
        setupShare()
    }

    private fun safeInit() {
        intent.extras?.apply {
            getLong(RECEIPT_ID).let { viewModel.init(it) }
        }
    }

    private fun setupShare() {
        shareReceiptBtn.setOnClickListener {
            ShareOptionsSheet().apply {
                show(supportFragmentManager, ShareOptionsSheet.TAG)
                onTextOnly = { viewModel.exportText(textExporter) }
            }
        }
    }

    private val textExporter = { text: String ->
        Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, text)
            putExtra(Intent.EXTRA_SUBJECT, "Receipt from ${viewModel.merchant.value}")
            type = "text/plain"
        }.let(::startActivity)
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
