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

class CategoriesPositioning(private val centerPercent: Float) : HorizontalCarousel.PositioningStrategy {
    override val snap = StartSnapHelper()

    override fun setPadding(recyclerView: RecyclerView) {
        val ratio = centerPercent
        val fullWidth = recyclerView.width
        val childWidth = recyclerView.getChildAt(0)?.width ?: 0
        val left = (ratio * fullWidth - childWidth / 2).toInt()
        val right = fullWidth - (ratio * fullWidth + childWidth / 2).toInt()
        recyclerView.setPadding(left, 0, right, 0)
    }

    override val decorator = object : RecyclerView.ItemDecoration() {
        private val spacing = 20

        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            super.getItemOffsets(outRect, view, parent, state)
            if (parent.getChildAdapterPosition(view) != 0)
                outRect.left = spacing
            if (parent.getChildAdapterPosition(view) != parent.adapter?.itemCount?.minus(1) ?: -1)
                outRect.right = spacing
        }
    }
}