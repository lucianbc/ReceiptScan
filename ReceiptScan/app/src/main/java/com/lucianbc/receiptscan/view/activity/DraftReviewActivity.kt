package com.lucianbc.receiptscan.view.activity

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.lucianbc.receiptscan.R
import com.lucianbc.receiptscan.viewmodel.DraftReviewViewModel
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class DraftReviewActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: DraftReviewViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_draft_review)

        viewModel = ViewModelProviders
            .of(this, viewModelFactory)
            .get(DraftReviewViewModel::class.java)
    }
}
