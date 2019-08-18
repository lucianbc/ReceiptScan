package com.lucianbc.receiptscan.presentation.home.exports.form

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.lucianbc.receiptscan.R
import kotlinx.android.synthetic.main.fragment_form_container.*


class FormContainerFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_form_container, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = OptionsAdapter(fragmentManager!!)
        exportOptionsPager.adapter = adapter

        wormDotsIndicator.setViewPager(exportOptionsPager)
    }
}
