package com.lucianbc.receiptscan.view.activity

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.lucianbc.receiptscan.R
import com.lucianbc.receiptscan.base.BaseActivity
import com.lucianbc.receiptscan.databinding.ActivityScannerBinding
import com.lucianbc.receiptscan.domain.model.ID
import com.lucianbc.receiptscan.util.logd
import com.lucianbc.receiptscan.view.fragment.scanner.Error
import com.lucianbc.receiptscan.view.fragment.scanner.Permission
import com.lucianbc.receiptscan.view.fragment.scanner.Processing
import com.lucianbc.receiptscan.view.fragment.scanner.Scanner
import com.lucianbc.receiptscan.viewmodel.Event
import com.lucianbc.receiptscan.viewmodel.scanner.ScannerViewModel
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions

class ScannerActivity :
    BaseActivity<ScannerViewModel>(ScannerViewModel::class.java),
    EasyPermissions.PermissionCallbacks {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scanner)
        setupBinding(viewModel)
        observe(viewModel)
        checkCameraPermission()
    }

    private fun setupBinding(vm: ScannerViewModel) {
        val binding = DataBindingUtil.setContentView<ActivityScannerBinding>(this, R.layout.activity_scanner)
        binding.viewModel = vm
    }

    private fun observe(viewModel: ScannerViewModel) {
        viewModel.state.observe(this, Observer {
            when (it) {
                ScannerViewModel.State.NoPermission -> replaceFragment(Permission())
                ScannerViewModel.State.Allowed -> replaceFragment(Scanner())
                ScannerViewModel.State.Error -> replaceFragment(Error())
                ScannerViewModel.State.Processing -> replaceFragment(Processing())
            }
        })
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.scanner_container, fragment)
            .commit()
    }

    //region Event Bus
    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe
    fun onImportImage(event: Event.ImportImage) {
        checkGalleryPermission()
    }

    @Subscribe
    fun onImageScanned(event: Event.ImageScanned) {
        goToDraftReview(event.draftId)
    }

    //endregion

    private fun goToDraftReview(draftId: ID) {
        val intent = DraftReviewActivity.navIntent(this, draftId)
        startActivityForResult(intent, DRAFT_REVIEW_REQUEST)
    }

    //region Permission Management

    @AfterPermissionGranted(CAMERA_PERMISSION_REQUEST)
    private fun checkCameraPermission() {
        if (EasyPermissions.hasPermissions(this, Manifest.permission.CAMERA))
            viewModel.state.value = ScannerViewModel.State.Allowed
        else
            viewModel.state.value = ScannerViewModel.State.NoPermission
    }

    @AfterPermissionGranted(STORAGE_PERMISSION_REQUEST)
    private fun checkGalleryPermission() {
        logd("Requesting gallery permission")
        val perm = Manifest.permission.READ_EXTERNAL_STORAGE
        if (EasyPermissions.hasPermissions(this, perm))
            browseGallery()
        else
            EasyPermissions.requestPermissions(
                this,
                getString(R.string.request_read_external_storage_msg),
                STORAGE_PERMISSION_REQUEST,
                perm
            )
    }

    private fun browseGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = IMAGE_INTENT_TYPE
        startActivityForResult(intent, GALLERY_REQUEST)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms))
            AppSettingsDialog.Builder(this)
                .setRequestCode(requestCode)
                .build()
                .show()
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAMERA_PERMISSION_REQUEST)
            checkCameraPermission()
        if (requestCode == DRAFT_REVIEW_REQUEST)
            checkCameraPermission()
        if (requestCode == GALLERY_REQUEST)
            data?.let { callToScan(data) }
    }

    private fun callToScan(intent: Intent) {
        val uri: Uri = intent.data!!

        val bitmapObservable =
            Observable
                .fromCallable { MediaStore.Images.Media.getBitmap(contentResolver, uri) }
                .subscribeOn(Schedulers.io())

        viewModel.scanImage(bitmapObservable)
    }

    //endregion

    companion object {
        const val CAMERA_PERMISSION_REQUEST = 100
        const val STORAGE_PERMISSION_REQUEST = 101
        const val GALLERY_REQUEST = 1000
        const val DRAFT_REVIEW_REQUEST = 1001
        const val IMAGE_INTENT_TYPE = "image/*"
    }
}
