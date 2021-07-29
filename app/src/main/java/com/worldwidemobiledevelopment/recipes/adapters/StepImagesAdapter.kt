package com.worldwidemobiledevelopment.recipes.adapters

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.worldwidemobiledevelopment.recipes.R

const val CONTENT_TYPE = 1
const val BUTTON_TYPE = 2

interface StepImagesDelegate {
    fun addStepImages()
    fun removeImage(position: Int)
    fun onImageClicked(position: Int)
}

class StepImagesAdapter : RecyclerView.Adapter<StepImagesAdapter.ViewHolder>() {

    private var images: List<String> = listOf()
    private var stepImagesDelegate: StepImagesDelegate? = null

    override fun getItemCount() = images.count() + 1

    fun attachDelegate(stepImagesDelegate: StepImagesDelegate) {
        this.stepImagesDelegate = stepImagesDelegate
    }

    fun submitList(images: List<String>) {
        val diffResult: DiffUtil.DiffResult = DiffUtil.calculateDiff(ItemsDiffCallback(this.images, images))
        this.images = images
        diffResult.dispatchUpdatesTo(this)
    }

    class ItemsDiffCallback(var oldList: List<String>, var newList: List<String>) :
        DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldList.size
        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return (oldItemPosition == newItemPosition)
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            CONTENT_TYPE -> {
                ViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_image_view, parent, false)
                )
            }
            BUTTON_TYPE -> {
                ViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_add_element, parent, false)
                )
            }
            else -> ViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_add_element, parent, false)
            )
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            in 0 until images.count() -> {
                CONTENT_TYPE
            }
            else -> {
                BUTTON_TYPE
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            CONTENT_TYPE -> {
                holder.imageView?.setOnClickListener {
                    Log.e("StepImagesAdapter", "Click")
                    holder.onImageClicked()
                }
                holder.delete?.setOnClickListener { holder.removeImage() }
                Picasso.get()
                    .load(images.get(position))
                    .resize(300, 300)
                    .centerCrop()
                    .into(holder.imageView)
            }
            BUTTON_TYPE -> {
                holder.newElement?.setOnClickListener { holder.addStepImage() }
            }
        }
    }

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView? = view.findViewById(R.id.imageView)
        val delete: FrameLayout? = view.findViewById(R.id.delete)
        val imageCard: CardView? = view.findViewById(R.id.imageCard)
        val newElement: FrameLayout? = view.findViewById(R.id.newElement)

        fun removeImage(){
            stepImagesDelegate?.removeImage(adapterPosition)
        }

        fun addStepImage(){
            stepImagesDelegate?.addStepImages()
        }

        fun onImageClicked(){
            Log.e("ViewHolder", "Clicked")
            stepImagesDelegate?.onImageClicked(adapterPosition)
        }
    }

}