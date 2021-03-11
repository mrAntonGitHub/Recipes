package com.worldwidemobiledevelopment.recipes.data

import androidx.annotation.ColorInt
import com.bumptech.glide.Glide
import com.worldwidemobiledevelopment.recipes.R
import com.worldwidemobiledevelopment.recipes.databinding.ItemMealBinding
import com.worldwidemobiledevelopment.recipes.databinding.ItemMealLandBinding
import com.xwray.groupie.databinding.BindableItem

/**
 * A card item with a fixed width so it can be used with a horizontal layout manager.
 */

// REPLACE MEAL on RECIPE
class CarouselCardItem(private val meal: Meal, val mealAction: CardItem.MealAction) : BindableItem<ItemMealLandBinding>() {
    override fun getLayout(): Int {
        return R.layout.item_meal_land
    }

    override fun bind(viewBinding: ItemMealLandBinding, position: Int) {
        Glide
            .with(viewBinding.itemIcon.context)
            .load(meal.icon)
            .override(800,700)
            .into(viewBinding.itemIcon)
    }
}