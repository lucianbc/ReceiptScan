package com.lucianbc.receiptscan.presentation.home.receipts

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.lucianbc.receiptscan.presentation.components.Centering
import com.lucianbc.receiptscan.presentation.components.HorizontalCarousel
import com.lucianbc.receiptscan.presentation.components.washedToBlackTextTransform
import kotlinx.android.synthetic.main.home_month_layout.view.*
import java.util.*

class MonthsCarousel (
    context: Context,
    attrs: AttributeSet
) : HorizontalCarousel(context, attrs) {

    override val positioningStrategy = Centering()

    var onMonthChanged : ((Date) -> Unit)? = null

    lateinit var adapter : MonthsAdapter

    fun initialize() {
        adapter = MonthsAdapter()
        initialize(adapter)
        this.onSnapChanged = { p ->
            onMonthChanged?.let {
                adapter.getItemAt(p)?.let(it)
            }
        }
    }

    fun submitList(list: List<Date>) {
        adapter.submitList(list)
        adapter.notifyDataSetChanged()
    }

    override fun applyTransform(child: View, gaussianFactor: Double) {
        washedToBlackTextTransform(
            child.monthItemText,
            gaussianFactor.toFloat(),
            context
        )
    }
}