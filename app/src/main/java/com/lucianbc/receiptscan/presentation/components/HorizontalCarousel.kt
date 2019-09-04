package com.lucianbc.receiptscan.presentation.components

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.pow

class HorizontalCarousel(
    context: Context,
    attrs: AttributeSet
) : RecyclerView(context, attrs) {

    fun <T: ViewHolder> initialize(newAdapter: Adapter<T>) {
        clipToPadding = false
        addItemDecoration(SpaceDecorator())
        layoutManager = LinearLayoutManager(context, HORIZONTAL, false)
        newAdapter.registerAdapterDataObserver(object: AdapterDataObserver() {
            override fun onChanged() {
                super.onChanged()
                post {
                    val sidePadding = (width / 2) - getChildAt(0).width / 2
                    setPadding(sidePadding, 0, sidePadding, 0)
                    smoothScrollToPosition(0)
                    addOnScrollListener(object : OnScrollListener() {
                        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                            super.onScrolled(recyclerView, dx, dy)
                            onScrollChanged()
                        }
                    })
                }
            }
        })
        adapter = newAdapter
        newAdapter.notifyDataSetChanged()
    }

    private fun onScrollChanged() {
    }

    private fun getGaussianScale(
        childCenterX: Int,
        minScaleOffset: Float,
        scaleFactor: Float,
        spreadFactor: Double
    ): Float {
        val recyclerCenterX = (left + right) / 2
        return (Math.E.pow(
            -(childCenterX - recyclerCenterX.toDouble()).pow(2.toDouble()) / (2 * spreadFactor.pow(
                2.toDouble()
            ))
        ) * scaleFactor + minScaleOffset).toFloat()
    }

    inner class SpaceDecorator : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: State) {
            super.getItemOffsets(outRect, view, parent, state)
            if (parent.getChildAdapterPosition(view) != 0) {
                outRect.left = 150
            }
        }
    }
}