package com.lucianbc.receiptscan.presentation.home.drafts


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.lucianbc.receiptscan.R
import com.lucianbc.receiptscan.base.BaseFragment
import com.lucianbc.receiptscan.databinding.FragmentDraftsBinding
import com.lucianbc.receiptscan.domain.usecase.ListDraftsUseCase
import com.lucianbc.receiptscan.presentation.draft.DraftReviewActivity
import kotlinx.android.synthetic.main.fragment_drafts.*


class DraftsFragment : BaseFragment<DraftsViewModel>(DraftsViewModel::class.java) {

    private lateinit var draftsAdapter: DraftsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initViewModel()
        return setupBinding(inflater, container)?.root
    }

    private fun setupBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentDraftsBinding? {
        val binding = DataBindingUtil.inflate<FragmentDraftsBinding>(
            inflater,
            R.layout.fragment_drafts,
            container,
            false
        )
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        return binding
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapter()
        observe(viewModel)
    }

    private fun setupAdapter() {
        draftsAdapter = DraftsAdapter(goToDraft)
        draftsList.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = draftsAdapter
        }
    }

    private fun observe(viewModel: DraftsViewModel) =
        viewModel.drafts.observe(viewLifecycleOwner, Observer { it?.submit() })

    private fun List<ListDraftsUseCase.DraftItem>.submit() = draftsAdapter.submitList(this)

    private val goToDraft: DraftItemClick = {
        val intent = DraftReviewActivity.navIntent(activity!!, it.id)
        startActivity(intent)
    }
}
