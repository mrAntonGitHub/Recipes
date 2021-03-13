package com.worldwidemobiledevelopment.recipes.adapters

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.worldwidemobiledevelopment.recipes.R

class RecyclerItemDecoration(var context: Context, var headerOffset: Int, var sticky: Boolean, callback: SectionCallback) : ItemDecoration() {
    var sectionCallback: SectionCallback = callback
    var headerView: View? = null
    var tvTitle: TextView? = null
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val pos = parent.getChildAdapterPosition(view)
        if (sectionCallback.isSection(pos)) {
            outRect.top = headerOffset
        }
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)
        if (headerView == null) {
            headerView = inflateHeader(parent)
            tvTitle = headerView!!.findViewById<View>(R.id.text_header_name) as TextView
            fixLayoutSize(headerView, parent)
        }
        var prevTitle = ""
        for (i in 0 until parent.childCount) {
            val child = parent.getChildAt(i)
            val childPos = parent.getChildAdapterPosition(child)
            val title: String = sectionCallback.getSectionHeaderName(childPos).toString()
            tvTitle!!.text = title
            if (!prevTitle.equals(
                    title,
                    ignoreCase = true
                ) || sectionCallback.isSection(childPos)
            ) {
                drawHeader(c, child, headerView)
                prevTitle = title
            }
        }
    }

    private fun drawHeader(c: Canvas, child: View, headerView: View?) {
        c.save()
        if (sticky) {
            c.translate(0f, Math.max(0, child.top - headerView!!.height).toFloat())
        } else {
            c.translate(0f, child.top - headerView!!.height.toFloat())
        }
        headerView.draw(c)
        c.restore()
    }

    fun fixLayoutSize(view: View?, viewGroup: ViewGroup) {
        val widthSpec = View.MeasureSpec.makeMeasureSpec(viewGroup.width, View.MeasureSpec.EXACTLY)
        val heightSpec =
            View.MeasureSpec.makeMeasureSpec(viewGroup.height, View.MeasureSpec.UNSPECIFIED)
        val childWidth = ViewGroup.getChildMeasureSpec(
            widthSpec,
            viewGroup.paddingLeft + viewGroup.paddingRight,
            view!!.layoutParams.width
        )
        val childHeight = ViewGroup.getChildMeasureSpec(
            heightSpec,
            viewGroup.paddingTop + viewGroup.paddingBottom,
            view.layoutParams.height
        )
        view.measure(childWidth, childHeight)
        view.layout(0, 0, view.measuredWidth, view.measuredHeight)
    }

    private fun inflateHeader(recyclerView: RecyclerView): View {
        return LayoutInflater.from(context)
            .inflate(R.layout.item_section_header, recyclerView, false)
    }

    interface SectionCallback {
        fun isSection(pos: Int): Boolean
        fun getSectionHeaderName(pos: Int): String?
    }

}