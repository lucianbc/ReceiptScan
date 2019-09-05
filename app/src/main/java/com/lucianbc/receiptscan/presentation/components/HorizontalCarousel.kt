package com.lucianbc.receiptscan.presentation.components

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import kotlin.math.pow

abstract class HorizontalCarousel(
    context: Context,
    attrs: AttributeSet
) : RecyclerView(context, attrs) {

    protected val magnitude = 1f

    protected val spread
        get() = width / 4

    protected val spacing = 150

    private val center
        get() = (left + right) / 2

    var onSnapChanged : ((Int) -> Unit)? = null

    protected val snapHelper = LinearSnapHelper()

    protected val reversed = false

    protected fun <T : ViewHolder> initialize(newAdapter: Adapter<T>) {
        clipToPadding = false
        addItemDecoration(SpaceDecorator())
        snapHelper.attachToRecyclerView(this)
        layoutManager = LinearLayoutManager(context, HORIZONTAL, reversed)
        newAdapter.registerAdapterDataObserver(object : AdapterDataObserver() {
            override fun onChanged() {
                super.onChanged()
                post {
                    val sidePadding = (width / 2) - getChildAt(0).width / 2
                    setPadding(sidePadding, 0, sidePadding, 0)
                    smoothScrollToPosition(0)
                    snapHelper.getSnapPosition().let(::updateSnapPosition)
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
        post {
            (0 until childCount).forEach { position ->
                val child = getChildAt(position)
                val childCenterX = (child.left + child.right) / 2
                val gaussianFactor = gaussianFactor(childCenterX)
                applyTransform(child, gaussianFactor)
            }
        }
    }

    abstract fun applyTransform(child: View, gaussianFactor: Double)

    private var lastPosition = NO_POSITION

    override fun onScrollStateChanged(state: Int) {
        super.onScrollStateChanged(state)
        if (state == SCROLL_STATE_IDLE) {
            maybeSnapChanged()
        }
    }

    private fun maybeSnapChanged() {
        onSnapChanged?.let {
            val snapPosition = snapHelper.getSnapPosition()
            if (updateSnapPosition(snapPosition)) {
                it(snapPosition)
            }
        }
    }

    private fun updateSnapPosition(newSnap: Int): Boolean {
        if (newSnap != lastPosition && newSnap != NO_POSITION) {
            lastPosition = newSnap
            return true
        }
        return false
    }

    private fun SnapHelper.getSnapPosition(): Int {
        return findSnapView(layoutManager)
            ?.let { layoutManager?.getPosition(it) }
            ?: NO_POSITION
    }

    private fun gaussianFactor(x: Int) =
        magnitude * Math.E.pow(-(x - center).pow(2) / (2 * spread * spread).toDouble())

    private fun Int.pow(p: Int) = toDouble().pow(p)

    inner class SpaceDecorator : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: State) {
            super.getItemOffsets(outRect, view, parent, state)
            outRect.right = spacing
            outRect.left = spacing
        }
    }
}
