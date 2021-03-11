package com.worldwidemobiledevelopment.recipes.data

import android.view.View
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.worldwidemobiledevelopment.recipes.R
import com.worldwidemobiledevelopment.recipes.databinding.ItemHeaderBinding
import com.xwray.groupie.databinding.BindableItem

data class HeaderItem(
    @StringRes private val titleResId: Int? = null,
    @StringRes private val subtitleResId: Int? = null,
    @DrawableRes private val iconResId: Int? = null,
    private val onClickListener: View.OnClickListener? = null
) : BindableItem<ItemHeaderBinding>() {
    override fun bind(viewBinding: ItemHeaderBinding, position: Int) {
        viewBinding.apply {
            titleResId?.let {
                title.setText(it)
                title.visibility = View.VISIBLE
            }
            if (titleResId == null) title.visibility =  View.GONE else title.visibility = View.VISIBLE
            subtitleResId?.let {
                subtitle.setText(subtitleResId)
                subtitle.visibility = View.VISIBLE
            }
            if (subtitleResId == null) subtitle.visibility = View.GONE else subtitle.visibility = View.VISIBLE
            iconResId?.let {
                icon.setImageResource(iconResId)
                icon.setOnClickListener(onClickListener)
                icon.visibility = View.VISIBLE
            }
            if (iconResId == null) icon.visibility = View.GONE else icon.visibility = View.VISIBLE
        }
    }

    override fun getLayout() = R.layout.item_header
}