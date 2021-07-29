package com.worldwidemobiledevelopment.recipes.adapters

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.worldwidemobiledevelopment.recipes.R
import com.worldwidemobiledevelopment.recipes.data.CookingStep
import com.worldwidemobiledevelopment.recipes.utils.DynamicTextWatcher

interface StepAdapterDelegate {
    fun removeStep(position: Int)
    fun addStep()
    fun addStepImages(position: Int)
    fun onImageClicked(stepPosition: Int, imagePosition: Int)
}

class StepAdapter : RecyclerView.Adapter<StepAdapter.ViewHolder>() {
    private var steps: MutableList<CookingStep> = mutableListOf()
    private var stepAdapterDelegate: StepAdapterDelegate? = null

    fun attachDelegate(stepAdapterDelegate: StepAdapterDelegate) {
        this.stepAdapterDelegate = stepAdapterDelegate
    }

    fun submitList(steps: MutableList<CookingStep>) {
        val diffResult: DiffUtil.DiffResult = DiffUtil.calculateDiff(ItemsDiffCallback(this.steps, steps))
        this.steps = steps
        diffResult.dispatchUpdatesTo(this)
    }

    class ItemsDiffCallback(var oldList: List<CookingStep>, var newList: List<CookingStep>) :
        DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldList.size
        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return (oldItemPosition == newItemPosition)
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].description == newList[newItemPosition].description && oldList[oldItemPosition].imageUrlSet == newList[newItemPosition].imageUrlSet
        }
    }

    override fun getItemCount(): Int {
        return steps.count() + 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when(viewType){
            CONTENT_TYPE -> {
                ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_add_step, parent, false), stepAdapterDelegate)
            }
            BUTTON_TYPE -> {
                ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_new_step, parent, false), stepAdapterDelegate)
            }
            else -> ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_new_step, parent, false), stepAdapterDelegate)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when(position){
            in 0 until (steps.count()) -> {
                CONTENT_TYPE
            }
            else -> {
                BUTTON_TYPE
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when(getItemViewType(position)){
            CONTENT_TYPE -> { holder.step = steps[position] }
            BUTTON_TYPE -> {
                holder.view.setOnClickListener {
                    stepAdapterDelegate?.addStep()
                }
            }
        }
    }

    inner class ViewHolder(val view: View, private val stepAdapterDelegate: StepAdapterDelegate?) : RecyclerView.ViewHolder(view), StepImagesDelegate {
        private val imagesAdapter = StepImagesAdapter()

        var step: CookingStep? = null
            set(value) {
                value?.let { newValue ->
                    field = newValue
                    itemView.apply {
                        this.requestFocus()
                        findViewById<TextView>(R.id.number).text = (adapterPosition + 1).toString()
                        findViewById<RecyclerView>(R.id.rvStepImages).adapter = imagesAdapter
                        findViewById<EditText>(R.id.description).setText(newValue.description)
                        findViewById<EditText>(R.id.description).addTextChangedListener(DynamicTextWatcher(
                            afterChanged = {
                                steps[adapterPosition].description = it.toString()
                            }
                        ))
                        imagesAdapter.attachDelegate(this@ViewHolder)
                        newValue.imageUrlSet?.let {
                            Log.d("StepAdapter", "$it")
                            imagesAdapter.submitList(it)
                        }
                        findViewById<FrameLayout>(R.id.delete).setOnClickListener {
                            stepAdapterDelegate?.removeStep(adapterPosition)
                        }
                    }
                }
            }

        override fun addStepImages() {
            stepAdapterDelegate?.addStepImages(adapterPosition)
        }

        override fun removeImage(position: Int) {
            val list = step?.imageUrlSet?.toMutableList()
            list?.removeAt(position)

            steps[adapterPosition].imageUrlSet = list
            list?.toList()?.let { imagesAdapter.submitList(it) }
        }

        override fun onImageClicked(position: Int) {
            stepAdapterDelegate?.onImageClicked(adapterPosition, position)
        }
    }

    fun getStepsList() : List<CookingStep> = steps
}