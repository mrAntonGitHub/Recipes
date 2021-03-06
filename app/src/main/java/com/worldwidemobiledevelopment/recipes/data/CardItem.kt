package com.worldwidemobiledevelopment.recipes.data

import com.bumptech.glide.Glide
import com.worldwidemobiledevelopment.recipes.R
import com.worldwidemobiledevelopment.recipes.databinding.ItemMealBinding
import com.worldwidemobiledevelopment.recipes.view.home.INSET
import com.worldwidemobiledevelopment.recipes.view.home.INSET_TYPE_KEY
import com.xwray.groupie.databinding.BindableItem
import kotlin.random.Random

// Basic Groupie class, which binds all views
open class CardItem(private val recipe: Recipe, private val mealAction: MealAction) : BindableItem<ItemMealBinding>() {

    interface MealAction {
        fun mealClicked(recipe: Recipe)
        fun mealLiked(recipe: Recipe)
        fun addToBookmarkClicked(recipe: Recipe)
    }

    override fun getLayout(): Int {
        return R.layout.item_meal
    }

    val likedListIds = mutableListOf<Recipe>()
    val bookedListIds = mutableListOf<Recipe>()

    override fun bind(viewBinding: ItemMealBinding, position: Int) {

        viewBinding.apply {

            ivLike.isSelected = likedListIds.contains(recipe)

            mealCard.setOnClickListener {
                mealAction.mealClicked(recipe)
            }

            ivLike.setOnClickListener {
                if (it.isSelected) {
                    likedListIds.remove(recipe)
                } else {
                    likedListIds.add(recipe)
                }
                it.isSelected = !it.isSelected
//                mealAction.mealLiked(recipe)
            }


            tvkcal.text = "${recipe.kcal} ккал.}"

            cookingTime.text = recipe.cooking_minutes.toString() + " мин."

        }

        viewBinding.tvMealName.text = recipe.name
        Glide
            .with(viewBinding.itemIcon.context)
            .load(R.drawable.ic_empty_plate)
            .override(800, 700)
            .into(viewBinding.itemIcon)

        viewBinding.mealRating.rating = Random.nextFloat() * 3 / 2

    }

    init {
        extras[INSET_TYPE_KEY] = INSET
    }
}
