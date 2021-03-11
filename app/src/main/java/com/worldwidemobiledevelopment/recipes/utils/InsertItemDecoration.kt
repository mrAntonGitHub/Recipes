package com.worldwidemobiledevelopment.recipes.utils

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.Dimension
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.xwray.groupie.GroupieViewHolder

/**
 * An ItemDecoration which applies an even visual padding on the left and right edges of a grid and
 * between each item, while also applying an even amount of inset to each item.  This ensures that
 * all items remain the same size.
 *
 * It assumes all items in a row have the same span size, and it assumes it's the only item
 * decorator.
 */
open class InsetItemDecoration(
    @ColorInt backgroundColor: Int,
    @Dimension padding: Int,
    private val key: String,
    private val value: String
) :
    ItemDecoration() {
    private val paint: Paint
    private val padding: Int
    private fun isInset(view: View, parent: RecyclerView): Boolean {
        val viewHolder = parent.getChildViewHolder(view) as GroupieViewHolder
        return if (viewHolder.extras.containsKey(key)) {
            viewHolder.extras[key] == value
        } else {
            false
        }
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        if (!isInset(view, parent)) return
        val layoutParams = view.layoutParams as GridLayoutManager.LayoutParams
        val gridLayoutManager = parent.layoutManager as GridLayoutManager?
        val spanSize = layoutParams.spanSize.toFloat()
        val totalSpanSize = gridLayoutManager!!.spanCount.toFloat()
        val n = totalSpanSize / spanSize // num columns
        val c = layoutParams.spanIndex / spanSize // column index
        val leftPadding = padding * ((n - c) / n) + 10
        val rightPadding = padding * ((c + 1) / n) + 10
        outRect.left = leftPadding.toInt()
        outRect.right = rightPadding.toInt()
        outRect.bottom = padding + 15
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val lm = parent.layoutManager
        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            if (!isInset(child, parent)) continue

            //
            if (child.translationX != 0f || child.translationY != 0f) {
                c.drawRect(
                    lm!!.getDecoratedLeft(child).toFloat(),
                    lm.getDecoratedTop(child).toFloat(),
                    lm.getDecoratedRight(child).toFloat(),
                    lm.getDecoratedBottom(child).toFloat(),
                    paint
                )
                continue
            }
            val isLast = i == childCount - 1
            val top = child.top + child.translationY
            val bottom = child.bottom + child.translationY

            // Left border
            c.drawRect(
                lm!!.getDecoratedLeft(child) + child.translationX,
                top,
                child.left + child.translationX,
                bottom,
                paint
            )
            var right = lm.getDecoratedRight(child) + child.translationX
            if (isLast) {
                right = Math.max(right, parent.width.toFloat())
            }

            // Right border
            c.drawRect(
                child.right + child.translationX,
                top,
                right,
                bottom,
                paint
            )

            // Bottom border
            c.drawRect(
                lm.getDecoratedLeft(child) + child.translationY,
                bottom,
                right,
                lm.getDecoratedBottom(child) + child.translationY,
                paint
            )
        }
    }

    init {
        paint = Paint()
        paint.color = backgroundColor
        this.padding = padding
    }
}