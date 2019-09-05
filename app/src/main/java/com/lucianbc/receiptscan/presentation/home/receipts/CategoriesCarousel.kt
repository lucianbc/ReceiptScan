package com.lucianbc.receiptscan.presentation.home.receipts

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.lucianbc.receiptscan.domain.receipts.SpendingGroup
import com.lucianbc.receiptscan.presentation.components.HorizontalCarousel

class CategoriesCarousel(
    context: Context,
    attrs: AttributeSet
) : HorizontalCarousel(context, attrs) {

    override val positioningStrategy = CategoriesPositioning()

    var onGroupChanged : ((SpendingGroup) -> Unit)? = null

    lateinit var adapter: CategoriesAdapter

    fun initialize() {
        adapter = CategoriesAdapter()
        initialize(adapter)
        this.onSnapChanged = { p ->
            onGroupChanged?.let {
                adapter.getItemAt(p)?.let(it)
            }
        }
    }

    fun submitList(list: List<SpendingGroup>) = adapter.submitList(list)

    override fun applyTransform(child: View, gaussianFactor: Double) {
    }
}

class CategoriesPositioning : HorizontalCarousel.PositioningStrategy {
    override val snap = null

    override fun setPadding(recyclerView: RecyclerView) {
        val ratio = 0.1
        val fullWidth = recyclerView.width
        val left = (ratio * fullWidth).toInt()
        val childWidth = recyclerView.getChildAt(0)?.width ?: 0
        val right = fullWidth - left - childWidth
        recyclerView.setPadding(left, 0, right, 0)
    }

    override val decorator = object : RecyclerView.ItemDecoration() {
        private val spacing = 40

        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            super.getItemOffsets(outRect, view, parent, state)
            if (parent.getChildAdapterPosition(view) != 0)
                outRect.left = spacing
            if (parent.getChildAdapterPosition(view) != parent.adapter?.itemCount?.minus(1) ?: -1)
                outRect.right = spacing
        }
    }
}