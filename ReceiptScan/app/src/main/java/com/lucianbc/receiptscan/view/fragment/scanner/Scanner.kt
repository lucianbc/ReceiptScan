package com.lucianbc.receiptscan.view.fragment.scanner


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata
import com.google.firebase.ml.vision.text.FirebaseVisionText
import com.lucianbc.receiptscan.R
import com.lucianbc.receiptscan.databinding.FragmentScannerBinding
import com.lucianbc.receiptscan.view.fragment.scanner.widget.OcrGraphic
import com.lucianbc.receiptscan.viewmodel.scanner.LiveViewVM
import com.otaliastudios.cameraview.Flash
import com.otaliastudios.cameraview.FrameProcessor
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_scanner.*
import javax.inject.Inject

class Scanner: Fragment() {

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

        val vm = ViewModelProviders.of(this).get(LiveViewVM::class.java)
        binding.viewModel = vm
        binding.lifecycleOwner = this

        vm.flash.observe(this, Observer {
            when (it) {
                true -> scanner_view.flash = Flash.TORCH
                false -> scanner_view.flash = Flash.OFF
            }
        })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        scanner_view.setLifecycleOwner(viewLifecycleOwner)
        ocr_overlay.setCameraInfo(scanner_view.width, scanner_view.height, scanner_view.facing)
//        scanner_view.addFrameProcessor(frameProcessor)
    }

    private val frameProcessor = FrameProcessor {
        Log.d("Frame Processor", it.size.toString())
        val metadata = FirebaseVisionImageMetadata.Builder()
            .setFormat(it.format)
            .setRotation(rotation(it.rotation))
            .setHeight(it.size.height)
            .setWidth(it.size.width)
            .build()
        val image = FirebaseVisionImage.fromByteArray(it.data, metadata)
        val detector = FirebaseVision.getInstance().onDeviceTextRecognizer
        detector.processImage(image)
            .addOnSuccessListener(cb)
    }

    private val cb: (FirebaseVisionText) -> Unit = { result ->
        val graphics = result.textBlocks
            .asSequence()
            .flatMap { it.lines.asSequence() }
            .map { OcrGraphic(ocr_overlay.graphicPresenter, it) }
        ocr_overlay.setGraphics(graphics)
    }


    private fun rotation(rotation: Int): Int {
        return when (rotation) {
            0 -> FirebaseVisionImageMetadata.ROTATION_0
            90 -> FirebaseVisionImageMetadata.ROTATION_90
            180 -> FirebaseVisionImageMetadata.ROTATION_180
            270 -> FirebaseVisionImageMetadata.ROTATION_270
            else -> FirebaseVisionImageMetadata.ROTATION_0
        }
    }
}
