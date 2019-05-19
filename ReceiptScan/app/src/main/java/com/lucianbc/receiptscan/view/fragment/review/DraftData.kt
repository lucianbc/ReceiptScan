package com.lucianbc.receiptscan.view.fragment.review

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.lucianbc.receiptscan.R
import com.lucianbc.receiptscan.base.BaseFragment
import com.lucianbc.receiptscan.domain.model.ReceiptDraft
import com.lucianbc.receiptscan.domain.service.LineUnificationStrategy
import com.lucianbc.receiptscan.domain.service.ThresholdBetweenNeighbors
import com.lucianbc.receiptscan.viewmodel.DraftReviewViewModel
import kotlinx.android.synthetic.main.fragment_draft_data.*

class DraftData:
    BaseFragment<DraftReviewViewModel>(DraftReviewViewModel::class.java) {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initParentViewModel()
        return inflater.inflate(R.layout.fragment_draft_data, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observe(viewModel)
    }

    private fun observe(vm: DraftReviewViewModel) {
        vm.draft.observe(this, Observer {
            draft_data_text.text = receiptText(it)
        })
    }

    private fun receiptText(data: ReceiptDraft): String {
        val strategy: LineUnificationStrategy = ThresholdBetweenNeighbors(0.5F)
        val lines = strategy.unify(data.annotations)
        return lines.joinToString("\n") {
                line -> line.joinToString("\t") { it.text }
        }
    }
}
