package com.worldwidemobiledevelopment.recipes.adapters

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.worldwidemobiledevelopment.recipes.R
import kotlinx.android.synthetic.main.image.view.*

interface ViewPagerAdapterDelegate{
    fun imageClicked(position: Int)
    fun imageLongClicked(position: Int)
}

internal class ImageViewPagerAdapter(private val uris: List<Uri>, private val viewPagerAdapterDelegate: ViewPagerAdapterDelegate) : PagerAdapter() {

    override fun getCount(): Int {
        return uris.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): View {
        val layoutInflater: LayoutInflater = container.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = layoutInflater.inflate(R.layout.image, container, false)

        view.image.setImageURI(uris[position])

        view.setOnClickListener {
            viewPagerAdapterDelegate.imageClicked(position)
        }

        view.setOnLongClickListener {
            viewPagerAdapterDelegate.imageLongClicked(position)
            true
        }

        container.addView(view)

        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }
}