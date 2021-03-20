package com.worldwidemobiledevelopment.recipes.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.worldwidemobiledevelopment.recipes.R
import kotlinx.android.synthetic.main.item_image_view.view.*

class ImagesAdapter(private val images: List<String>) : RecyclerView.Adapter<ImagesAdapter.ViewHolder>() {

    override fun getItemCount() = images.count()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_image_view, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Picasso.get()
            .load(images[position])
            .resize(300, 300)
            .centerCrop()
            .into(holder.imageView)
    }


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val imageView: ImageView = view.imageView
    }

}