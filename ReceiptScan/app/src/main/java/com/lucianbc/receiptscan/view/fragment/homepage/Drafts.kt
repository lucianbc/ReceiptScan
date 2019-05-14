package com.lucianbc.receiptscan.view.fragment.homepage


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.lucianbc.receiptscan.R
import com.lucianbc.receiptscan.base.BaseFragment
import com.lucianbc.receiptscan.util.logd
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

        listAdapter = DraftsListAdapter {
            logd("Item tapped")
        }

        drafts_list.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = listAdapter
        }

        observe(viewModel)
    }

    private fun observe(viewModel: DraftsViewModel) {
        viewModel.drafts.observe(this, Observer {
            listAdapter.submitList(it)
        })
    }
}
