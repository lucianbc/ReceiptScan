package com.lucianbc.receiptscan.view.activity

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.lucianbc.receiptscan.R
import com.lucianbc.receiptscan.databinding.ActivityScannerBinding
import com.lucianbc.receiptscan.util.logd
import com.lucianbc.receiptscan.view.fragment.scanner.Error
import com.lucianbc.receiptscan.view.fragment.scanner.Permission
import com.lucianbc.receiptscan.view.fragment.scanner.Scanner
import com.lucianbc.receiptscan.viewmodel.Event
import com.lucianbc.receiptscan.viewmodel.scanner.ScannerViewModel
import dagger.android.support.DaggerAppCompatActivity
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import javax.inject.Inject

class ScannerActivity : DaggerAppCompatActivity(), EasyPermissions.PermissionCallbacks {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory // = LiveViewVM.Factory(EventBus.getDefault())

    private lateinit var viewModel: ScannerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scanner)
        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(ScannerViewModel::class.java)
        setupBinding()
        observe(viewModel)
        checkCameraPermission()
    }

    private fun setupBinding() {
        val binding = DataBindingUtil.setContentView<ActivityScannerBinding>(this, R.layout.activity_scanner)
        binding.viewModel = viewModel
    }

    private fun observe(viewModel: ScannerViewModel) {
        viewModel.state.observe(this, Observer {
            when (it) {
                ScannerViewModel.State.NoPermission -> replaceFragment(Permission())
                ScannerViewModel.State.Allowed -> replaceFragment(Scanner())
                ScannerViewModel.State.Error -> replaceFragment(Error())
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
    @Suppress("unused")
    fun onImportImage(@Suppress("UNUSED_PARAMETER")event: Event.ImportImage) {
        logd("Importing image")
        checkGalleryPermission()
    }
    //endregion

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
        if (requestCode == GALLERY_REQUEST)
            callToScan(data!!)
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
        const val IMAGE_INTENT_TYPE = "image/*"
    }
}
