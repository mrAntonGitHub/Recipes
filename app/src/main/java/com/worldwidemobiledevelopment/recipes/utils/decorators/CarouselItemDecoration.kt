package com.worldwidemobiledevelopment.recipes.utils.decorators

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.annotation.ColorInt
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration

class CarouselItemDecoration(@ColorInt backgroundColor: Int, paddingPixelSize: Int) :
    ItemDecoration() {
    private val grayBackgroundPaint: Paint = Paint()
    private val padding: Int
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.right = padding
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val childCount = parent.childCount
        val lm = parent.layoutManager
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            var right = (lm!!.getDecoratedRight(child) + child.translationX).toInt()
            if (i == childCount - 1) {
                // Last item
                right = Math.max(right, parent.width)
            }

            // Right border
            c.drawRect(
                child.right + child.translationX,
                0f,
                right.toFloat(),
                parent.height.toFloat(),
                grayBackgroundPaint
            )
        }
    }

    init {
        grayBackgroundPaint.color = backgroundColor
        padding = paddingPixelSize
    }
}