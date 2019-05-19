package com.lucianbc.receiptscan.view.fragment.review


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.github.chrisbanes.photoview.OnPhotoTapListener
import com.lucianbc.receiptscan.R
import com.lucianbc.receiptscan.base.BaseFragment
import com.lucianbc.receiptscan.domain.model.ScanInfoBox
import com.lucianbc.receiptscan.domain.repository.DraftWithImage
import com.lucianbc.receiptscan.domain.repository.draft
import com.lucianbc.receiptscan.domain.repository.image
import com.lucianbc.receiptscan.util.logd
import com.lucianbc.receiptscan.view.fragment.scanner.widget.boundingBoxF
import com.lucianbc.receiptscan.viewmodel.DraftReviewViewModel
import kotlinx.android.synthetic.main.fragment_draft_image.*

class DraftImage:
    BaseFragment<DraftReviewViewModel>(DraftReviewViewModel::class.java) {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initParentViewModel()
        return inflater.inflate(R.layout.fragment_draft_image, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observe(viewModel)
    }

    private fun observe(vm: DraftReviewViewModel) {
        vm.singleImageDraft.observe(this, Observer {
            draft_photo_view.setImageBitmap(it.image)
            draft_photo_view.setOnPhotoTapListener(imageTapListener(it))
        })
    }

    private fun onElementTapped(box: ScanInfoBox) {
        logd("Tapped on $box")
    }

    private val imageTapListener: (DraftWithImage) -> OnPhotoTapListener = { receipt ->
        OnPhotoTapListener { _, x, y ->
            val xx: Float = receipt.image.width * x
            val yy: Float = receipt.image.height * y

            for (box in receipt.draft.annotations) {
                if (box.boundingBoxF.contains(xx, yy)) {
                    onElementTapped(box)
                    break
                }
            }
        }
    }
}
