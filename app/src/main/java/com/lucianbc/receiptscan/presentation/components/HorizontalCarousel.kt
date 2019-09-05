package com.lucianbc.receiptscan.presentation.components

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import kotlin.math.pow

abstract class HorizontalCarousel(
    context: Context,
    attrs: AttributeSet
) : RecyclerView(context, attrs) {

    abstract val positioningStrategy: PositioningStrategy

    open protected val magnitude = 1f

    open protected val spread
        get() = width / 4

    private val center
        get() = (left + right) / 2

    var onSnapChanged : ((Int) -> Unit)? = null

    private val snapHelper by lazy { positioningStrategy.snap }

    open protected val reversed = false

    protected fun <T : ViewHolder> initialize(newAdapter: Adapter<T>) {
        clipToPadding = false
        overScrollMode = View.OVER_SCROLL_NEVER
        positioningStrategy.decorator?.let(::addItemDecoration)
        snapHelper?.attachToRecyclerView(this)
        layoutManager = LinearLayoutManager(context, HORIZONTAL, reversed)
        newAdapter.registerAdapterDataObserver(object : AdapterDataObserver() {
            override fun onChanged() {
                super.onChanged()
                post {
                    positioningStrategy.setPadding(this@HorizontalCarousel)
                    smoothScrollToPosition(0)
                    snapHelper?.getSnapPosition()?.let(::updateSnapPosition)
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
            snapHelper?.getSnapPosition()?.let { pos ->
                if (updateSnapPosition(pos)) {
                    it(pos)
                }
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

    interface PositioningStrategy {
        val snap: SnapHelper?
        fun setPadding(recyclerView: RecyclerView)
        val decorator : ItemDecoration?
    }
}
