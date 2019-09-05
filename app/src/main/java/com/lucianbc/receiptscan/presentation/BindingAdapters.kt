package com.lucianbc.receiptscan.presentation

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.lucianbc.receiptscan.domain.model.Category
import com.lucianbc.receiptscan.domain.extract.rules.show
import com.lucianbc.receiptscan.domain.receipts.SpendingGroup
import com.lucianbc.receiptscan.util.show
import java.util.*

@BindingAdapter("android:text")
fun setText(view: TextView, date: Date?) {
    view.text = date.show()
}

@BindingAdapter("android:src")
fun setIcon(view: ImageView, value: Category?) {
    value?.let { view.setImageResource(value.icon) }
}

@BindingAdapter("android:text")
fun setCategory(view: TextView, value: Category?) {
    value?.let { view.text = value.displayName }
}

@BindingAdapter("app:draftText")
fun setDraftCategory(view: TextView, value: Category?) {
    value?.let {
        when(it) {
            Category.NotAssigned -> "Tap to set category"
            else -> it.name
        }
    }?.let { view.text = it }
}

@BindingAdapter("android:text")
fun setCurrency(view: TextView, value: Currency?) {
    value?.let { view.text = value.currencyCode }
}

@BindingAdapter("app:displayCategory")
fun setCategoryName(view: TextView, value: SpendingGroup?) {
    value?.let { view.text = value.displayName }
}

@BindingAdapter("app:displayPrice")
fun setPrice(view: TextView, value: SpendingGroup?) {
    value?.let { view.text = value.total.show() }
}

@BindingAdapter("app:displayCurrency")
fun setCurrency(view: TextView, value: SpendingGroup?) {
    value?.let { view.text = value.currency.show() }
}
