package com.lucianbc.receiptscan.presentation.draft


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.toRectF
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.github.chrisbanes.photoview.OnPhotoTapListener
import com.lucianbc.receiptscan.R
import com.lucianbc.receiptscan.domain.model.Annotation
import com.lucianbc.receiptscan.presentation.service.rect
import com.lucianbc.receiptscan.presentation.service.swapImageBitmap
import com.lucianbc.receiptscan.util.logd
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_annotations.*
import javax.inject.Inject


class AnnotationsFragment : DaggerFragment() {
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
        logd("ViewModel in annotations fragment: ${viewModel.hashCode()}")
    }

    private fun observe(viewModel: DraftReviewViewModel) {
        viewModel.image.observe(viewLifecycleOwner, Observer {
            receiptView.swapImageBitmap(it)
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
