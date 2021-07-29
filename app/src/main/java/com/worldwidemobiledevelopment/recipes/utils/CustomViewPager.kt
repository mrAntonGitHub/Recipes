package com.worldwidemobiledevelopment.recipes.utils

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.viewpager.widget.ViewPager


class CustomViewPager : ViewPager {
    var canSwipe = true

    constructor(context: Context) : super(context) {}
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {}

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var heightMeasure = heightMeasureSpec
        val child: View? = getChildAt(currentItem)
        if (child != null) {
            child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED))
            val h: Int = child.measuredHeight
            heightMeasure = MeasureSpec.makeMeasureSpec(h, MeasureSpec.EXACTLY)
        }
        super.onMeasure(widthMeasureSpec, heightMeasure)
    }

    fun canSwipe(canSwipe: Boolean) {
        this.canSwipe = canSwipe
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return if (canSwipe) {
            super.onTouchEvent(event)
        } else false
    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        return if (canSwipe) {
            super.onInterceptTouchEvent(event)
        } else false
    }
}