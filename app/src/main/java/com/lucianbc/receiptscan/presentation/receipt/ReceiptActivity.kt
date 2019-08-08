package com.lucianbc.receiptscan.presentation.receipt

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.lucianbc.receiptscan.R
import com.lucianbc.receiptscan.base.BaseActivity
import com.lucianbc.receiptscan.presentation.draft.DraftViewModel

class ReceiptActivity
    : BaseActivity<DraftViewModel>(DraftViewModel::class.java) {

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
