package com.lucianbc.receiptscan.presentation.components

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper

class Centering : HorizontalCarousel.PositioningStrategy {
    override val decorator: RecyclerView.ItemDecoration = SpaceDecorator()

    override val snap: SnapHelper
        get() = LinearSnapHelper()

    override fun setPadding(recyclerView: RecyclerView) {
        val sidePadding = (recyclerView.width / 2) - recyclerView.getChildAt(0).width / 2
        recyclerView.setPadding(sidePadding, 0, sidePadding, 0)
    }

    class SpaceDecorator : RecyclerView.ItemDecoration() {
        private val spacing = 150

        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            super.getItemOffsets(outRect, view, parent, state)
            outRect.right = spacing
            outRect.left = spacing
        }
    }
}
