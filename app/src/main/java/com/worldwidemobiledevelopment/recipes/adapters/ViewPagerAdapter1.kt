package com.worldwidemobiledevelopment.recipes.adapters

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.github.chrisbanes.photoview.PhotoView
import com.worldwidemobiledevelopment.recipes.R


internal class ViewPagerAdapter1(private val uris: List<Uri>, val viewPagerAction: ViewPagerAction1) : PagerAdapter() {

    interface ViewPagerAction1{
        fun clicked(position: Int)
    }

    override fun getCount(): Int {
        return uris.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): View {
        val photoView = PhotoView(container.context)
        photoView.setImageURI(uris[position])
        container.isMotionEventSplittingEnabled = true

        container.addView(
            photoView,
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        return photoView
    }


    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }
}