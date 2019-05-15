package com.lucianbc.receiptscan.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.lucianbc.receiptscan.R
import com.lucianbc.receiptscan.base.BaseActivity
import com.lucianbc.receiptscan.domain.model.ID
import com.lucianbc.receiptscan.util.loge
import com.lucianbc.receiptscan.view.fragment.review.DraftData
import com.lucianbc.receiptscan.view.fragment.review.DraftImage
import com.lucianbc.receiptscan.viewmodel.DraftReviewViewModel
import kotlinx.android.synthetic.main.activity_draft_review.*

class DraftReviewActivity :
    BaseActivity<DraftReviewViewModel>(DraftReviewViewModel::class.java) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        safeLoadDraft()
        setContentView(R.layout.activity_draft_review)
        setupTabs()
    }

    private fun safeLoadDraft() {
        val default = -1L
        val bundle = intent.extras

        when (val id = bundle?.getLong(DRAFT_ID_KEY, default)) {
            null, default -> loge("No draft id passed to the DraftReviewActivity")
            else -> viewModel.initialize(id)
        }
    }

    private fun setupTabs() {
        val adapter = TabAdapter(supportFragmentManager)
        adapter.add(DraftData(), "Data")
        adapter.add(DraftImage(), "Image")
        draft_pager.adapter = adapter
        draft_tabs.setupWithViewPager(draft_pager)
    }

    companion object {
        fun navIntent(ctx: Context, draftId: ID): Intent {
            val intent = Intent(ctx, DraftReviewActivity::class.java)
            intent.putExtra(DRAFT_ID_KEY, draftId)
            return intent
        }

        private const val DRAFT_ID_KEY = "DRAFT_ID"
    }
}
