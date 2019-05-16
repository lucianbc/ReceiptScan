package com.lucianbc.receiptscan.view.fragment.scanner


import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.lucianbc.receiptscan.R
import com.lucianbc.receiptscan.base.BaseFragment
import com.lucianbc.receiptscan.databinding.FragmentScannerBinding
import com.lucianbc.receiptscan.domain.service.OcrElements
import com.lucianbc.receiptscan.view.fragment.scanner.widget.OcrGraphic
import com.lucianbc.receiptscan.viewmodel.scanner.LiveViewVM
import com.lucianbc.receiptscan.viewmodel.scanner.ScannerViewModel
import com.otaliastudios.cameraview.CameraListener
import com.otaliastudios.cameraview.Flash
import com.otaliastudios.cameraview.FrameProcessor
import com.otaliastudios.cameraview.PictureResult
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_scanner.*


class Scanner:
    BaseFragment<LiveViewVM>(LiveViewVM::class.java) {

    private lateinit var parentViewModel: ScannerViewModel

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

        initViewModel()

        parentViewModel = ViewModelProviders
            .of(activity!!, viewModelFactory)
            .get(ScannerViewModel::class.java)
        
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        observe(viewModel)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ocr_overlay.setCameraInfo(scanner_view.width, scanner_view.height, scanner_view.facing)
        scanner_view.setLifecycleOwner(viewLifecycleOwner)
        scanner_view.addFrameProcessor(frameProcessor)
        scanner_view.addCameraListener(cameraListener)
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

    private val graphicsObserver = Observer<OcrElements> {
        val graphics = it.asSequence()
            .map { element -> OcrGraphic(ocr_overlay.graphicPresenter, element) }
        ocr_overlay.setGraphics(graphics)
    }

    private val frameProcessor = FrameProcessor {
        viewModel.processFrame(it)
    }

    private val cameraListener = object: CameraListener() {
        override fun onPictureTaken(result: PictureResult) {
            val bmpResult = result.toBitmap()
                .subscribeOn(Schedulers.io())
            parentViewModel.scanImage(bmpResult)
        }
    }

    private fun PictureResult.toBitmap(): Observable<Bitmap> {
        val bmpResult = PublishSubject.create<Bitmap>()
        this.toBitmap {
            if (it != null) bmpResult.onNext(it)
            else {
                val errMsg: String = resources.getString(R.string.picture_error_message)
                bmpResult.onError(IllegalStateException(errMsg))
            }
        }
        return bmpResult
    }
}
