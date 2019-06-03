package com.lucianbc.receiptscan.presentation.draft


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.lucianbc.receiptscan.R
import com.lucianbc.receiptscan.di.DaggerAwareViewModelFactory
import com.lucianbc.receiptscan.domain.model.Product
import com.lucianbc.receiptscan.domain.model.ProductDraft
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.receipt_layout.*
import javax.inject.Inject


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
        ProductDraft("Ochelari 3D 3.0", 8.55F),
        ProductDraft("Proiectie Film 3D", 44.00F)
    )

    private val hugeList = generateSequence { smallList }
        .flatten()
        .take(55)
        .toList()
}
