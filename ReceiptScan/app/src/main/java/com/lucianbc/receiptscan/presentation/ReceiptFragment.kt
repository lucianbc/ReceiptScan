package com.lucianbc.receiptscan.presentation


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.lucianbc.receiptscan.R
import com.lucianbc.receiptscan.domain.model.Receipt
import kotlinx.android.synthetic.main.receipt_layout.*


class ReceiptFragment : Fragment() {

    private lateinit var itemsAdapter: ReceiptItemsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_receipt, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapter()
    }

    private fun setupAdapter() {
        itemsAdapter = ReceiptItemsAdapter()
        receiptItemsList.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = itemsAdapter
        }
        itemsAdapter.submitList(hugeList)
    }

    private val smallList = listOf(
        Receipt.Item("Ochelari 3D 3.0", 8.55),
        Receipt.Item("Proiectie Film 3D", 44.00)
    )

    private val hugeList = generateSequence { smallList }
        .flatten()
        .take(55)
        .toList()
}
