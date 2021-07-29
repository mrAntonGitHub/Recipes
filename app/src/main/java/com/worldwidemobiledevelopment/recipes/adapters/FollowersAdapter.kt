package com.worldwidemobiledevelopment.recipes.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.worldwidemobiledevelopment.recipes.R
import com.worldwidemobiledevelopment.recipes.data.Follower
import de.hdodenhof.circleimageview.CircleImageView

// todo refactor + delegates
class FollowersAdapter(private val followers: List<Follower>, val followersAdapterAction: FollowersAdapterAction) : RecyclerView.Adapter<FollowersAdapter.ViewHolder>() {

    interface FollowersAdapterAction{
        fun itemClicked(follower: Follower)
    }

    override fun getItemCount(): Int = followers.count()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_follower, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.follower = followers[position]
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        private val avatar = view.findViewById<CircleImageView>(R.id.avatar)
        private val name = view.findViewById<TextView>(R.id.name)
        private val level = view.findViewById<TextView>(R.id.level)

        var follower : Follower? = null
            set(value){
                value?.let {follower ->
                    field = follower
                    Picasso.get().load("https://upload.wikimedia.org/wikipedia/commons/4/48/Outdoors-man-portrait_%28cropped%29.jpg").into(avatar)
                    name.text = follower.name
//                    level.text = follower.level.title
                    itemView.setOnClickListener { followersAdapterAction.itemClicked(follower) }
                }
            }
    }
}