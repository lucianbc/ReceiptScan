package com.lucianbc.receiptscan.presentation.home.receipts

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.lucianbc.receiptscan.domain.receipts.SpendingGroup
import com.lucianbc.receiptscan.presentation.components.CategoriesPositioning
import com.lucianbc.receiptscan.presentation.components.HorizontalCarousel
import com.lucianbc.receiptscan.presentation.components.washedToActiveTransform

class CategoriesCarousel(
    context: Context,
    attrs: AttributeSet
) : HorizontalCarousel(context, attrs) {

    private val peakPercent = 0.25f

    override val positioningStrategy = CategoriesPositioning(peakPercent)

    var onGroupChanged : ((SpendingGroup) -> Unit)? = null

    lateinit var adapter: CategoriesAdapter

    override val peak: Int
        get() = ((left + right).toFloat() * peakPercent).toInt()

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
//        washedToActiveTransform(child, gaussianFactor.toFloat(), context)
    }
}
