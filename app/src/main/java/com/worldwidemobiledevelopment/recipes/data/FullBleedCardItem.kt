package com.worldwidemobiledevelopment.recipes.data

import com.bumptech.glide.Glide
import com.worldwidemobiledevelopment.recipes.R
import com.worldwidemobiledevelopment.recipes.databinding.ItemDayMealBinding
import com.worldwidemobiledevelopment.recipes.databinding.ItemMealLandBinding
import com.worldwidemobiledevelopment.recipes.view.home.INSET_TYPE_KEY
import com.xwray.groupie.databinding.BindableItem

class FullBleedCardItem(private val meal: Meal, mealAction: CardItem.MealAction) : BindableItem<ItemDayMealBinding>() {
    init {
        extras.remove(INSET_TYPE_KEY)
    }

    override fun getLayout(): Int {
        return R.layout.item_day_meal
    }

    override fun bind(viewBinding: ItemDayMealBinding, position: Int) {
        Glide
            .with(viewBinding.itemIcon.context)
            .load(meal.icon)
            .override(800,700)
            .into(viewBinding.itemIcon)
    }
}