package com.lucianbc.receiptscan.widgets

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import com.lucianbc.receiptscan.R
import kotlinx.android.synthetic.main.sheet_item_layout.view.*

class SheetItem(context: Context, attrs: AttributeSet): ConstraintLayout(context, attrs) {
    init {
        inflate(context, R.layout.sheet_item_layout, this)
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.SheetItem)

        sheetItemIcon.setImageDrawable(attributes.getDrawable(R.styleable.SheetItem_image))
        sheetItemCaption.text = attributes.getText(R.styleable.SheetItem_text)

        attributes.recycle()
    }
}