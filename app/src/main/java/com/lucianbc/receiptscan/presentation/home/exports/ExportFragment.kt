package com.lucianbc.receiptscan.presentation.home.exports

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.lucianbc.receiptscan.R
import com.lucianbc.receiptscan.base.BaseFragment
import com.lucianbc.receiptscan.infrastructure.services.ExportService
import com.lucianbc.receiptscan.presentation.Event
import kotlinx.android.synthetic.main.fragment_export.*
import org.greenrobot.eventbus.EventBus
import javax.inject.Inject

class ExportFragment
    : BaseFragment<ExportViewModel>(ExportViewModel::class.java) {

    @Inject
    lateinit var eventBus: EventBus

    private val adapter = ExportsAdapter { i -> activity?.startActivity(i) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initViewModel()
        return inflater.inflate(R.layout.fragment_export, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createExportBtn.setOnClickListener {
            eventBus.post(Event.ExportForm { s ->
                activity?.let {
                    ExportService.intent(activity!!, s).let(it::startService)
                }
                fragmentManager?.popBackStack()
            })
        }
        setupAdapter()
        observe(viewModel)
    }

    private fun setupAdapter() {
        exportsList.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = this@ExportFragment.adapter
        }
    }

    private fun observe(viewModel: ExportViewModel) {
        viewModel.exports.observe(viewLifecycleOwner, Observer(adapter::submitList))
    }
}
