package com.lucianbc.receiptscan.presentation.draft


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.lucianbc.receiptscan.R
import com.lucianbc.receiptscan.domain.model.Tag
import kotlinx.android.synthetic.main.fragment_annotations.*


class AnnotationsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_annotations, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        receiptView.setOnClickListener {
            AnnotationDialog.open(
                childFragmentManager,
                AnnotationDialog.Arguments("Text", Tag.Noise)
            )
//            AlertDialog.Builder(activity!!)
//                .setMessage("Mesaj")
//                .show()
        }
    }
}
