package com.lucianbc.receiptscan.presentation

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.lucianbc.receiptscan.domain.model.Category
import com.lucianbc.receiptscan.domain.scanner.show
import java.util.*

@BindingAdapter("android:text")
fun setText(view: TextView, date: Date?) {
    view.text = date.show()
}

@BindingAdapter("android:src")
fun setIcon(view: ImageView, value: Category?) {
    value?.let { view.setImageResource(value.icon) }
}