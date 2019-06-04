package com.lucianbc.receiptscan.presentation.draft


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.toRectF
import androidx.lifecycle.Observer
import com.github.chrisbanes.photoview.OnPhotoTapListener
import com.lucianbc.receiptscan.R
import com.lucianbc.receiptscan.base.BaseFragment
import com.lucianbc.receiptscan.domain.model.Annotation
import com.lucianbc.receiptscan.presentation.service.rect
import kotlinx.android.synthetic.main.fragment_annotations.*


class AnnotationsFragment :
    BaseFragment<DraftReviewViewModel>(DraftReviewViewModel::class.java) {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initParentViewModel()
        return inflater.inflate(R.layout.fragment_annotations, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        receiptView.setOnClickListener {

        }
        observe(viewModel)
    }

    private fun observe(viewModel: DraftReviewViewModel) {
        viewModel.image.observe(viewLifecycleOwner, Observer {
            receiptView.setImageBitmap(it)
        })
        viewModel.annotations.observe(viewLifecycleOwner, Observer {
            receiptView.setOnPhotoTapListener(imageTapListener(it))
        })
    }

    private val imageTapListener: (List<Annotation>) -> OnPhotoTapListener = { annotations ->
        OnPhotoTapListener { view, x, y ->
            val imageWidth = view.drawable.intrinsicWidth
            val imageHeight = view.drawable.intrinsicHeight
            val xx = x * imageWidth
            val yy = y * imageHeight

            for (a in annotations) {
                if (a.rect.toRectF().contains(xx, yy)) {
                    onAnnotationTapped(a)
                    break
                }
            }
        }
    }

    private fun onAnnotationTapped(a: Annotation) {
        AnnotationDialog.open(
            childFragmentManager,
            AnnotationDialog.Arguments(a.text, a.tag),
            onAnnotationEdited(a)
        )
    }

    private val onAnnotationEdited: (Annotation) -> (AnnotationDialog.Arguments) -> Unit = { a -> { args ->
        val newAnnotation = a.copy(text = args.text, tag = args.tag)
        viewModel.editAnnotation(newAnnotation)
    } }
}
