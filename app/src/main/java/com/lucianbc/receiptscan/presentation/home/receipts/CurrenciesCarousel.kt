package com.lucianbc.receiptscan.presentation.home.receipts

import android.animation.ArgbEvaluator
import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import com.lucianbc.receiptscan.R
import com.lucianbc.receiptscan.presentation.components.HorizontalCarousel
import kotlinx.android.synthetic.main.home_currency_layout.view.*
import java.util.*

class CurrenciesCarousel(
    context: Context,
    attrs: AttributeSet
) : HorizontalCarousel(context, attrs) {

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

    fun submitList(list: List<Currency>) = adapter.submitList(list)

    override fun applyTransform(child: View, gaussianFactor: Double) {
        fun resolveColor(@ColorRes resId: Int) = ContextCompat.getColor(context, resId)

        val color = ArgbEvaluator().evaluate(
            gaussianFactor.toFloat(),
            resolveColor(R.color.washed),
            resolveColor(R.color.black)
        ) as Int
        child.currencyItemText.setTextColor(color)
    }
}