package com.lucianbc.receiptscan.presentation.draft

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.lucianbc.receiptscan.R
import com.lucianbc.receiptscan.base.BaseActivity
import kotlinx.android.synthetic.main.fragment_draft.*

class DraftReviewActivity
    : BaseActivity<DraftViewModel>(DraftViewModel::class.java) {
    private var canDiscard =
        DEFAULT_CAN_DISCARD

    private val annotationsFragment by lazy { ReceiptImageFragment() }
    private val currenciesFragment by lazy { CurrencyFragment() }

    @SuppressLint("PrivateResource")
    private val inn = R.anim.mtrl_bottom_sheet_slide_in
    @SuppressLint("PrivateResource")
    private val out = R.anim.mtrl_bottom_sheet_slide_out

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        safeLoadParams()
        setContentView(R.layout.activity_draft_review)
        setupButtons()
    }

    private fun safeLoadParams() {
        val bundle = intent.extras
        bundle?.getBoolean(CAN_DISCARD)?.let { canDiscard = it }
        bundle?.getLong(DRAFT_ID)?.let { viewModel.init(it) }
    }

    private fun setupButtons() {
        imageBtn.setOnClickListener(showAnnotationsListener)
        deleteBtn.setOnClickListener(discardListener)
        saveBtn.setOnClickListener(validateListener)

        editCurrencyBtn.setOnClickListener(showCurrencies)
    }

    private val showAnnotationsListener = View.OnClickListener {
        supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(inn, out, inn, out)
            .replace(R.id.draftContainer, annotationsFragment, ANNOTATIONS_FRAG_TAG)
            .addToBackStack(ANNOTATIONS_FRAG_TAG)
            .commit()
    }

    private val showCurrencies = View.OnClickListener {
        supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(inn, out, inn, out)
            .add(R.id.draftContainer, currenciesFragment, CURRENCIES_FRAG_TAG)
            .addToBackStack(CURRENCIES_FRAG_TAG)
            .commit()
    }

    private val discardListener = View.OnClickListener {
        discardAndFinish()
    }

    private val validateListener = View.OnClickListener {
        viewModel.validateDraft()
        finish()
    }

    override fun onBackPressed() {
        if (canDiscard && supportFragmentManager.backStackEntryCount < 1)
            askAboutDiscard()
        else
            super.onBackPressed()
    }

    private fun askAboutDiscard() =
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.extract_receipt_title))
            .setMessage(getString(R.string.discard_receipt_message))
            .setPositiveButton("Yes") { _, _ -> discardAndFinish() }
            .setNegativeButton("No") { _, _ -> finish() }
            .show()

    private fun discardAndFinish() {
        viewModel.discardDraft()
        finish()
    }

    companion object {
        fun navIntent(
            context: Context,
            draftId: Long = DEFAULT_DRAFT_ID,
            canDiscard: Boolean = DEFAULT_CAN_DISCARD
        ) =
            Intent(context, DraftReviewActivity::class.java).apply {
                putExtra(DRAFT_ID, draftId)
                putExtra(CAN_DISCARD, canDiscard)
            }

        private const val DRAFT_ID = "DRAFT_ID"
        private const val CAN_DISCARD = "CAN_DISCARD"
        private const val ANNOTATIONS_FRAG_TAG = "RECEIPT_ANNOTATIONS"

        private const val CURRENCIES_FRAG_TAG = "CURRENCIES"

        private const val DEFAULT_DRAFT_ID = -1L
        private const val DEFAULT_CAN_DISCARD = false
    }
}
