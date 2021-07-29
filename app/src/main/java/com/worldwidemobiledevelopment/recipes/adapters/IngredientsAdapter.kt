package com.worldwidemobiledevelopment.recipes.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.worldwidemobiledevelopment.recipes.R
import kotlinx.android.synthetic.main.item_ingredient.view.*

// todo refactor + delegates
class IngredientsAdapter(private val ingredients: List<Pair<String, String>>, private val ingredientsAction: IngredientsAction) : RecyclerView.Adapter<IngredientsAdapter.ViewHolder>() {

    interface IngredientsAction {
        fun addToCart(ingredient: Pair<String, String>)
    }

    override fun getItemCount(): Int = ingredients.count()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_ingredient, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.apply {
            ingredientName.text = ingredients[position].first
            ingredientVolume.text = ingredients[position].second
        }
    }

    inner class ViewHolder(val item: View) : RecyclerView.ViewHolder(item) {
        val ingredientName: TextView = item.ingredientName
        val ingredientVolume: TextView = item.ingredientVolume
//        private val ingredientAddToCart: Button = item.ingredientAddToCart

        init {
//            ingredientAddToCart.setOnClickListener {
//                ingredientsAction.addToCart(ingredients[adapterPosition])
//            }
        }

    }
}