package com.lucianbc.receiptscan


import android.Manifest
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_camera_permission.*
import pub.devrel.easypermissions.EasyPermissions


class CameraPermissionFragment : Fragment() {
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        allowCameraBtn.setOnClickListener(requestPermission)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_camera_permission, container, false)
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
