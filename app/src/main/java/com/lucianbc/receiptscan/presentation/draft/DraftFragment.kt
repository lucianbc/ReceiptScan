package com.lucianbc.receiptscan.presentation.draft

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.lucianbc.receiptscan.R
import com.lucianbc.receiptscan.base.BaseFragment
import com.lucianbc.receiptscan.databinding.FragmentDraftBinding
import kotlinx.android.synthetic.main.fragment_draft.*
import java.util.*

class DraftFragment
    : BaseFragment<DraftViewModel>(DraftViewModel::class.java) {

    private lateinit var itemsAdapter: OtherReceiptItemsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initParentViewModel()
        return setupBinding(inflater, container)?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapter()
        observe(viewModel)
        setupActions()
    }

    private fun setupBinding(
        inflater: LayoutInflater, container: ViewGroup?
    ): FragmentDraftBinding? {
        val binding = DataBindingUtil.inflate<FragmentDraftBinding>(
            inflater,
            R.layout.fragment_draft,
            container,
            false
        )
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        return binding
    }

    private fun setupActions() {
        editDateBtn.setOnClickListener { openDateDialog(viewModel.date.value) }
        addProductBtn.setOnClickListener { viewModel.createProduct() }
    }

    private fun setupAdapter() {
        itemsAdapter = OtherReceiptItemsAdapter().apply {
            onItemEdit = {
                viewModel.updateProduct(it)
            }
            onItemSwipe = {
                viewModel.deleteProduct(it)
            }
        }
        draftItems.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = itemsAdapter
        }
        itemsAdapter.bindSwipe(draftItems)
    }

    private fun observe(viewModel: DraftViewModel) {
        viewModel.apply {
            products.observe(viewLifecycleOwner, Observer {
                it?.let { itemsAdapter.submitList(it) }
            })
            merchant.observe(viewLifecycleOwner, Observer {
                it?.let { updateMerchant(it) }
            })
            total.observe(viewLifecycleOwner, Observer {
                it?.toFloatOrNull()?.let { t -> updateTotal(t) }
            })
            date.observe(viewLifecycleOwner, Observer {
                it?.let { updateDate(it) }
            })
        }
    }

    private fun openDateDialog(date: Date?) {
        val calendar = Calendar.getInstance().apply { time = date?: Date() }
        val listener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            val newDate = Calendar.getInstance().apply { set(year, month, dayOfMonth) }.time
            viewModel.date.value = newDate
        }
        DatePickerDialog(
            context!!,
            listener,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }
}