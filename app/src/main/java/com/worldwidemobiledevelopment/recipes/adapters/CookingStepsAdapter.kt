package com.worldwidemobiledevelopment.recipes.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.worldwidemobiledevelopment.recipes.R
import com.worldwidemobiledevelopment.recipes.data.CookingStep
import kotlinx.android.synthetic.main.item_step.view.*


class CookingStepsAdapter(private val step: List<CookingStep>, private val stepAction: StepAction) :
    RecyclerView.Adapter<CookingStepsAdapter.ViewHolder>() {

    interface StepAction {
        fun onImageClicked(step: CookingStep)
    }

    override fun getItemCount(): Int = step.count()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_step,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.apply {
            stepNumber.text = (position + 1).toString()
            stepDescription.text = step[position].description

            val images = step[position].imageUrlSet

            val adapter = images?.let { ImagesAdapter(it) }
            stepImages.adapter = adapter

        }
    }

    inner class ViewHolder(val item: View) : RecyclerView.ViewHolder(item) {
        val stepDescription: TextView = item.tvStepDescription
        val stepNumber: TextView = item.tvStepNumber
        val stepImages: RecyclerView = item.rvStepImages

        init {
//            ingredientAddToCart.setOnClickListener {
//                ingredientsAction.addToCart(ingredients[adapterPosition])
//            }
        }

    }
}