package com.worldwidemobiledevelopment.recipes.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.worldwidemobiledevelopment.recipes.R
import com.worldwidemobiledevelopment.recipes.data.Recipe

// todo refactor + delegates
class MyRecipeAdapter(private val recipes: List<Recipe>, val myRecipeAdapterAction: MyRecipeAdapterAction) : RecyclerView.Adapter<MyRecipeAdapter.ViewHolder>() {

    interface MyRecipeAdapterAction{
        fun itemClicked(recipe: Recipe)
    }

    override fun getItemCount(): Int  {
        return recipes.count()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_my_meal, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ingred = listOf<String>(
            "Молоко", "Соль","Перец", "Перец", "Кинза", "Базилик",
        )

//        holder.follower = followers[position]
    }

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view){
        val ingredients = view.findViewById<TextView>(R.id.ingredients)
//        private val avatar = view.findViewById<CircleImageView>(R.id.avatar)
//        private val name = view.findViewById<TextView>(R.id.name)
//        private val level = view.findViewById<TextView>(R.id.level)

//        var follower : Follower? = null
//            set(value){
//                value?.let {follower ->
//                    field = follower
//                    Picasso.get().load("https://upload.wikimedia.org/wikipedia/commons/4/48/Outdoors-man-portrait_%28cropped%29.jpg").into(avatar)
//                    name.text = follower.name
//                    level.text = follower.level.title
//                    itemView.setOnClickListener { followersAdapterAction.itemClicked(follower) }
//                }
//            }
    }
}