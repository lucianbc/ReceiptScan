package com.lucianbc.receiptscan.view.fragment.review


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer

import com.lucianbc.receiptscan.R
import com.lucianbc.receiptscan.base.BaseFragment
import com.lucianbc.receiptscan.viewmodel.DraftReviewViewModel
import kotlinx.android.synthetic.main.fragment_draft_image.*

class DraftImage:
    BaseFragment<DraftReviewViewModel>(DraftReviewViewModel::class.java) {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initParentViewModel()
        return inflater.inflate(R.layout.fragment_draft_image, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.image.observe(this, Observer {
            draft_photo_view.setImageBitmap(it)
        })
    }
}
