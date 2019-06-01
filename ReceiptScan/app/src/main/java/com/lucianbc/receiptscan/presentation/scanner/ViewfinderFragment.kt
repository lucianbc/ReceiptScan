package com.lucianbc.receiptscan.presentation.scanner


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.lucianbc.receiptscan.R
import com.lucianbc.receiptscan.base.BaseFragment
import com.lucianbc.receiptscan.databinding.FragmentViewfinderBinding
import com.lucianbc.receiptscan.presentation.events.Event
import com.lucianbc.receiptscan.util.logd
import com.otaliastudios.cameraview.CameraListener
import com.otaliastudios.cameraview.Flash
import com.otaliastudios.cameraview.FrameProcessor
import com.otaliastudios.cameraview.PictureResult
import kotlinx.android.synthetic.main.fragment_viewfinder.*
import org.greenrobot.eventbus.EventBus
import javax.inject.Inject

class ViewfinderFragment :
    BaseFragment<ViewfinderViewModel>(ViewfinderViewModel::class.java) {

    @Inject
    lateinit var eventBus: EventBus

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initViewModel()
        return setupBinding(inflater, container)?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewFinder.setLifecycleOwner(viewLifecycleOwner)
        viewFinder.addFrameProcessor(frameProcessor)
        viewFinder.addCameraListener(cameraListener)
        observe(viewModel)
    }

    private fun setupBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentViewfinderBinding? {
        val binding = DataBindingUtil.inflate<FragmentViewfinderBinding>(
            inflater,
            R.layout.fragment_viewfinder,
            container,
            false
        )

        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        return binding
    }

    private fun observe(viewModel: ViewfinderViewModel) {
        viewModel.flash.observe(this, Observer {
            when (it) {
                true -> viewFinder.flash = Flash.TORCH
                false -> viewFinder.flash = Flash.OFF
            }
        })
        viewModel.ocrElements.observe(this, Observer {
            logd("Showing the elements")
        })
    }

    private val frameProcessor = FrameProcessor {
        viewModel.processFrame(it)
    }

    private val cameraListener = object : CameraListener() {
        override fun onPictureTaken(result: PictureResult) {
            eventBus.post(Event.PictureTaken(result))
        }
    }
}
