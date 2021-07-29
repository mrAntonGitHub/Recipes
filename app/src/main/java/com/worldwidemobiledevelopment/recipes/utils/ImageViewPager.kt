package com.worldwidemobiledevelopment.recipes.utils

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.SurfaceView
import android.view.View
import androidx.viewpager.widget.PagerTabStrip
import androidx.viewpager.widget.ViewPager


class ImageViewPager : ViewPager {
    constructor(context: Context?) : super(context!!)
    constructor(context: Context?, attrs: AttributeSet?) : super(
        context!!, attrs
    )

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        return try {
            super.onInterceptTouchEvent(ev)
        } catch (e: IllegalArgumentException) {
            false
        }
    }

    override fun canScroll(v: View?, checkV: Boolean, dx: Int, x: Int, y: Int): Boolean {
        return if (v is SurfaceView || v is PagerTabStrip) {
            true
        } else super.canScroll(v, checkV, dx, x, y)
    }


}