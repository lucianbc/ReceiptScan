package com.lucianbc.receiptscan.view.fragment.scanner.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.lucianbc.receiptscan.domain.model.ScanInfoBox
import com.lucianbc.receiptscan.util.Just
import com.lucianbc.receiptscan.util.None
import com.lucianbc.receiptscan.util.Optional
import com.otaliastudios.cameraview.Facing

class GraphicOverlay (
    context: Context,
    attrs: AttributeSet
    ): View(context, attrs) {

    lateinit var graphicPresenter: GraphicPresenter
        private set

    fun setCameraInfo(previewWidth: Int, previewHeight: Int, facing: Facing) {
        synchronized(this) {
            graphicPresenter = GraphicPresenter(previewWidth, previewHeight, facing)
            postInvalidate()
        }
    }

    fun setGraphics(graphics: Sequence<GraphicPresenter.Graphic>) {
        synchronized(this) {
            graphicPresenter.graphics.clear()
            graphicPresenter.graphics.addAll(graphics)
            postInvalidate()
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
//        Log.d("GRAPHIC OVERLAY", "Drawing overlay")
        synchronized(this) {
            graphicPresenter.render(canvas!!)
        }
    }
}


class GraphicPresenter(
    private val width: Int,
    private val height: Int,
    private val facing: Facing = Facing.BACK
) {
    private var widthScaleFactor = 1.0f
    private var heightScaleFactor = 1.0f

    val graphics: MutableSet<Graphic> = HashSet()

    abstract class Graphic(private val presenter: GraphicPresenter) {
        abstract fun draw(canvas: Canvas)
        abstract fun contains(x: Float, y: Float): Boolean

        fun scaleX(horizontal: Float) = horizontal * presenter.widthScaleFactor
        fun scaleY(vertical: Float) = vertical * presenter.heightScaleFactor
        fun translateX(x: Float) =
            if (presenter.facing == Facing.BACK) scaleX(x)
            else presenter.width - scaleX(x)
        fun translateY(y: Float) =
            scaleY(y)
    }

    fun graphicAtLocation(x: Float, y: Float): Optional<Graphic> {
        for (g in graphics) {
            if (g.contains(x, y))
                return Just(g)
        }
        return None
    }

    fun render(canvas: Canvas) {
        if (width != 0 && height != 0) {
            widthScaleFactor = canvas.width.toFloat() / width
            heightScaleFactor = canvas.height.toFloat() / height
        }

        for (g in graphics) {
            g.draw(canvas)
        }
    }
}

typealias TextElement = ScanInfoBox

class OcrGraphic (
    presenter: GraphicPresenter,
    private val element: TextElement
): GraphicPresenter.Graphic(presenter) {

    companion object {
        private const val TEXT_COLOR = Color.WHITE
        private val rectPaint = Paint()
        private val textPaint = Paint()

        init {
            rectPaint.color = TEXT_COLOR
            rectPaint.style = Paint.Style.STROKE
            rectPaint.strokeWidth = 4f

            textPaint.color = TEXT_COLOR
            textPaint.textSize = 54f
        }
    }

    override fun draw(canvas: Canvas) {
        val rect = rect(element)
        canvas.drawRect(rect, rectPaint)

        canvas.drawText(element.text, rect.left, rect.bottom, textPaint)
    }

    override fun contains(x: Float, y: Float): Boolean {
        val rect = rect(element)
        return rect.left < x && rect.right > x && rect.top < y && rect.bottom > y
    }

    private fun rect(element: TextElement): RectF {
        val rect = RectF(element.boundingBox)
        rect.left = translateX(rect.left)
        rect.top = translateY(rect.top)
        rect.right = translateX(rect.right)
        rect.bottom = translateY(rect.bottom)

        return rect
    }
}

val ScanInfoBox.boundingBox: Rect
    get() = Rect(this.left, this.top, this.right, this.bottom)

