package com.lucianbc.receiptscan.presentation.home.receipts

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.lucianbc.receiptscan.presentation.components.Centering
import com.lucianbc.receiptscan.presentation.components.HorizontalCarousel
import com.lucianbc.receiptscan.presentation.components.washedToBlackTextTransform
import kotlinx.android.synthetic.main.home_currency_layout.view.*
import java.util.*

class CurrenciesCarousel(
    context: Context,
    attrs: AttributeSet
) : HorizontalCarousel(context, attrs) {

    override val positioningStrategy = Centering()

    var onCurrencyChanged : ((Currency) -> Unit)? = null

    lateinit var adapter : CurrenciesAdapter

    fun initialize() {
        adapter = CurrenciesAdapter()
        initialize(adapter)
        this.onSnapChanged = { p ->
            onCurrencyChanged?.let {
                adapter.getItemAt(p)?.let(it)
            }
        }
    }

    fun submitList(list: List<Currency>) {
        adapter.submitList(list)
        adapter.notifyDataSetChanged()
    }

    override fun applyTransform(child: View, gaussianFactor: Double) {
        washedToBlackTextTransform(
            child.currencyItemText,
            gaussianFactor.toFloat(),
            context
        )
    }
}