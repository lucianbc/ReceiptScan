package com.lucianbc.receiptscan.presentation.components

import android.animation.ArgbEvaluator
import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import com.lucianbc.receiptscan.R

private fun resolveColor(@ColorRes resId: Int, context: Context) = ContextCompat.getColor(context, resId)

fun washedToBlackTextTransform(
    textView: TextView,
    factor: Float,
    context: Context
) {
    val color = ArgbEvaluator().evaluate(
        factor,
        resolveColor(R.color.washed, context),
        resolveColor(R.color.black, context)
    ) as Int
    textView.setTextColor(color)
}

fun washedToActiveTransform (
    view: View,
    factor: Float,
    context: Context
) {
    val color = ArgbEvaluator().evaluate(
        factor,
        resolveColor(R.color.washed, context),
        Color.parseColor("#c8d4eb")
    ) as Int

    view.setBackgroundColor(color)
}