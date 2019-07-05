package com.lucianbc.receiptscan.presentation.draft


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.lucianbc.receiptscan.R
import com.lucianbc.receiptscan.presentation.service.swapImageBitmap
import com.lucianbc.receiptscan.util.logd
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_annotations.*
import javax.inject.Inject


class ReceiptImageFragment : DaggerFragment() {
    @Inject
    lateinit var viewModelFactory: DraftReviewViewModel.Factory
    lateinit var viewModel: DraftReviewViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initParentViewModel()
        return inflater.inflate(R.layout.fragment_annotations, container, false)
    }

    private fun initParentViewModel() {
        viewModel = ViewModelProviders
            .of(activity!!, viewModelFactory)
            .get(DraftReviewViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observe(viewModel)
    }

    private fun observe(viewModel: DraftReviewViewModel) {
        viewModel.image.observe(viewLifecycleOwner, Observer {
            receiptView.swapImageBitmap(it)
        })
    }
}
