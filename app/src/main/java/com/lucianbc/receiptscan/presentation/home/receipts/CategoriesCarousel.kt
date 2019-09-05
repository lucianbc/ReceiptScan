package com.lucianbc.receiptscan.presentation.home.receipts

import android.content.Context
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
    override val snap: SnapHelper
        get() = LinearSnapHelper()

    override fun setPadding(recyclerView: RecyclerView) {
        val sidePadding = (recyclerView.width / 2) - recyclerView.getChildAt(0).width / 2
        recyclerView.setPadding(sidePadding, 0, sidePadding, 0)
    }

    override val decorator: RecyclerView.ItemDecoration?
        get() = null
}