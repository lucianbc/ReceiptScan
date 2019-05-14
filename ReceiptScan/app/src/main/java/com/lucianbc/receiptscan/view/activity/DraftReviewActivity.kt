package com.lucianbc.receiptscan.view.activity

import android.os.Bundle
import com.lucianbc.receiptscan.R
import com.lucianbc.receiptscan.base.BaseActivity
import com.lucianbc.receiptscan.viewmodel.DraftReviewViewModel

class DraftReviewActivity :
    BaseActivity<DraftReviewViewModel>(DraftReviewViewModel::class.java) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_draft_review)
    }
}
