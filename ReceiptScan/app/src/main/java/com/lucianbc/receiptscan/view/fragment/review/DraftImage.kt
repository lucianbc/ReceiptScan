package com.lucianbc.receiptscan.view.fragment.review


import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.github.chrisbanes.photoview.PhotoView
import com.lucianbc.receiptscan.R
import com.lucianbc.receiptscan.base.BaseFragment
import com.lucianbc.receiptscan.databinding.FragmentDraftImageBinding
import com.lucianbc.receiptscan.viewmodel.DraftReviewViewModel
import kotlinx.android.synthetic.main.fragment_draft_image.*

class DraftImage:
    BaseFragment<DraftReviewViewModel>(DraftReviewViewModel::class.java) {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initParentViewModel()

        val binding = DataBindingUtil.inflate<FragmentDraftImageBinding>(
            inflater,
            R.layout.fragment_draft_image,
            container,
            false
        )

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.image.observe(this, Observer {
            draft_photo_view.setImageBitmap(it)
        })
    }
}

@BindingAdapter("app:bitmap")
fun bindImage(view: PhotoView, image: Bitmap?) {
    view.setImageBitmap(image)
}
