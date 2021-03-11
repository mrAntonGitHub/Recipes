package com.worldwidemobiledevelopment.recipes.data

import com.worldwidemobiledevelopment.recipes.R
import com.worldwidemobiledevelopment.recipes.databinding.ItemMealBinding
import com.xwray.groupie.databinding.BindableItem

sealed class RecommendItems

data class TopMeal(
    val subItems: List<Meal>
) : RecommendItems()

class Meal(
    val id: String,
    val name: String,
    val icon: Int
) : BindableItem<ItemMealBinding>() {
    override fun bind(viewBinding: ItemMealBinding, position: Int) {
//        viewBinding.itemName.text = name
//        viewBinding.itemIcon.setImageResource(icon)
    }

    override fun getLayout() = R.layout.item_meal
}

data class PopularMeal(
    val subItems: List<Meal>
) : RecommendItems()

//class MenuMeals(
//    val name: String,
//    val icon: Int,
//    val height: Int?,
//    val width: Int?,
//    val layoutManager: RecyclerView.LayoutManager
//) : Items()


//class Title(
//    val title: String
//) : Items()
