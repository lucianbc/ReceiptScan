package com.lucianbc.receiptscan.presentation.draft

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.lucianbc.receiptscan.R
import com.lucianbc.receiptscan.base.BaseFragment
import com.lucianbc.receiptscan.presentation.service.swapImageBitmap
import kotlinx.android.synthetic.main.fragment_annotations.*

class ReceiptImageFragment
    : BaseFragment<DraftViewModel>(DraftViewModel::class.java) {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initParentViewModel()
        return inflater.inflate(R.layout.fragment_annotations, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observe(viewModel)
    }

    private fun observe(viewModel: DraftViewModel) {
        viewModel.receiptImage.observe(viewLifecycleOwner, Observer {
            receiptView.swapImageBitmap(it)
        })
    }
}
