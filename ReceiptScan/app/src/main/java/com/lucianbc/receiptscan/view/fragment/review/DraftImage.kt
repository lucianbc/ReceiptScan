package com.lucianbc.receiptscan.view.fragment.review


import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import com.github.chrisbanes.photoview.PhotoView
import com.lucianbc.receiptscan.R
import com.lucianbc.receiptscan.base.BaseFragment
import com.lucianbc.receiptscan.databinding.FragmentDraftImageBinding
import com.lucianbc.receiptscan.domain.model.ScanInfoBox
import com.lucianbc.receiptscan.viewmodel.DraftReviewViewModel
import kotlinx.android.synthetic.main.fragment_draft_image.*

class DraftImage:
    BaseFragment<DraftReviewViewModel>(DraftReviewViewModel::class.java) {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initParentViewModel()
        return setupBinding(inflater, container).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        draft_photo_view.setOnPhotoTapListener { _, x, y -> viewModel.imageTapped(x, y, this::showElementDetails) }
    }

    private fun showElementDetails(box: ScanInfoBox) {
        val dialog = ScanInfoBoxDialog.create(ScanInfoBoxDialog.Arguments.from(box))
        fragmentManager?.apply {
            dialog.show(this, "scan_info_box_fragment")
        }
    }

    private fun setupBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentDraftImageBinding {
        val binding = DataBindingUtil.inflate<FragmentDraftImageBinding>(
            inflater,
            R.layout.fragment_draft_image,
            container,
            false
        )

        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        return binding
    }
}

@BindingAdapter("app:bitmap")
fun bindImage(view: PhotoView, image: Bitmap?) {
    view.setImageBitmap(image)
}
