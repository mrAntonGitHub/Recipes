package com.worldwidemobiledevelopment.recipes.data

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.worldwidemobiledevelopment.recipes.R
import com.worldwidemobiledevelopment.recipes.databinding.ItemCarouselBinding
import com.xwray.groupie.GroupieAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.OnItemClickListener
import com.xwray.groupie.databinding.BindableItem
import com.xwray.groupie.databinding.GroupieViewHolder

/**
 * A horizontally scrolling RecyclerView, for use in a vertically scrolling RecyclerView.
 */
data class CarouselItem(
    private val carouselDecoration: ItemDecoration,
    private val adapter: GroupieAdapter
) : BindableItem<ItemCarouselBinding>(), OnItemClickListener {

    init {
        adapter.setOnItemClickListener(this)
    }

    override fun createViewHolder(itemView: View): GroupieViewHolder<ItemCarouselBinding> {
        val viewHolder: GroupieViewHolder<ItemCarouselBinding> = super.createViewHolder(itemView)
        val recyclerView: RecyclerView = viewHolder.binding.recyclerView
        recyclerView.addItemDecoration(carouselDecoration)
        recyclerView.layoutManager = LinearLayoutManager(recyclerView.context, LinearLayoutManager.HORIZONTAL, false)
        return viewHolder
    }

    override fun bind(viewBinding: ItemCarouselBinding, position: Int) {
        viewBinding.recyclerView.adapter = adapter
    }

    override fun getLayout() = R.layout.item_carousel


    override fun onItemClick(item: Item<*>, view: View) = adapter.remove(item)
}