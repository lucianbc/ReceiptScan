package com.lucianbc.receiptscan.view.activity

import android.Manifest
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.lucianbc.receiptscan.R
import com.lucianbc.receiptscan.data.logd
import com.lucianbc.receiptscan.view.fragment.scanner.Error
import com.lucianbc.receiptscan.view.fragment.scanner.Permission
import com.lucianbc.receiptscan.view.fragment.scanner.Scanner
import com.lucianbc.receiptscan.viewmodel.Event
import com.lucianbc.receiptscan.viewmodel.scanner.ScannerViewModel
import dagger.android.support.DaggerAppCompatActivity
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions

class Scanner : DaggerAppCompatActivity(), EasyPermissions.PermissionCallbacks {
    private lateinit var viewModel: ScannerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scanner)
        viewModel = ViewModelProviders.of(this).get(ScannerViewModel::class.java)
        observe(viewModel)
        checkCameraPermissions()
    }

    private fun observe(viewModel: ScannerViewModel) {
        viewModel.state.observe(this, Observer {
            when (it) {
                ScannerViewModel.State.NoPermission -> loadPermissionScreen()
                ScannerViewModel.State.Allowed -> loadScannerScreen()
                ScannerViewModel.State.Error -> loadErrorScreen()
            }
        })
    }

    private fun loadErrorScreen() {
        replaceFragment(Error())
    }

    private fun loadPermissionScreen() {
        replaceFragment(Permission())
    }

    private fun loadScannerScreen() {
        replaceFragment(Scanner())
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
        logd("Importing image from activity")
    }

    //endregion

    //region Permission Management
    @AfterPermissionGranted(CAMERA_PERMISSION_REQUEST)
    private fun checkCameraPermissions() {
        if (EasyPermissions.hasPermissions(this, Manifest.permission.CAMERA))
            viewModel.state.value = ScannerViewModel.State.Allowed
        else
            viewModel.state.value = ScannerViewModel.State.NoPermission
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms))
            AppSettingsDialog.Builder(this).build().show()
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE)
            checkCameraPermissions()
    }
    //endregion

    companion object {
        const val CAMERA_PERMISSION_REQUEST = 100
    }
}
