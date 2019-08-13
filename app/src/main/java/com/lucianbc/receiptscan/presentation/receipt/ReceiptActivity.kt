package com.lucianbc.receiptscan.presentation.receipt

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.lucianbc.receiptscan.R
import com.lucianbc.receiptscan.base.BaseActivity
import com.lucianbc.receiptscan.presentation.ShareOptionsSheet
import com.lucianbc.receiptscan.util.loge
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
                onImageOnly = { viewModel.exportImage(imageExporter, shareFileErrorHandler) }
                onBoth = { viewModel.exportBoth(bothExporter, shareFileErrorHandler) }
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

    private val imageExporter = { imageUri: Uri ->
        Intent().apply {
            action = Intent.ACTION_SEND_MULTIPLE
            type = "image/jpeg"
            putParcelableArrayListExtra(Intent.EXTRA_STREAM, arrayListOf(imageUri))
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }.let(::startActivity)
    }

    private val bothExporter = { text: String, imageUri: Uri ->
        Intent().apply {
            action = Intent.ACTION_SEND_MULTIPLE
            type = "image/jpeg"
            putParcelableArrayListExtra(Intent.EXTRA_STREAM, arrayListOf(imageUri))
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            putExtra(Intent.EXTRA_TEXT, text)
            putExtra(Intent.EXTRA_SUBJECT, "Receipt from ${viewModel.merchant.value}")
        }.let(::startActivity)
    }

    private val shareFileErrorHandler = { t: Throwable ->
        loge("Error exporting image", t)
        AlertDialog.Builder(this)
            .setTitle("Error!")
            .setMessage("Error when sharing the image")
            .setNegativeButton("Dismiss") { _, _ ->
                supportFragmentManager.apply {
                    findFragmentByTag(ShareOptionsSheet.TAG)?.let {
                        beginTransaction()
                            .remove(it)
                            .commit()
                    }
                }
            }
            .show()
        Unit
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
