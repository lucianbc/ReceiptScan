package com.lucianbc.receiptscan.presentation.components

import android.animation.ArgbEvaluator
import android.content.Context
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import com.lucianbc.receiptscan.R

fun washedToBlackTextTransform(
    textView: TextView,
    factor: Float,
    context: Context
) {
    fun resolveColor(@ColorRes resId: Int) = ContextCompat.getColor(context, resId)
    val color = ArgbEvaluator().evaluate(
        factor,
        resolveColor(R.color.washed),
        resolveColor(R.color.black)
    ) as Int
    textView.setTextColor(color)
}