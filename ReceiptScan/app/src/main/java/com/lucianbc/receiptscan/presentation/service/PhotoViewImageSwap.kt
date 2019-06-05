package com.lucianbc.receiptscan.presentation.service

import android.graphics.Bitmap
import android.graphics.Matrix
import com.github.chrisbanes.photoview.PhotoView

private fun Float.project(left: Float, right: Float): Float {
    return when {
        this in left..right -> this
        this < left -> left
        else -> right
    }
}

fun PhotoView.swapImageBitmap(image: Bitmap) {
    val matrix = Matrix()
    this.getDisplayMatrix(matrix)
    val zoom = this.scale.project(this.minimumScale, this.maximumScale)
    this.setImageBitmap(image)
    this.setDisplayMatrix(matrix)
    this.scale = zoom
}