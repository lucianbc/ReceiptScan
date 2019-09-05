package com.lucianbc.receiptscan.presentation.home.receipts

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.lucianbc.receiptscan.domain.receipts.SpendingGroup
import com.lucianbc.receiptscan.presentation.components.CategoriesPositioning
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
