package com.worldwidemobiledevelopment.recipes.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.FrameLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.worldwidemobiledevelopment.recipes.R
import com.worldwidemobiledevelopment.recipes.data.CookingStep
import com.worldwidemobiledevelopment.recipes.data.Ingredient
import com.worldwidemobiledevelopment.recipes.utils.DynamicTextWatcher

interface IngredientAdapterDelegate {
    fun removeIngredient(position: Int)
    fun addIngredient()
}

// todo refactor
class IngredientAdapter() : RecyclerView.Adapter<IngredientAdapter.ViewHolder>() {

    private var ingredients: MutableList<Ingredient> = mutableListOf()
    private var ingredientAdapterDelegate: IngredientAdapterDelegate? = null

    fun attachDelegate(ingredientAdapterDelegate: IngredientAdapterDelegate) {
        this.ingredientAdapterDelegate = ingredientAdapterDelegate
    }

    fun submitList(ingredients: MutableList<Ingredient>) {
        val diffResult: DiffUtil.DiffResult =
            DiffUtil.calculateDiff(ItemsDiffCallback(this.ingredients, ingredients))
        this.ingredients = ingredients
        diffResult.dispatchUpdatesTo(this)
    }

    class ItemsDiffCallback(var oldList: List<Ingredient>, var newList: List<Ingredient>) :
        DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldList.size
        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return (oldList[oldItemPosition].ingredient.first == newList[newItemPosition].ingredient.first)
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].ingredient.first == newList[newItemPosition].ingredient.first && oldList[oldItemPosition].ingredient.second == newList[newItemPosition].ingredient.second
        }
    }

    override fun getItemCount(): Int {
        return ingredients.size + 1
    }

    override fun getItemViewType(position: Int): Int {
        return when(position){
            in 0 until (ingredients.count()) -> {
                CONTENT_TYPE
            }
            else -> {
                BUTTON_TYPE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            CONTENT_TYPE -> {
                ViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_add_ingredient, parent, false)
                )
            }
            BUTTON_TYPE -> {
                ViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_new_ingredients, parent, false)
                )
            }
            else -> ViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_new_ingredients, parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            CONTENT_TYPE -> { holder.ingredient = ingredients[position] }
            BUTTON_TYPE -> {
                holder.view.setOnClickListener {
                    ingredientAdapterDelegate?.addIngredient() }
            }
        }
    }

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        var ingredient: Ingredient? = null
            set(value) {
                value?.let { newValue ->
                    field = newValue
                    itemView.apply {
                        this.requestFocus()
                        findViewById<EditText>(R.id.name).setText(newValue.ingredient.first)
                        findViewById<EditText>(R.id.volume).setText(newValue.ingredient.second)
                        findViewById<EditText>(R.id.name).addTextChangedListener(
                            DynamicTextWatcher(
                                afterChanged = {
                                    val volume = ingredients[adapterPosition].ingredient.second
                                    ingredients[adapterPosition].ingredient =
                                        it.toString() to volume
                                }
                            ))
                        findViewById<EditText>(R.id.volume).addTextChangedListener(
                            DynamicTextWatcher(
                                afterChanged = {
                                    val name = ingredients[adapterPosition].ingredient.first
                                    ingredients[adapterPosition].ingredient = name to it.toString()
                                }
                            ))

                        findViewById<FrameLayout>(R.id.delete).setOnClickListener {
                            Log.e("ViewHolder", "$adapterPosition")
                            ingredientAdapterDelegate?.removeIngredient(adapterPosition)
                        }
                    }
                }
            }
    }

    fun getIngredientsList(): List<Ingredient> = ingredients
}
