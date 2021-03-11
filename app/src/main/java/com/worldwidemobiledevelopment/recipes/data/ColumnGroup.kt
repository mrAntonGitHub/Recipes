package com.worldwidemobiledevelopment.recipes.data

import com.xwray.groupie.Group
import com.xwray.groupie.GroupDataObserver
import com.xwray.groupie.Item
import com.xwray.groupie.databinding.BindableItem


/**
 * A simple, non-editable, non-nested group of Items which displays a list as vertical columns.
 */
class ColumnGroup(items: List<BindableItem<*>?>) : Group{

    private val items : MutableList<BindableItem<*>> = ArrayList()

    init {
        for (i in items.indices) {
            // Rearrange items so that the adapter appears to arrange them in vertical columns
            var index: Int
            if (i % 2 == 0) {
                index = i / 2
            } else {
                index = (i - 1) / 2 + (items.size / 2f).toInt()
                // If columns are uneven, we'll put an extra one at the end of the first column,
                // meaning the second column's indices will all be increased by 1
                if (items.size % 2 == 1) index++
            }
            val trackItem = items[index]
            trackItem?.let { this.items.add(it) }
        }
    }

    override fun getItemCount() = items.size

    override fun getItem(position: Int): Item<*> = items[position]

    override fun getPosition(item: Item<*>) = items.indexOf(item)

    override fun registerGroupDataObserver(groupDataObserver: GroupDataObserver) {
        // no real need to do anything here
    }

    override fun unregisterGroupDataObserver(groupDataObserver: GroupDataObserver) {
        // no real need to do anything here
    }
}