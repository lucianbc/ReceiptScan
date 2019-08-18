package com.lucianbc.receiptscan.presentation.home.exports

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.lucianbc.receiptscan.R
import com.lucianbc.receiptscan.presentation.home.exports.form.FormActivity
import kotlinx.android.synthetic.main.fragment_export.*

class ExportFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_export, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createExportBtn.setOnClickListener {
            FormActivity.navIntent(activity!!).let(::startActivity)
        }
    }
}
