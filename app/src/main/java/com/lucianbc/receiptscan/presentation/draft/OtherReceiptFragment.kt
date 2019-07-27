package com.lucianbc.receiptscan.presentation.draft

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.lucianbc.receiptscan.R
import com.lucianbc.receiptscan.base.BaseFragment
import com.lucianbc.receiptscan.databinding.FragmentReceipt2Binding
import com.lucianbc.receiptscan.util.logd
import kotlinx.android.synthetic.main.fragment_receipt_2.*
import java.util.*

class OtherReceiptFragment
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
    ): FragmentReceipt2Binding? {
        val binding = DataBindingUtil.inflate<FragmentReceipt2Binding>(
            inflater,
            R.layout.fragment_receipt_2,
            container,
            false
        )
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        return binding
    }

    private fun setupActions() {
        editDateBtn.setOnClickListener { openDateDialog(viewModel.date.value) }
        textView5.addTextChangedListener {
            it?.toString()
        }
    }

    private fun setupAdapter() {
        itemsAdapter = OtherReceiptItemsAdapter()
        draftItems.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = itemsAdapter
        }
    }

    private fun observe(viewModel: DraftViewModel) {
        viewModel.products.observe(viewLifecycleOwner, Observer {
             it?.let { itemsAdapter.submitList(it) }
        })
    }

    private fun openDateDialog(date: Date?) {
        val calendar = Calendar.getInstance().apply { time = date?: Date() }
        val listener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            val newDate = Calendar.getInstance().apply { set(year, month, dayOfMonth) }.time
            logd(newDate.toString())
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