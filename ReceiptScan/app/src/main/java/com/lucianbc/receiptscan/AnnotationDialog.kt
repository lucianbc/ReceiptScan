package com.lucianbc.receiptscan

import android.graphics.Point
import android.os.Bundle
import android.os.Parcelable
import android.view.*
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.FragmentManager
import com.lucianbc.receiptscan.domain.model.Tag
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.annotation_dialog_layout.*

class AnnotationDialog : AppCompatDialogFragment() {
    var callback: ((Arguments) -> Unit)? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.annotation_dialog_layout, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cancelBtn.setOnClickListener { dismiss() }
        saveBtn.setOnClickListener { dismiss() }
        loadSpinner()
        loadArgs()
    }

    override fun onResume() {
        super.onResume()
        val window = dialog?.window
        val display = window?.windowManager?.defaultDisplay
        val size = Point()
        display?.getSize(size)
        window?.setLayout(size.x, WindowManager.LayoutParams.WRAP_CONTENT)
        window?.setGravity(Gravity.CENTER)
    }

    private fun loadArgs() {
        arguments?.apply {
            val args = getParcelable<Arguments>(ARGS)
            args?.apply {
                annotationText.setText(this.text)
                annotationType.setSelection(this.tag.ordinal, false)
                callback?.also { cb ->
                    saveBtn.setOnClickListener { cb.invoke(fromUi()); dismiss() }
                }
            }
        }
    }

    private fun fromUi(): Arguments {
        @Suppress("UNCHECKED_CAST")
        val tag = (annotationType.adapter as ArrayAdapter<Tag>).getItem(annotationType.selectedItemPosition)!!
        return Arguments(annotationText.text.toString(), tag)
    }

    private fun loadSpinner() {
        annotationType.adapter = ArrayAdapter<Tag>(
            context!!, android.R.layout.simple_spinner_item, Tag.values()
        )
    }


    companion object {
        fun open(fragmentManager: FragmentManager, args: Arguments) {
            val dialog = AnnotationDialog()
            val bundle = Bundle()
            bundle.putParcelable(ARGS, args)
            dialog.arguments = bundle
            dialog.show(fragmentManager, "ANNOTATION_DIALOG")
        }
        private const val ARGS = "ARGS"
    }

    @Parcelize
    data class Arguments (
        val text: String,
        val tag: Tag
    ): Parcelable
}