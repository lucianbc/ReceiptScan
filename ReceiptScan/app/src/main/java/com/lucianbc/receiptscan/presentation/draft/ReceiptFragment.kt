package com.lucianbc.receiptscan.presentation.draft


import android.app.DatePickerDialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.lucianbc.receiptscan.R
import com.lucianbc.receiptscan.databinding.FragmentReceiptBinding
import com.lucianbc.receiptscan.domain.model.Draft
import com.lucianbc.receiptscan.domain.model.Product
import com.lucianbc.receiptscan.domain.service.show
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.product_item_layout.*
import kotlinx.android.synthetic.main.product_item_layout.view.*
import kotlinx.android.synthetic.main.receipt_layout.*
import java.util.*
import javax.inject.Inject


class ReceiptFragment : DaggerFragment() {

    private lateinit var itemsAdapter: ReceiptItemsAdapter

    @Inject
    lateinit var viewModelFactory: DraftReviewViewModel.Factory
    lateinit var viewModel: DraftReviewViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initParentViewModel()
        return setupBinding(inflater, container)?.root
    }

    private fun initParentViewModel() {
        viewModel = ViewModelProviders
            .of(activity!!, viewModelFactory)
            .get(DraftReviewViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapter()
        observe(viewModel)
    }

    private fun setupBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentReceiptBinding? {
        val binding = DataBindingUtil.inflate<FragmentReceiptBinding>(
            inflater,
            R.layout.fragment_receipt,
            container,
            false
        )
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        return binding
    }

    private fun observe(viewModel: DraftReviewViewModel) {
        viewModel.receipt.observe(viewLifecycleOwner, Observer {
            itemsAdapter.submitList(it.products)
            merchantTextView.text = it.receipt.merchantName
            dateTextView.text = it.receipt.date?.show()
            currencyTextView.text = it.receipt.currency?.toString()
            totalTextView.text = it.receipt.total?.toString()
            setupDialogs(it.receipt)
        })
    }

    private fun setupAdapter() {
        itemsAdapter = ReceiptItemsAdapter(onProdTap)
        receiptItemsList.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = itemsAdapter
        }
    }

    private val onProdTap: (Product) -> Unit = {
        val view = layoutInflater.inflate(R.layout.product_item_layout, null)
        AlertDialog.Builder(context!!)
            .setView(view)
            .setTitle("Edit Product")
            .setPositiveButton("Save") { _, _ ->
                view.prodPrice.text.toString().toFloatOrNull()?.let { pr ->
                    val name = view.prodName.text.toString()
                    val prod = it.copy(name=name, price=pr)
                    viewModel.updateProducts(prod)
                }
            }
            .setNegativeButton("Delete") { _, _ -> viewModel.deleteProduct(it) }
            .setNeutralButton("Cancel") { _, _ -> }
            .show()
        view.prodName.text = Editable.Factory().newEditable(it.name)
        view.prodPrice.text = Editable.Factory().newEditable(it.price.toString())
    }

    private fun setupDialogs(model: Draft) {
        merchantContainer.setOnClickListener { merchantDialog(model.merchantName) }
        totalContainer.setOnClickListener { totalDialog(model.total.toString()) }
        dateContainer.setOnClickListener { dateDialog(model.date) }
        addProductBtn.setOnClickListener { addProdDialog() }
    }

    val cb = DialogInterface.OnClickListener { v1, v2 ->

    }

    private fun addProdDialog() {
        val view = layoutInflater.inflate(R.layout.product_item_layout, null)
        AlertDialog.Builder(context!!)
            .setView(view)
            .setTitle("Add product")
            .setPositiveButton("Add") { _, _ ->
                val name = view.prodName.text.toString()
                val price = view.prodPrice.text.toString().toFloatOrNull()
                price?.let {
                    viewModel.addProduct(name, price) }
                }
            .setNegativeButton("Cancel") { _, _ -> }
            .show()
    }

    private fun dateDialog(date: Date?) {
        val calendar = Calendar.getInstance().apply {
            time = date ?: Date()
        }

        val listener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            val newDate = Calendar.getInstance().apply { set(year, month, dayOfMonth) }.time
            viewModel.updateDate(newDate)
        }

        val dialog = DatePickerDialog(
            context!!,
            listener,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH))

        dialog.show()
    }

    private val merchantDialog =
        dialog(InputType.TYPE_CLASS_TEXT, "Merchant") {
            viewModel.updateMerchant(it.text.toString())
        }

    private val totalDialog =
        dialog(InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL,"Total") {
            it.text.toString().toFloatOrNull()?.let { t -> viewModel.updateTotal(t) }
        }

    private fun dialog(inputType: Int, property: String, cb: (EditText) -> Unit): (String?) -> Unit {
        return fun (value: String?) {
            val input = EditText(context)
            input.text = Editable.Factory().newEditable(value)
            input.inputType = inputType

            AlertDialog.Builder(context!!)
                .setTitle(property)
                .setView(input)
                .setPositiveButton("Ok") { _, _ -> cb(input) }
                .setNegativeButton("Cacnel") { _, _ -> }
                .show()
        }
    }
}
