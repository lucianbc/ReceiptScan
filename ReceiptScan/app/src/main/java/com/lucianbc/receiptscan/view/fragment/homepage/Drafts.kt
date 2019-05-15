package com.lucianbc.receiptscan.view.fragment.homepage


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.lucianbc.receiptscan.R
import com.lucianbc.receiptscan.base.BaseFragment
import com.lucianbc.receiptscan.domain.model.ReceiptDraft
import com.lucianbc.receiptscan.view.activity.DraftReviewActivity
import com.lucianbc.receiptscan.viewmodel.DraftsViewModel
import kotlinx.android.synthetic.main.fragment_drafts.*

class Drafts:
    BaseFragment<DraftsViewModel>(DraftsViewModel::class.java) {

    private lateinit var listAdapter: DraftsListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initViewModel()

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_drafts, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapter()
        observe(viewModel)
    }

    private fun setupAdapter() {
        listAdapter = DraftsListAdapter(itemClick)
        drafts_list.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = listAdapter
        }
    }

    private val itemClick: (ReceiptDraft) -> Unit = {
        val ctx: Context = activity!!
        val intent = DraftReviewActivity.navIntent(ctx, it.id)
        activity?.startActivity(intent)
    }

    private fun observe(viewModel: DraftsViewModel) {
        viewModel.drafts.observe(this, Observer {
            listAdapter.submitList(it)
        })
    }
}
