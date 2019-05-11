package com.lucianbc.receiptscan.view.fragment.scanner


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.lucianbc.receiptscan.R
import com.lucianbc.receiptscan.databinding.FragmentScannerBinding
import com.lucianbc.receiptscan.domain.model.ScanAnnotations
import com.lucianbc.receiptscan.util.logd
import com.lucianbc.receiptscan.view.fragment.scanner.widget.OcrGraphic
import com.lucianbc.receiptscan.viewmodel.scanner.LiveViewVM
import com.otaliastudios.cameraview.Flash
import com.otaliastudios.cameraview.FrameProcessor
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_scanner.*
import javax.inject.Inject

class Scanner: DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: LiveViewVM

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentScannerBinding>(
            inflater,
            R.layout.fragment_scanner,
            container,
            false
        )

        viewModel = ViewModelProviders
            .of(this, viewModelFactory)
            .get(LiveViewVM::class.java)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        observe(viewModel)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        scanner_view.setLifecycleOwner(viewLifecycleOwner)
        ocr_overlay.setCameraInfo(scanner_view.width, scanner_view.height, scanner_view.facing)
        scanner_view.addFrameProcessor(frameProcessor)
    }

    private fun observe(viewModel: LiveViewVM) {
        viewModel.flash.observe(this, flashObserver)
        viewModel.ocrOverlays.observe(this, graphicsObserver)
    }

    private val flashObserver = Observer<Boolean> {
        when (it) {
            true -> scanner_view.flash = Flash.TORCH
            false -> scanner_view.flash = Flash.OFF
        }
    }

    private val graphicsObserver = Observer<ScanAnnotations> {
        logd("Observing annotations")
        val graphics = it.asSequence()
            .map { element -> OcrGraphic(ocr_overlay.graphicPresenter, element) }
        ocr_overlay.setGraphics(graphics)
    }

    private val frameProcessor = FrameProcessor {
        viewModel.processFrame(it)
    }
}
