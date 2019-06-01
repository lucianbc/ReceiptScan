package com.lucianbc.receiptscan.presentation.scanner

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.databinding.DataBindingUtil.setContentView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.lucianbc.receiptscan.R
import com.lucianbc.receiptscan.base.BaseActivity
import com.lucianbc.receiptscan.databinding.ActivityScannerBinding
import com.lucianbc.receiptscan.presentation.DraftReviewActivity
import com.lucianbc.receiptscan.presentation.events.Event
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_scanner.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import javax.inject.Inject

class ScannerActivity :
    BaseActivity<ScannerViewModel>(ScannerViewModel::class.java),
    EasyPermissions.PermissionCallbacks {

    @Inject
    lateinit var eventBus: EventBus

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBinding()
        import_image.setOnClickListener(importImage)
        observe(viewModel)
        checkCameraPermissions()
    }

    private fun setupBinding() {
        val binding = setContentView<ActivityScannerBinding>(this, R.layout.activity_scanner)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
    }

    private fun observe(viewModel: ScannerViewModel) {
        viewModel.state.observe(this, Observer {
            when (it) {
                ScannerViewModel.State.NoPermission -> loadFragment(CameraPermissionFragment())
                ScannerViewModel.State.Allowed -> loadFragment(ViewfinderFragment())
                ScannerViewModel.State.Error -> loadFragment(ErrorFragment())
                ScannerViewModel.State.Processing -> loadFragment(ProcessingFragment())
            }
        })
    }

    override fun onStart() {
        super.onStart()
        eventBus.register(this)
    }

    override fun onStop() {
        super.onStop()
        eventBus.unregister(this)
    }

    @Subscribe
    fun onPictureTaken(event: Event.PictureTaken) =
        viewModel.process(event.pictureResult)

    @Subscribe
    fun onImageScanned(event: Event.ImageScanned) =
        goToDraftReview(event.draftId)

    @AfterPermissionGranted(CAMERA_PERMISSION_REQUEST)
    private fun checkCameraPermissions() {
        val perm = Manifest.permission.CAMERA
        if (EasyPermissions.hasPermissions(this, perm))
            viewModel.allowed()
        else
            viewModel.noPermission()
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
        startActivityForResult(intent,
            GALLERY_REQUEST
        )
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
        when (requestCode) {
            CAMERA_PERMISSION_REQUEST -> checkCameraPermissions()
            DRAFT_REVIEW_REQUEST -> checkCameraPermissions()
            GALLERY_REQUEST -> data?.let { callToScan(it) }
        }
    }

    private fun callToScan(intent: Intent) =
        intent.data?.let {
            val uri = it
            val bitmapObservable = Observable
                .fromCallable { MediaStore.Images.Media.getBitmap(contentResolver, uri) }
                .subscribeOn(Schedulers.io())
            viewModel.process(bitmapObservable)
        }

    private fun goToDraftReview(draftId: Long) {
        val intent = DraftReviewActivity.navIntent(this, draftId, true)
        startActivityForResult(intent,
            DRAFT_REVIEW_REQUEST
        )
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
