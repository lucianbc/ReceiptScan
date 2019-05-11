package com.lucianbc.receiptscan.view.fragment.scanner


import android.Manifest
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders

import com.lucianbc.receiptscan.R
import com.lucianbc.receiptscan.view.activity.ScannerActivity
import com.lucianbc.receiptscan.viewmodel.scanner.ScannerViewModel
import kotlinx.android.synthetic.main.fragment_scanner_permission.*
import pub.devrel.easypermissions.EasyPermissions

class Permission : Fragment() {

    private lateinit var viewModel: ScannerViewModel

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(activity!!).get(ScannerViewModel::class.java)
        allow_camera_btn.setOnClickListener(requestPermission)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_scanner_permission, container, false)
    }

    private val requestPermission = View.OnClickListener {
        EasyPermissions.requestPermissions(
            activity!!,
            getString(R.string.request_camera_msg),
            ScannerActivity.CAMERA_PERMISSION_REQUEST,
            Manifest.permission.CAMERA
        )
    }
}
