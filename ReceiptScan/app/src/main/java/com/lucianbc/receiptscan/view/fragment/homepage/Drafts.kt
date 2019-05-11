package com.lucianbc.receiptscan.view.fragment.homepage


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.lucianbc.receiptscan.R
import com.lucianbc.receiptscan.util.logd
import com.lucianbc.receiptscan.viewmodel.DraftsViewModel
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_drafts.*
import javax.inject.Inject

class Drafts : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: DraftsViewModel
    private lateinit var listAdapter: DraftsListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(DraftsViewModel::class.java)

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

    fun observe(viewModel: DraftsViewModel) {
        viewModel.drafts.observe(this, Observer {
            listAdapter.submitList(it)
        })
    }
}
