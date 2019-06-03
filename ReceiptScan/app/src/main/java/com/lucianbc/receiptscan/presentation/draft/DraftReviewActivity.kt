package com.lucianbc.receiptscan.presentation.draft

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.lucianbc.receiptscan.R
import com.lucianbc.receiptscan.di.DaggerAwareViewModelFactory
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.fragment_receipt.*
import javax.inject.Inject

class DraftReviewActivity : DaggerAppCompatActivity() {
    @Inject
    lateinit var viewModelFactory: DraftReviewViewModel.Factory
    lateinit var viewModel: DraftReviewViewModel

    private var canDiscard =
        DEFAULT_CAN_DISCARD

    private val annotationsFragment by lazy { AnnotationsFragment() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        safeLoadParams()
        setContentView(R.layout.activity_draft_review)
        editFab.setOnClickListener(showAnnotations)
    }

    @SuppressLint("PrivateResource")
    private val showAnnotations = View.OnClickListener {
        val _in = R.anim.mtrl_bottom_sheet_slide_in
        val _out = R.anim.mtrl_bottom_sheet_slide_out
        supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(_in, _out, _in, _out)
            .replace(R.id.draftContainer, annotationsFragment, "RECEIPT_ANNOTATIONS")
            .addToBackStack("RECEIPT_ANNOTATIONS")
            .commit()
    }

    private fun safeLoadParams() {
        val bundle = intent.extras
        bundle?.getBoolean(CAN_DISCARD)?.let { canDiscard = it }
        bundle?.getLong(DRAFT_ID)?.let { viewModelFactory.draftId = it }
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
            .setPositiveButton("Yes") { _, _ -> finish() }
            .setNegativeButton("No", null)
            .show()

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

        private const val DEFAULT_DRAFT_ID = -1L
        private const val DEFAULT_CAN_DISCARD = false
    }
}
