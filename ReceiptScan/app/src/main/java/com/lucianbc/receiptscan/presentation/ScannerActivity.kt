package com.lucianbc.receiptscan.presentation

import android.Manifest
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.lucianbc.receiptscan.R
import kotlinx.android.synthetic.main.activity_scanner.*
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions

class ScannerActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scanner)
        checkCameraPermissions()
        import_image.setOnClickListener(importImage)
    }

    @AfterPermissionGranted(CAMERA_PERMISSION_REQUEST)
    private fun checkCameraPermissions() {
        val perm = Manifest.permission.CAMERA
        if (EasyPermissions.hasPermissions(this, perm))
            loadFragment(ViewfinderFragment())
        else
            loadFragment(CameraPermissionFragment())
    }

    @AfterPermissionGranted(STORAGE_PERMISSION_REQUEST)
    private fun importWithPermission() {
        val perm = Manifest.permission.READ_EXTERNAL_STORAGE
        if (EasyPermissions.hasPermissions(this, perm))
            browseGallery()
        else
            EasyPermissions.requestPermissions(
                this,
                getString(R.string.request_storage_msg),
                STORAGE_PERMISSION_REQUEST,
                perm
            )
    }

    private fun browseGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = IMAGE_INTENT_TYPE
        startActivityForResult(intent, GALLERY_REQUEST)
    }

    private fun loadFragment(fragment: Fragment) =
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.scanner_container, fragment)
            .commit()

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms))
            AppSettingsDialog.Builder(this)
                .setRequestCode(requestCode)
                .build()
                .show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAMERA_PERMISSION_REQUEST)
            checkCameraPermissions()
        if (requestCode == GALLERY_REQUEST)
            data?.let { callToScan(it) }
    }

    private fun callToScan(it: Intent) {
        goToDraftReview()
    }

    private fun goToDraftReview() {
        val randomId = -1L
        val intent = DraftReviewActivity.navIntent(this, randomId, true)
        startActivityForResult(intent, DRAFT_REVIEW_REQUEST)
    }

    private val importImage = View.OnClickListener {
        importWithPermission()
    }

    companion object {
        const val CAMERA_PERMISSION_REQUEST = 100
        const val STORAGE_PERMISSION_REQUEST = 101

        const val GALLERY_REQUEST = 1000
        const val DRAFT_REVIEW_REQUEST = 1001

        const val IMAGE_INTENT_TYPE = "image/*"

        fun navIntent(context: Context): Intent =
            Intent(context, ScannerActivity::class.java)
    }
}
